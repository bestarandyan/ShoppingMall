package com.huoqiu.framework.rest;

import static org.apache.http.conn.params.ConnManagerParams.setMaxConnectionsPerRoute;
import static org.apache.http.conn.params.ConnManagerParams.setMaxTotalConnections;
import static org.apache.http.conn.params.ConnManagerParams.setTimeout;
import static org.apache.http.params.HttpConnectionParams.setConnectionTimeout;
import static org.apache.http.params.HttpConnectionParams.setSoTimeout;
import static org.apache.http.params.HttpProtocolParams.setContentCharset;
import static org.apache.http.params.HttpProtocolParams.setUseExpectContinue;
import static org.apache.http.params.HttpProtocolParams.setUserAgent;
import static org.apache.http.params.HttpProtocolParams.setVersion;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.Assert;

/**
 * {@link org.springframework.http.client.ClientHttpRequestFactory} implementation that uses <a href="http://hc.apache.org/httpcomponents-client-ga/httpclient/">Http Components HttpClient</a> to
 * create requests.
 * 
 * <p>
 * Allows to use a pre-configured {@link HttpClient} instance - potentially with authentication, HTTP connection pooling, etc.
 * 
 * @author Oleg Kalnichevski
 * @author Roy Clarkson
 * @author leo
 * @since 1.1
 */
public class HttpComponentsClientHttpRequestFactory implements ClientHttpRequestFactory, DisposableBean {

	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;

	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;

	private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);

	protected final static String HTTP_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.5 Safari/537.22";

	private ConcurrentHashMap<String, HttpUriRequest> requestPool = new ConcurrentHashMap<String, HttpUriRequest>(6);

	private HttpClient httpClient = createHttpClient();

	/**
	 * Create a new instance of the {@code HttpComponentsClientHttpRequestFactory} with a default {@link HttpClient} that uses a default {@link ThreadSafeClientConnManager}.
	 */
	public HttpComponentsClientHttpRequestFactory() {
	}

	public void abortRequest(String url) {
		if (requestPool.containsKey(url))
			requestPool.remove(url).abort();
	}

	public synchronized void removeRequest(String url) {
		if (requestPool.containsKey(url))
			requestPool.remove(url);
	}

	public boolean containsRequest(String url) {
		return requestPool.containsKey(url);
	}

	public void abortAllRequests() {
		for (HttpUriRequest r : requestPool.values()) {
			r.abort();
		}

		requestPool.clear();

	}

	private HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		setVersion(params, HttpVersion.HTTP_1_1);
		setContentCharset(params, HTTP.UTF_8);
		setUseExpectContinue(params, true);
		setUserAgent(params, HTTP_USER_AGENT);
		setTimeout(params, 1000);// 第一行设置ConnectionPoolTimeout：这定义了从ConnectionManager管理的连接池中取出连接的超时时间，此处设置为1秒。
		setConnectionTimeout(params, 2000);// 第二行设置ConnectionTimeout：这定义了通过网络与服务器建立连接的超时时间。Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间，此处设置为2秒。
		setSoTimeout(params, DEFAULT_READ_TIMEOUT_MILLISECONDS);// 第三行设置SocketTimeout:这定义了Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间，此处设置为4秒。

		setMaxTotalConnections(params, DEFAULT_MAX_TOTAL_CONNECTIONS);
		setMaxConnectionsPerRoute(params, new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS_PER_ROUTE));

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		// requstingList.clear();
		DefaultHttpClient dhc = new DefaultHttpClient(conMgr, params);

		dhc.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(1, false) {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				/*System.out.println("=========================retryRequest（）===============================");
				boolean result = super.retryRequest(exception, executionCount, context);
				if (!result) {
					if (exception instanceof SSLHandshakeException) {
						// SSL handshake exception
						return true;
					}
					if (exception instanceof ConnectException) {
						// Connection refused
						return true;
					}
				}

				return result;*/
				return false;
			}
		});
		return dhc;
	}

	/**
	 * Create a new instance of the HttpComponentsClientHttpRequestFactory with the given {@link HttpClient} instance.
	 * 
	 * @param httpClient
	 *            the HttpClient instance to use for this factory
	 */
	public HttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
		Assert.notNull(httpClient, "HttpClient must not be null");
		this.httpClient = httpClient;
	}

	/**
	 * Set the {@code HttpClient} used by this factory.
	 */
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * Return the {@code HttpClient} used by this factory.
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * Set the connection timeout for the underlying HttpClient. A timeout value of 0 specifies an infinite timeout.
	 * 
	 * @param timeout
	 *            the timeout value in milliseconds
	 */
	public void setConnectTimeout(int timeout) {
		Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
		getHttpClient().getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
	}

	/**
	 * Set the socket read timeout for the underlying HttpClient. A timeout value of 0 specifies an infinite timeout.
	 * 
	 * @param timeout
	 *            the timeout value in milliseconds
	 */
	public void setReadTimeout(int timeout) {
		Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
		getHttpClient().getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
	}

	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		HttpUriRequest httpRequest = createHttpRequest(httpMethod, uri);

		// add by leo
		requestPool.put(httpRequest.getURI().toString(), httpRequest);

		postProcessHttpRequest(httpRequest);
		return new HttpComponentsClientHttpRequest(getHttpClient(), httpRequest, createHttpContext(httpMethod, uri));
	}

	/**
	 * Create a HttpComponents HttpUriRequest object for the given HTTP method and URI specification.
	 * 
	 * @param httpMethod
	 *            the HTTP method
	 * @param uri
	 *            the URI
	 * @return the HttpComponents HttpUriRequest object
	 */
	protected HttpUriRequest createHttpRequest(HttpMethod httpMethod, URI uri) {
		switch (httpMethod) {
		case GET:
			return new HttpGet(uri);
		case DELETE:
			return new HttpDelete(uri);
		case HEAD:
			return new HttpHead(uri);
		case OPTIONS:
			return new HttpOptions(uri);
		case POST:
			return new HttpPost(uri);
		case PUT:
			return new HttpPut(uri);
		case TRACE:
			return new HttpTrace(uri);
		default:
			throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
		}
	}

	/**
	 * Template method that allows for manipulating the {@link HttpUriRequest} before it is returned as part of a {@link HttpComponentsClientHttpRequest}.
	 * <p>
	 * The default implementation is empty.
	 * 
	 * @param httpRequest
	 *            the HTTP request object to process
	 */
	protected void postProcessHttpRequest(HttpUriRequest httpRequest) {
		HttpParams params = httpRequest.getParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
	}

	/**
	 * Template methods that creates a {@link HttpContext} for the given HTTP method and URI.
	 * <p>
	 * The default implementation returns {@code null}.
	 * 
	 * @param httpMethod
	 *            the HTTP method
	 * @param uri
	 *            the URI
	 * @return the http context
	 */
	protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
		return null;
	}

	/**
	 * Shutdown hook that closes the underlying {@link ClientConnectionManager}'s connection pool, if any.
	 */
	public void destroy() {
		getHttpClient().getConnectionManager().shutdown();
	}

	public interface RequestCreatedListener {
		public void OnRequestCreated(HttpUriRequest uriRequest);
	}

}