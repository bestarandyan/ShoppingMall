package com.huoqiu.framework.rest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;

import org.androidannotations.api.BackgroundExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.huoqiu.framework.backstack.BackOpFragment;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.exception.ClientException;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.exception.ServerException;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.framework.util.NetworkUtil;
import com.huoqiu.framework.util.ReflectTool;
import com.huoqiu.framework.util.StringUtil;

public abstract class RestProxyFragment extends BackOpFragment {
	private final static String TAG = RestProxyFragment.class.getSimpleName();
	public final static String REQUEST_URL_SUFFIX = ".rest";
	public final static String REPORT_URL = Configuration.DEFAULT.getRootUrl() + "/report/clientReport.rest";

	private HttpComponentsClientHttpRequestFactory requestFactory = CustomRestTemplate.getHttpRequestFactory();
	private List<String> currentRequestUrls = new ArrayList<String>();// 当前fragment中正在执行的所有请求

	ExecutorService executor = Executors.newSingleThreadExecutor();
	final FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {// 使用Callable接口作为构造参数
				public Boolean call() {
					return true;
				}
			});

	{
		try {
			initRestService();
		} catch (IllegalAccessException e) {
			throw new ClientException(e.getMessage(), ClientException.NATIVE_SYSTEM_EXCEPTION);
		} catch (IllegalArgumentException e) {
			throw new ClientException(e.getMessage(), ClientException.NATIVE_SYSTEM_EXCEPTION);
		}
	}

//	protected void addOverlay() {
//		ViewGroup vg = (ViewGroup) getView();
//		// 修改空保护
//		if (vg != null) {
//			final Bitmap bitmap = vg.getDrawingCache();
//			vg.getChildAt(0).setVisibility(View.GONE);
//			if (bitmap != null) {
//				ImageView iv = new ImageView(vg.getContext());
//				// System.err.println("---------start------------>" + iv);
//				// iv.setBackgroundColor(0xffffffff);
//				iv.setImageBitmap(bitmap);
//				iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//				vg.addView(iv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			}
//		}
//	}
//
//	protected void removeOverlay() {
//		ViewGroup vg = (ViewGroup) getView();
//		if (vg != null) {
//			vg.getChildAt(0).setVisibility(View.VISIBLE);
//			int imageViewIndex = vg.getChildCount() - 1;
//			if (vg.getChildCount() > 1 && vg.getChildAt(imageViewIndex) instanceof ImageView) {
//				// System.err.println("----------end----------->" + vg.getChildAt(imageViewIndex));
//				vg.removeViewAt(imageViewIndex);
//			}
//		}
//	}

	private List<Animation.AnimationListener> animationListeners = new ArrayList<Animation.AnimationListener>();

	protected Animation.AnimationListener removeAnimationListener(Animation.AnimationListener animationListener) {
		if (animationListeners.contains(animationListener))
			animationListeners.remove(animationListener);
		return animationListener;
	}

    protected void addAnimationListener(Animation.AnimationListener animationListener) {
		if (!animationListeners.contains(animationListener))
			animationListeners.add(animationListener);
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		Animation anim = null;
		if (enter && nextAnim != 0) {
			anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
			if (anim != null) {
				anim.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						for (Animation.AnimationListener l : animationListeners) {
							l.onAnimationStart(animation);
						}
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						for (Animation.AnimationListener l : animationListeners) {
							l.onAnimationEnd(animation);
						}
						executor.execute(future);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						for (Animation.AnimationListener l : animationListeners) {
							l.onAnimationRepeat(animation);
						}
					}
				});
			}
		} else {
			anim = super.onCreateAnimation(transit, enter, nextAnim);
			executor.execute(future);
		}
		return anim;
	}

	@Override
	public void onDestroy() {
		abortFragmentAllInvokingRequests();
		animationListeners.clear();
		super.onDestroy();
	}

	protected void abortFragmentAllInvokingRequests() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            for (String url : currentRequestUrls)
                requestFactory.abortRequest(url);
            currentRequestUrls.clear();
        } catch (Exception ex) {
        } finally {
            lock.unlock();
        }
    }

	@Override
	public void onPause() {
		ManyiUtils.closeKeyBoard(getActivity(), getView());
		super.onPause();
	}

	private void initRestService() throws IllegalAccessException, IllegalArgumentException {
		Set<Field> set = new HashSet<Field>();
		set.addAll(Arrays.asList(getClass().getSuperclass().getDeclaredFields()));
		set.addAll(Arrays.asList(getClass().getSuperclass().getFields()));

		for (Field f : set) {
			f.setAccessible(true);

			final Class<?> type = f.getType();

			if (RestService.class.isAssignableFrom(type)) {
				f.set(this, Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { type }, new InvocationHandler() {

					Handler handler = new Handler();

					private CustomRestTemplate restTemplate = new CustomRestTemplate();
					private RequestMapping clazz = type.getAnnotation(RequestMapping.class);
					private String accept = clazz.accept();
					private HttpMethod httpMethod = clazz.method();
					private Class<? extends HttpMessageConverter<Object>>[] converters = clazz.converters();
					private Class<? extends ClientHttpRequestInterceptor>[] interceptors = clazz.interceptors();

					@Override
					public Object invoke(Object proxy, final Method method, Object[] args) throws Throwable {
						String _url = Configuration.DEFAULT.getRootUrl() + clazz.value();

						if (null != getActivity() && !NetworkUtil.isOpenNetwork(getActivity())) {
							throw new ClientException("当前网络不可用", ClientException.REQUEST_NETWORK);
						}

						if (method.getName().equals("abort")) {
							String abortMethod = String.class.cast(args[0]);

							Method _abortMethod = null;
							for (Class<?> c : proxy.getClass().getInterfaces()) {
								_abortMethod = ReflectTool.getMethod(c, abortMethod);
								if (_abortMethod != null)
									break;
							}

							if (_abortMethod == null)
								return null;

							RequestMapping rm = _abortMethod.getAnnotation(RequestMapping.class);

							if (rm == null) {
								_url = _url + "/" + _abortMethod.getName() + REQUEST_URL_SUFFIX;
							} else {
								_url = _url
										+ (StringUtil.isEmptyOrNull(rm.value()) ? "/" + _abortMethod.getName() + REQUEST_URL_SUFFIX : rm
												.value());
							}

							restTemplate.abortRequest(_url);
							return null;
						}

						String _accept = accept;
						HttpMethod _httpMethod = httpMethod;
						Class<? extends HttpMessageConverter<Object>>[] _converters = converters;
						Class<? extends ClientHttpRequestInterceptor>[] _interceptors = interceptors;

						RequestMapping m = method.getAnnotation(RequestMapping.class);
						if (m == null) {
							_url = _url + "/" + method.getName();
						} else {
							_url = _url + (StringUtil.isEmptyOrNull(m.value()) ? "/" + method.getName() : m.value());
							_accept = m.accept();
							_httpMethod = m.method();
							_converters = m.converters();
							_interceptors = m.interceptors();
						}
//						Log.d("bestar", _url);
//						if (restTemplate.containsRequest(_url))
//							throw new ClientException("Repeated request, The request will be cancelled!", ClientException.REPEATED_REQUEST);

						for (Class<? extends HttpMessageConverter<Object>> c : _converters) {
							HttpMessageConverter<Object> converter = c.newInstance();
							restTemplate.getMessageConverters().add(converter);
						}

						if (restTemplate.getInterceptors() == null) {
							restTemplate.setInterceptors(new ArrayList<ClientHttpRequestInterceptor>());
							for (Class<? extends ClientHttpRequestInterceptor> i : _interceptors) {
								ClientHttpRequestInterceptor interceptor = i.newInstance();
								restTemplate.getInterceptors().add(interceptor);
							}
						}

						currentRequestUrls.add(_url);// 用于fragment管理请求

						Class<?> responseType = method.getReturnType();

						if (method.getGenericReturnType() instanceof ParameterizedType) {
							ParameterizedType pdType = (ParameterizedType) method.getGenericReturnType();
							Class<?> rawType = (Class<?>) pdType.getRawType();
							Class<?> argument = (Class<?>) pdType.getActualTypeArguments()[0];

							String targetClassName = rawType.getName() + "_" + argument.getSimpleName();

							responseType = Class.forName(targetClassName);

						}

						final Loading loading = method.getAnnotation(Loading.class);
						if (loading != null) {

							handler.post(new Runnable() {
								@Override
								public void run() {
									ViewGroup root = null;
									if (getActivity() != null) {
										if (loading.container() == Window.ID_ANDROID_CONTENT) {
											root = (ViewGroup) getBackOpActivity().findViewById(loading.container());
											View view = getBackOpActivity().getLayoutInflater().inflate(loading.layout(), root, false);
											root.addView(view);
										} else {
											if (getView() != null) {
												root = (ViewGroup) getView().findViewById(loading.container());
												View view = getBackOpActivity().getLayoutInflater().inflate(loading.layout(), root, false);
												root.addView(view);
											}
										}
									}

								}
							});
						}

						// -----------------------For report ---------------------------------//
						int reportResult = 1;
						int reportResponseTime = 0;
						String reportErrorMessage = "";
						// ---------------------------------END---------------------------------------//

						HttpHeaders httpHeaders = new HttpHeaders();
						try {
							httpHeaders.setAccept(Collections.singletonList(MediaType.parseMediaType(_accept)));
							Response res = null;
							long startTime = System.currentTimeMillis();
							if (args != null && args.length > 0) {
								res = Response.class.cast(restTemplate.exchange(_url, _httpMethod,
										new HttpEntity<Object>(args[0], httpHeaders), responseType).getBody());
							} else {
								res = Response.class.cast(restTemplate.exchange(_url, _httpMethod,
										new HttpEntity<Object>(null, httpHeaders), responseType).getBody());
							}
							long endTime = System.currentTimeMillis();
							reportResponseTime = (int) (endTime - startTime);

							future.get();

							if (res.getErrorCode() != RestException.NO_ERROR)
								throw new RestException(res.getMessage(), res.getErrorCode());

							if (RestProxyFragment.this.isDetached()) {
								// In order to avoid Add errorCode ClientException.FRAGMENT_DETACHED everywhere ,change this errorCode to
								// ClientException.REQUEST_ABORTED,as we always handle them in the same way.
								throw new ClientException("Fragment is already detached !", ClientException.REQUEST_ABORTED);
							}

							return res;

						} catch (Exception ex) {
							if (ex instanceof ClientException) {
								if (((ClientException) ex).getErrorCode() == ClientException.REQUEST_NETWORK)
									reportResult = 2;
							} else if (ex instanceof ServerException) {
								reportResult = 3;
							}
							reportErrorMessage = ex.getMessage();
							throw ex;
						} finally {
							// -----------------------For report ---------------------------------//
							ReportRequest reportRequest = new ReportRequest();
							reportRequest.setRestUrl(_url.substring(Configuration.DEFAULT.getDomain().length()));
							reportRequest.setResult(reportResult);
							reportRequest.setResponseTime(reportResponseTime);
							reportRequest.setErrorMessage(reportErrorMessage);
							sendReport(restTemplate, httpHeaders, reportRequest);
							// ---------------------------------END---------------------------------------//

							currentRequestUrls.remove(_url);// 用于fragment管理请求
							if (loading != null) {
								handler.post(new Runnable() {
									@Override
									public void run() {
										ViewGroup root = null;
										if (loading.container() == Window.ID_ANDROID_CONTENT) {
											if (getActivity() != null) {
												root = (ViewGroup) getActivity().findViewById(loading.container());
												View view = root.findViewById(loading.value());
												root.removeView(view);
											}
										} else {
											if (getView() != null) {
												root = (ViewGroup) getView().findViewById(loading.container());
												View view = root.findViewById(loading.value());
												root.removeView(view);
											}
										}
									}
								});
							}
						}
					}

				}));
			}
		}
	}

	private void sendReport(final CustomRestTemplate restTemplate, final HttpHeaders reportHeaders, final ReportRequest reportRequest) {
		Random rand = new Random();
		int randomNum = rand.nextInt(10);
		if (randomNum == 1 && BackOpFragmentActivity.PACKAGE_NAME.equals(BackOpFragmentActivity.IW_PACKAGE_NAME)) {
			BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {
				@Override
				public void execute() {
					try {
						ReportResponse reportResponse = ReportResponse.class.cast(restTemplate.exchange(REPORT_URL, HttpMethod.POST,
								new HttpEntity<Object>(reportRequest, reportHeaders), ReportResponse.class).getBody());
						Log.d(TAG, "Send report: " + reportResponse.getErrorCode() + reportResponse.getMessage());
					} catch (Exception e) {
						Log.e(TAG, "Send report failed." + e.toString());
					}
				}
			});
		}
	}
}