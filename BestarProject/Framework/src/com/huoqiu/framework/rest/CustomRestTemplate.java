package com.huoqiu.framework.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.huoqiu.framework.exception.ClientException;
import com.huoqiu.framework.exception.ServerException;

public class CustomRestTemplate extends RestTemplate {
	private final static String TAG = CustomRestTemplate.class.getSimpleName();
	// private final static String CLOSE_EXCEPTION = "There's something wrong while closing request";

	private static HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

	// private WeakReference<RestClientSupport> mRestWeakReference;

	public static HttpComponentsClientHttpRequestFactory getHttpRequestFactory() {
		return requestFactory;
	}

	public CustomRestTemplate() {
		// mRestWeakReference = new WeakReference<RestClientSupport>(restService);
		setRequestFactory(requestFactory);

	}

	/**
	 * Execute the given method on the provided URI. The {@link ClientHttpRequest} is processed using the {@link RequestCallback}; the
	 * response with the {@link ResponseExtractor}.
	 * 
	 * @param url
	 *            the fully-expanded URL to connect to
	 * @param method
	 *            the HTTP method to execute (GET, POST, etc.)
	 * @param requestCallback
	 *            object that prepares the request (can be <code>null</code>)
	 * @param responseExtractor
	 *            object that extracts the return value from the response (can be <code>null</code>)
	 * @return an arbitrary object, as returned by the {@link ResponseExtractor}
	 */
	protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) {

		Assert.notNull(url, "'url' must not be null");
		Assert.notNull(method, "'method' must not be null");
		
		ClientHttpResponse response = null;
		ClientHttpRequest request = null;
		try {

			// add by leo
			if (containsRequest(url.toString()))
				abortRequest(url.toString());

			request = createRequest(url, method);

			if (requestCallback != null) {
				requestCallback.doWithRequest(request);
			}

			response = request.execute();
			
			if (!getErrorHandler().hasError(response)) {
				logResponseStatus(method, url, response);
			} else {
				handleResponseError(method, url, response);
			}
			if (responseExtractor != null) {
				return responseExtractor.extractData(response);
			} else {
				return null;
			}
			
		} catch (ConnectTimeoutException e) {
			throw new ClientException("请求超时，请稍后重试！", ClientException.REQUEST_NETWORK, e);
		} catch (Exception ex) {
			if (ex.getMessage() != null) {
				String str = ex.getMessage().toLowerCase(Locale.getDefault());
				if (str.contains("shutdown") || str.contains("request already aborted") || str.contains("socket closed"))
					throw new ClientException("request already aborted", ClientException.REQUEST_ABORTED);
			}

			throw new ServerException("请求异常，请稍后再试！", ex);
		} finally {
			if (response != null) {
				response.close();
			}
			removeRequest(url.toString());
		}
	}

	public void abortRequest(String url) {
		if (containsRequest(url))
			requestFactory.abortRequest(url);
	}

	public void removeRequest(String url) {
		if (containsRequest(url))
			requestFactory.removeRequest(url);
	}

	public boolean containsRequest(String url) {
		return requestFactory.containsRequest(url);
	}

	public void abortAllRequests() {
		requestFactory.abortAllRequests();
	}
	
	private void logResponseStatus(HttpMethod method, URI url, ClientHttpResponse response) {
		if (Log.isLoggable(TAG, Log.DEBUG)) {
			try {
				Log.d(TAG,
						method.name() + " request for \"" + url + "\" resulted in " + response.getStatusCode() + " ("
								+ response.getStatusText() + ")");
			} catch (IOException e) {
				// ignore
			}
		}
	}

	private void handleResponseError(HttpMethod method, URI url, ClientHttpResponse response) throws IOException {
		if (Log.isLoggable(TAG, Log.WARN)) {
			try {
				Log.w(TAG,
						method.name() + " request for \"" + url + "\" resulted in " + response.getStatusCode() + " ("
								+ response.getStatusText() + "); invoking error handler");
			} catch (IOException e) {
				// ignore
			}
		}
		getErrorHandler().handleError(response);

	}
}
