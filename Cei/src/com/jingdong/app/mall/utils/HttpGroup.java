package com.jingdong.app.mall.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Proxy;
import android.os.Bundle;

import com.jingdong.app.mall.utils.thread.PooledThread;
import java.io.*;
import java.lang.ref.SoftReference;
import java.net.*;
import java.util.*;

import org.json.*;

public abstract class HttpGroup {

	private static final HashMap alertDialogStateMap = new HashMap();
	private static final int attempts = Integer.parseInt("30");
	private static final int attemptsTime = Integer.parseInt("30");
	private static String charset = "UTF-8";
	private static final int connectTimeout = Integer.parseInt("30");
	private static String cookies;
	private static final String host = "8091";
	private static int httpIdCounter = 0;
	private static String mMd5Key;
	private static final int readTimeout = Integer.parseInt("30");
	private static int test = 0;
	private int completesCount;
	protected int httpCount;
	public HttpGroupSetting httpGroupSetting;
	private int imageCornerDp;
	private int maxProgress;
	private int maxStep;
	private boolean needImageCorner;
	private OnGroupEndListener onGroupEndListener;
	private OnGroupErrorListener onGroupErrorListener;
	private OnGroupProgressListener onGroupProgressListener;
	private OnGroupStartListener onGroupStartListener;
	private OnGroupStepListener onGroupStepListener;
	protected int priority;
	private int progress;
	private boolean reportUserInfoFlag;
	private int step;
	protected int type;
	private boolean useCaches;

	public static class HttpGroupSync extends HttpGroup {

		public void execute(HttpRequest httprequest) {
		}

		public HttpGroupSync(HttpGroupSetting httpgroupsetting) {
			super(httpgroupsetting);
		}
	}

	public static interface CompleteListener {

		public abstract void onComplete(Bundle bundle);
	}

	public static interface CustomOnAllListener extends OnAllListener {
	}

	static interface Handler {

		public abstract void run();
	}

	public static class HttpError {

		public static final int EXCEPTION = 0;
		public static final String EXCEPTION_MESSAGE_ATTESTATION_RSA = "attestation RSA";
		public static final String EXCEPTION_MESSAGE_ATTESTATION_WIFI = "attestation WIFI";
		public static final String EXCEPTION_MESSAGE_NO_CACHE = "no cache";
		public static final String EXCEPTION_MESSAGE_NO_READY = "no ready";
		public static final int JSON_CODE = 3;
		public static final int RESPONSE_CODE = 2;
		public static final int TIME_OUT = 1;
		private int errorCode;
		private Throwable exception;
		private HttpResponse httpResponse;
		private int jsonCode;
		private String message;
		private boolean noRetry;
		private int responseCode;
		private int times;

		public HttpError() {
		}

		public HttpError(Throwable throwable) {
			errorCode = 0;
			exception = throwable;
		}

		public int getErrorCode() {
			return errorCode;
		}

		public String getErrorCodeStr() {
			String s = "UNKNOWN";

			switch (errorCode) {
			case 0:
				s = "EXCEPTION";
				break;
			case 1:
				s = "TIME_OUT";
				break;
			case 2:
				s = "RESPONSE_CODE";
				break;
			case 3:
				s = "JSON_CODE";
				break;
			}
			return s;
		}

		public Throwable getException() {
			return exception;
		}

		public HttpResponse getHttpResponse() {
			return httpResponse;
		}

		public int getJsonCode() {
			return jsonCode;
		}

		public String getMessage() {
			return message;
		}

		public int getResponseCode() {
			return responseCode;
		}

		public int getTimes() {
			return times;
		}

		public boolean isNoRetry() {
			return noRetry;
		}

		public void setErrorCode(int i) {
			errorCode = i;
		}

		public void setException(Throwable throwable) {
			exception = throwable;
		}

		public void setHttpResponse(HttpResponse httpresponse) {
			httpResponse = httpresponse;
		}

		public void setJsonCode(int i) {
			jsonCode = i;
		}

		public void setMessage(String s) {
			message = s;
		}

		public void setNoRetry(boolean flag) {
			noRetry = flag;
		}

		public void setResponseCode(int i) {
			responseCode = i;
		}

		public void setTimes(int i) {
			times = i;
		}

		public String toString() {
			return (new StringBuilder("HttpError [errorCode="))
					.append(getErrorCodeStr()).append(", exception=")
					.append(exception).append(", jsonCode=").append(jsonCode)
					.append(", message=").append(message)
					.append(", responseCode=").append(responseCode)
					.append(", time=").append(times).append("]").toString();
		}

	}

	public static class HttpGroupSetting {

		public static final int PRIORITY_FILE = 500;
		public static final int PRIORITY_IMAGE = 5000;
		public static final int PRIORITY_JSON = 1000;
		public static final int TYPE_FILE = 500;
		public static final int TYPE_IMAGE = 5000;
		public static final int TYPE_JSON = 1000;
		private int imageCornerDp;
		private boolean needImageCorner;
		private int priority;
		private int type;

		public int getImageCornerDp() {
			return imageCornerDp;
		}

		public int getPriority() {
			return priority;
		}

		public int getType() {
			return type;
		}

		public boolean isNeedImageCorner() {
			return needImageCorner;
		}

		public void setImageCornerDp(int i) {
			imageCornerDp = i;
		}

		public void setNeedImageCorner(boolean flag) {
			needImageCorner = flag;
		}

		public void setPriority(int i) {
			priority = i;
		}

		public void setType(int i) {
			type = i;
			if (priority != 0) {
				return;
			} else {
				switch (i) {
				case PRIORITY_IMAGE:
					setPriority(PRIORITY_IMAGE);
					break;
				case PRIORITY_JSON:
					setPriority(PRIORITY_IMAGE);
					break;
				}
			}
		}

		public HttpGroupSetting() {
			needImageCorner = true;
			imageCornerDp = 6;
		}
	}

	public static class HttpGroupaAsynPool extends HttpGroup {

		@Override
		public void execute(final HttpRequest httpRequest) {
			Runnable runnable = new Runnable() {

				public void run() {
					httpRequest.noNeedConnectionHandler();
					if (httpRequest.isNeedConnection) {
						Runnable runnable1 = new Runnable() {

							public void run() {
								// if (httpCount < 1)
								onStart();
								// httpCount = 1 + httpCount;
								// httpRequest.needConnectionHandler();
							}
						};
						if (httpRequest.getHttpSetting().isTopPriority())
							(new Thread(runnable1)).start();
						else if (httpRequest.getHttpSetting().getPriority() == HttpGroupSetting.PRIORITY_JSON)
							PooledThread.getSecondThreadPool().offerTask(
									runnable1,
									httpRequest.getHttpSetting().getPriority());
						else
							PooledThread.getThirdThreadPool().offerTask(
									runnable1,
									httpRequest.getHttpSetting().getPriority());
					}
				}
			};
			if (httpRequest.getHttpSetting().isTopPriority())
				(new Thread(runnable)).start();
			else
				PooledThread.getFirstThreadPool().offerTask(runnable,
						httpRequest.getHttpSetting().getPriority());
		}

		public HttpGroupaAsynPool(HttpGroupSetting httpgroupsetting) {
			super(httpgroupsetting);
		}
	}

	public class HttpRequest implements StopController {

		protected static final int MODULE_STATE_DISABLE = 0;
		protected static final int MODULE_STATE_ENCRYPT = 3;
		private Handler cacheHandler;
		protected HttpURLConnection conn;
		private Handler connectionHandler;
		protected boolean connectionRetry;
		private Handler connectionThreadPoolsHandler;
		private Handler contentHandler;
		private CompleteListener continueListener;
		private int currentHandlerIndex;
		protected ArrayList errorList;
		private Handler firstHandler;
		private ArrayList handlers;
		protected HttpResponse httpResponse;
		protected HttpSetting httpSetting;
		protected InputStream inputStream;
		private IOUtil.ProgressListener ioProgressListener;
		public boolean isNeedConnection = true;
		protected boolean manualRetry;
		private Handler paramHandler;
		private boolean stopFlag;
		private Handler testHandler;
		private String thirdHost;

		private android.graphics.BitmapFactory.Options getBitmapOpt() {
			android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
			options.inPurgeable = true;
			options.inInputShareable = true;
			if (httpResponse.getLength() > 0x10000L
					&& httpSetting.isSampleSize()) {
				options.inSampleSize = 2;
			}
			return options;
		}

		public HttpSetting getHttpSetting() {
			return httpSetting;
		}

		public boolean isStop() {
			return stopFlag;
		}

		public void needConnectionHandler() {
			if (isNeedConnection) {
				handlers.clear();
				handlers.add(connectionThreadPoolsHandler);
				handlers.add(connectionHandler);
				handlers.add(contentHandler);
				nextHandler();
			}
		}

		public void stop() {
			stopFlag = true;
		}

		/**
		 * 请求结束后回调该方法
		 */
		private void callBack() {
			if (!isNeedConnection) {
				addCompletesCount();
				addStep(1);
				httpSetting.onEnd(httpResponse);
			}
		}

		private void nextHandler() {
			int i = currentHandlerIndex;
			currentHandlerIndex = 1 + currentHandlerIndex;
			if (i < handlers.size()) {
				((Handler) handlers.get(i)).run();
				currentHandlerIndex = i;
			}
		}

		/**
		 * 拼接url与参数
		 */
		private void urlParam() {

		}

		/**
		 * 读取文件内容
		 * 
		 * @throws Exception
		 */
		private void fileContent() throws Exception {
			FileGuider fileguider = httpSetting.getSavePath();
			fileguider.setAvailableSize(httpResponse.getLength());
			java.io.FileOutputStream fileoutputstream;
			fileoutputstream = new FileOutputStream(fileguider.getFileName());
			IOUtil.readAsFile(httpResponse.getInputStream(), fileoutputstream,
					ioProgressListener, this);
			File file = new File(fileguider.getFileName());
			if (isStop())
				file.delete();
			httpResponse.setSaveFile(file);
		}

		/**
		 * 读取图片内容
		 * 
		 * @throws Exception
		 */
		private void imageContent() throws Exception {
			httpResponse.setInputData(IOUtil.readAsBytes(
					httpResponse.getInputStream(), ioProgressListener));
		}

		private void jsonContent() throws Exception {
			httpResponse.setJsonObject(new JSONObject(IOUtil.readAsString(
					httpResponse.getInputStream(), HttpGroup.charset,
					ioProgressListener)));
		}

		public void noNeedConnectionHandler() {
			handlers.add(paramHandler);
			handlers.add(firstHandler);
			handlers.add(testHandler);
			handlers.add(cacheHandler);
			nextHandler();
		}

		/**
		 * 判断能否读取缓存数据
		 */
		private void doNetAndCache() {
			if (3 != httpSetting.getCacheMode() || !readAssetsCache()) {
				isNeedConnection = true;
				httpSetting.onStart();
			}
		}

		private boolean readAssetsCache() {
			boolean flag;
			flag = true;
			httpResponse = new HttpResponse();
			httpSetting.getType();
			httpResponse.setInputStream(null);
			return flag;
		}

		public HttpRequest(final HttpSetting httpsetting) {
			currentHandlerIndex = 0;
			handlers = new ArrayList();
			paramHandler = new Handler() {

				public void run() {
					if (httpSetting.getFunctionId() != null) {
						String s = httpSetting.getFunctionId();
						httpSetting.putMapParams("functionId", s);
						String s1 = httpSetting.getJsonParams().toString();
						httpSetting.putMapParams("body", s1);
					}
				}
			};

			paramHandler = new Handler() {

				public void run() {
					if (httpSetting.getFunctionId() != null) {
						String s = httpSetting.getFunctionId();
						httpSetting.putMapParams("functionId", s);
						String s1 = httpSetting.getJsonParams().toString();
						httpSetting.putMapParams("body", s1);
					}
					nextHandler();
				}

			};

			firstHandler = new Handler() {

				public void run() {
					if (httpSetting.getUrl() == null)
						httpSetting.setUrl((new StringBuilder("http://"))
								.append(httpSetting.getHost())
								.append("/client.action").toString());
					if (httpSetting.getAttempts() == 0)
						httpSetting.setAttempts(HttpGroup.attempts);
					if (httpSetting.getConnectTimeout() == 0)
						httpSetting.setConnectTimeout(HttpGroup.connectTimeout);
					if (httpSetting.getReadTimeout() == 0)
						httpSetting.setReadTimeout(HttpGroup.readTimeout);
					if (httpSetting.getType() == 5000
							|| httpSetting.getType() == 500)
						httpSetting.setPost(false);
					if (httpSetting.getType() == 5000) {
						httpSetting.setLocalFileCache(true);
						httpSetting.setLocalFileCacheTime(0xf731400L);
					}
					if (httpSetting.getType() == 5000)
						httpSetting.setNeedGlobalInitialization(false);
					if (imageCornerDp == 0)
						imageCornerDp = 6;
					addMaxStep(1);
					urlParam();
					nextHandler();
					callBack();
				}

			};
			testHandler = new Handler() {

				@Override
				public void run() {
					nextHandler();
				}
			};
			cacheHandler = new Handler() {

				public void run() {
					// 读取缓存的数据
					doNetAndCache();
				}

			};

			connectionThreadPoolsHandler = new Handler() {

				public void run() {
					isNeedConnection = false;
					nextHandler();
					saveCache();
					callBack();
				}

			};

			connectionHandler = new Handler() {

				public void run() {
					String s = httpSetting.getFinalUrl();
					if (s == null)
						s = httpSetting.getUrl();
					URL url;
					try {
						url = new URL(s);
						conn = (HttpURLConnection) url.openConnection();
					} catch (Exception e) {
						e.printStackTrace();
					}
					conn.setConnectTimeout(httpSetting.getConnectTimeout());
					conn.setReadTimeout(httpSetting.getReadTimeout());
					conn.setUseCaches(useCaches);
					conn.setRequestProperty("Charset", HttpGroup.charset);
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
					try {
						httpResponse.setInputStream(conn.getInputStream());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			};
			/**
			 * 根据输入流获取内容
			 */
			contentHandler = new Handler() {

				public void run() {
					try {
						switch (httpsetting.getType()) {
						case 500:
							fileContent();
							break;
						case 1000:
							imageContent();
							break;
						case 2000:
							jsonContent();
							break;
						}
					} catch (Exception e) {

					}
				}
			};

			ioProgressListener = new IOUtil.ProgressListener() {

				public void notify(int i, int j) {
					addProgress(i);
					httpSetting.onProgress(
							Long.valueOf(httpResponse.getLength()).intValue(),
							j);
				}
			};

			continueListener = new CompleteListener() {

				public void onComplete(Bundle bundle) {
					notify();
					return;
				}
			};
			httpSetting = httpsetting;

		}

		/**
		 * 缓存数据
		 */
		protected void saveCache() {

		}
	}

	public class HttpResponse {

		private Bitmap bitmap;
		private int code;
		private Drawable drawable;
		private Map headerFields;
		private HttpURLConnection httpURLConnection;
		private byte inputData[];
		private InputStream inputStream;
		private long length;
		private File saveFile;
		private String shareImagePath;
		private SoftReference softReferenceBitmap;
		private SoftReference softReferenceDrawable;
		private SoftReference softReferenceInputData;
		private String string;
		private String type;
		private JSONObject jsonObject;

		public JSONObject getJsonObject() {
			return jsonObject;
		}

		public void setJsonObject(JSONObject jsonObject) {
			this.jsonObject = jsonObject;
		}

		private void imageClean() {
			softReferenceInputData = new SoftReference(inputData);
			softReferenceBitmap = new SoftReference(bitmap);
			softReferenceDrawable = new SoftReference(drawable);
			inputData = null;
			bitmap = null;
			drawable = null;
		}

		public void clean() {
			httpURLConnection = null;
		}

		public int getCode() {
			return code;
		}

		public Map getHeaderFields() {
			return headerFields;
		}

		public byte[] getInputData() {
			return inputData;
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public long getLength() {
			return length;
		}

		public File getSaveFile() {
			return saveFile;
		}

		public String getShareImagePath() {
			return shareImagePath;
		}

		public String getString() {
			return string;
		}

		public String getType() {
			return type;
		}

		public void setBitmap(Bitmap bitmap1) {
			if (bitmap1 == null) {
				throw new RuntimeException("bitmap is null");
			} else {
				bitmap = bitmap1;
				return;
			}
		}

		public void setCode(int i) {
			code = i;
		}

		public void setDrawable(Drawable drawable1) {
			drawable = drawable1;
		}

		public void setHeaderFields(Map map) {
			headerFields = map;
		}

		public void setInputData(byte abyte0[]) {
			inputData = abyte0;
		}

		public void setInputStream(InputStream inputstream) {
			inputStream = inputstream;
		}

		public void setLength(long l) {
			length = l;
		}

		public void setSaveFile(File file) {
			saveFile = file;
		}

		public void setShareImagePath(String s) {
			shareImagePath = s;
		}

		public void setString(String s) {
			string = s;
		}

		public void setType(String s) {
			type = s;
		}

		public HttpResponse() {
		}

		public HttpResponse(Drawable drawable1) {
		}

		public HttpResponse(HttpURLConnection httpurlconnection) {
			httpURLConnection = httpurlconnection;
		}

		public Bitmap getBitmap() {
			return null;
		}

		public BitmapDrawable getDrawable() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static class HttpSetting implements HttpSettingParams {

		public static final int CACHE_MODE_AUTO = 0;
		public static final int CACHE_MODE_ONLY_CACHE = 1;
		public static final int CACHE_MODE_ONLY_NET = 2;
		public static final int EFFECT_DEFAULT = 1;
		public static final int EFFECT_NO = 0;
		public static final int EFFECT_STATE_NO = 0;
		public static final int EFFECT_STATE_YES = 1;
		private int attempts;
		private int cacheMode;
		private int connectTimeout;
		private int effect;
		private int effectState;
		private String finalUrl;
		private String functionId;
		private String host;
		private int id;
		private int imageCornerDp;
		private int imageViewHeight;
		private int imageViewWidth;
		private boolean isReady;
		private boolean isSampleSize;
		private boolean isThisFunctionMustBeExcute;
		private boolean isTopPriority;
		private JSONObject jsonParams;
		private boolean localFileCache;
		private long localFileCacheTime;
		private boolean localMemoryCache;
		private boolean mNeedAgainEncrypt;
		private Map mapParams;
		private String md5;
		private boolean needGlobalInitialization;
		private boolean needImageCorner;
		private boolean needShareImage;
		private boolean notifyUser;
		private boolean notifyUserWithExit;
		private OnEndListener onEndListener;
		private OnErrorListener onErrorListener;
		private OnProgressListener onProgressListener;
		private OnReadyListener onReadyListener;
		private OnStartListener onStartListener;
		private boolean post;
		private int priority;
		private int readTimeout;
		private int type;
		private String url;
		private boolean useLocalCookies;
		private FileGuider savePath;

		public FileGuider getSavePath() {
			return savePath;
		}

		public void setSavePath(FileGuider savePath) {
			this.savePath = savePath;
		}

		public void appendOneAttempts() {
			attempts = 1 + attempts;
		}

		public int getAttempts() {
			return attempts;
		}

		public int getCacheMode() {
			return cacheMode;
		}

		public int getConnectTimeout() {
			return connectTimeout;
		}

		public int getEffect() {
			return effect;
		}

		public int getEffectState() {
			return effectState;
		}

		public String getFinalUrl() {
			return finalUrl;
		}

		public String getFunctionId() {
			return functionId;
		}

		public String getHost() {
			return host;
		}

		public int getId() {
			return id;
		}

		public int getImageCornerDp() {
			return imageCornerDp;
		}

		public int getImageViewHeight() {
			return imageViewHeight;
		}

		public int getImageViewWidth() {
			return imageViewWidth;
		}

		public JSONObject getJsonParams() {
			if (jsonParams == null)
				jsonParams = new JSONObject();
			return jsonParams;
		}

		public long getLocalFileCacheTime() {
			return localFileCacheTime;
		}

		public Map getMapParams() {
			return mapParams;
		}

		public boolean getNeedAgainEncrypt() {
			return mNeedAgainEncrypt;
		}

		public OnEndListener getOnEndListener() {
			return onEndListener;
		}

		public OnErrorListener getOnErrorListener() {
			return onErrorListener;
		}

		public OnProgressListener getOnProgressListener() {
			return onProgressListener;
		}

		public OnReadyListener getOnReadyListener() {
			return onReadyListener;
		}

		public OnStartListener getOnStartListener() {
			return onStartListener;
		}

		public int getPriority() {
			return priority;
		}

		public int getReadTimeout() {
			return readTimeout;
		}

		public int getType() {
			return type;
		}

		public String getUrl() {
			return url;
		}

		public boolean isLocalFileCache() {
			return localFileCache;
		}

		public boolean isLocalMemoryCache() {
			return localMemoryCache;
		}

		public boolean isNeedGlobalInitialization() {
			return needGlobalInitialization;
		}

		public boolean isNeedImageCorner() {
			return needImageCorner;
		}

		public boolean isNeedShareImage() {
			return needShareImage;
		}

		public boolean isNotifyUser() {
			return notifyUser;
		}

		public boolean isNotifyUserWithExit() {
			return notifyUserWithExit;
		}

		public boolean isPost() {
			return post;
		}

		public boolean isReady() {
			return isReady;
		}

		public boolean isSampleSize() {
			return isSampleSize;
		}

		public boolean isThisFunctionMustBeExcute() {
			return isThisFunctionMustBeExcute;
		}

		public boolean isTopPriority() {
			return isTopPriority;
		}

		public boolean isUseLocalCookies() {
			return useLocalCookies;
		}

		public void onEnd(HttpResponse httpresponse) {
			if (onEndListener != null)
				onEndListener.onEnd(httpresponse);
		}

		public void onError(HttpError httperror) {
			if (onErrorListener != null) {
				onErrorListener.onError(httperror);
			}
		}

		public void onProgress(int i, int j) {
			if (onProgressListener != null)
				onProgressListener.onProgress(i, j);
		}

		public void onStart() {
			if (onStartListener != null)
				onStartListener.onStart();
		}

		public void setAttempts(int i) {
			attempts = i;
		}

		public void setCacheMode(int i) {
			cacheMode = i;
		}

		public void setConnectTimeout(int i) {
			connectTimeout = i;
		}

		public void setEffect(int i) {
			effect = i;
		}

		public void setEffectState(int i) {
			effectState = i;
		}

		public void setFinalUrl(String s) {
			finalUrl = s;
		}

		public void setFunctionId(String s) {
			functionId = s;
		}

		public void setHost(String s) {
			host = s;
		}

		public void setId(int i) {
			id = i;
		}

		public void setImageCornerDp(int i) {
			imageCornerDp = i;
		}

		public void setImageViewHeight(int i) {
			imageViewHeight = i;
		}

		public void setImageViewWidth(int i) {
			imageViewWidth = i;
		}

		public void setJsonParams(JSONObject jsonobject) {
			if (jsonobject != null)
				try {
					jsonParams = new JSONObject(jsonobject.toString());
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
		}

		public void setListener(HttpTaskListener httptasklistener) {
			if (httptasklistener instanceof CustomOnAllListener)
				setEffect(0);
			if (httptasklistener instanceof OnErrorListener)
				onErrorListener = (OnErrorListener) httptasklistener;
			if (httptasklistener instanceof OnStartListener)
				onStartListener = (OnStartListener) httptasklistener;
			if (httptasklistener instanceof OnProgressListener)
				onProgressListener = (OnProgressListener) httptasklistener;
			if (httptasklistener instanceof OnEndListener)
				onEndListener = (OnEndListener) httptasklistener;
			if (httptasklistener instanceof OnReadyListener)
				onReadyListener = (OnReadyListener) httptasklistener;
		}

		public void setLocalFileCache(boolean flag) {
			localFileCache = flag;
		}

		public void setLocalFileCacheTime(long l) {
			localFileCacheTime = l;
		}

		public void setLocalMemoryCache(boolean flag) {
			localMemoryCache = flag;
		}

		public void setMapParams(Map map) {
			if (map != null) {
				Iterator iterator = map.keySet().iterator();
				while (iterator.hasNext()) {
					String s = (String) iterator.next();
					putMapParams(s, (String) map.get(s));
				}
			}
		}

		public void setMd5(String s) {
			md5 = s;
		}

		public void setNeedEncrypt(boolean flag) {
			mNeedAgainEncrypt = flag;
		}

		public void setNeedGlobalInitialization(boolean flag) {
			needGlobalInitialization = flag;
		}

		public void setNeedImageCorner(boolean flag) {
			needImageCorner = flag;
		}

		public void setNeedShareImage(boolean flag) {
			needShareImage = flag;
		}

		public void setNotifyUser(boolean flag) {
			notifyUser = flag;
		}

		public void setNotifyUserWithExit(boolean flag) {
			notifyUserWithExit = flag;
		}

		public void setPost(boolean flag) {
			post = flag;
		}

		public void setPriority(int i) {
			priority = i;
		}

		public void setReadTimeout(int i) {
			readTimeout = i;
		}

		public void setReady(boolean flag) {
			isReady = flag;
		}

		public void setSampleSize(boolean flag) {
			isSampleSize = flag;
		}

		public void setThisFunctionMustBeExcute(boolean flag) {
			isThisFunctionMustBeExcute = flag;
		}

		public void setTopPriority(boolean flag) {
			isTopPriority = flag;
		}

		public void setType(int i) {
			type = i;
		}

		public void setUrl(String s) {
			url = s;
		}

		public void setUseLocalCookies(boolean flag) {
			useLocalCookies = flag;
		}

		public HttpSetting() {
			notifyUser = false;
			useLocalCookies = false;
			notifyUserWithExit = false;
			localMemoryCache = false;
			localFileCache = false;
			localFileCacheTime = -1L;
			needGlobalInitialization = true;
			effect = 1;
			effectState = 0;
			cacheMode = 0;
			imageViewWidth = -1;
			imageViewHeight = -1;
			needImageCorner = true;
			imageCornerDp = 6;
			isThisFunctionMustBeExcute = false;
			mNeedAgainEncrypt = false;
			isTopPriority = false;
			isReady = true;
			isSampleSize = true;
		}

		@Override
		public void putJsonParam(String s, Object obj) {

		}

		@Override
		public void putMapParams(String s, String s1) {

		}
	}

	public static interface HttpSettingParams {

		public abstract void putJsonParam(String s, Object obj);

		public abstract void putMapParams(String s, String s1);

		public abstract void setReady(boolean flag);
	}

	public static interface HttpTaskListener {
	}

	public static interface OnAllListener extends OnStartListener,
			OnEndListener, OnErrorListener, OnProgressListener {
	}

	public static interface OnCommonListener extends OnEndListener,
			OnErrorListener, OnReadyListener {
	}

	public static interface OnEndListener extends HttpTaskListener {

		public abstract void onEnd(HttpResponse httpresponse);
	}

	public static interface OnErrorListener extends HttpTaskListener {

		public abstract void onError(HttpError httperror);
	}

	public static interface OnGroupEndListener {

		public abstract void onEnd();
	}

	public static interface OnGroupErrorListener {

		public abstract void onError();
	}

	public static interface OnGroupProgressListener {

		public abstract void onProgress(int i, int j);
	}

	public static interface OnGroupStartListener {

		public abstract void onStart();
	}

	public static interface OnGroupStepListener {

		public abstract void onStep(int i, int j);
	}

	public static interface OnProgressListener extends HttpTaskListener {

		public abstract void onProgress(int i, int j);
	}

	public static interface OnReadyListener extends HttpTaskListener {

		public abstract void onReady(HttpSettingParams httpsettingparams);
	}

	public static interface OnStartListener extends HttpTaskListener {

		public abstract void onStart();
	}

	public static interface StopController {

		public abstract boolean isStop();

		public abstract void stop();
	}

	public HttpGroup(HttpGroupSetting httpgroupsetting) {
		useCaches = false;
		httpCount = 0;
		reportUserInfoFlag = true;
		needImageCorner = true;
		imageCornerDp = 0;
		completesCount = 0;
		maxProgress = 0;
		progress = 0;
		maxStep = 0;
		step = 0;
		httpGroupSetting = httpgroupsetting;
		priority = httpgroupsetting.getPriority();
		type = httpgroupsetting.getType();
		needImageCorner = httpgroupsetting.isNeedImageCorner();
		imageCornerDp = httpgroupsetting.getImageCornerDp();
	}

	public static void cleanCookies() {
		cookies = null;
	}

	private void onProgress(int i, int j) {
		if (onGroupProgressListener != null)
			onGroupProgressListener.onProgress(i, j);
	}

	private void onStep(int i, int j) {
		if (onGroupStepListener != null)
			onGroupStepListener.onStep(i, j);
	}

	public static void queryMd5Key(CompleteListener completelistener) {
		HttpGroupSetting httpgroupsetting = new HttpGroupSetting();
		httpgroupsetting.setPriority(1000);
		httpgroupsetting.setType(1000);
		queryMd5Key(((HttpGroup) (new HttpGroupaAsynPool(httpgroupsetting))),
				completelistener);
	}

	public static void queryMd5Key(HttpGroup httpgroup,
			final CompleteListener listener) {
		OnAllListener onalllistener = new OnAllListener() {

			public void onEnd(HttpResponse httpresponse) {

			}

			public void onError(HttpError httperror) {
				if (listener != null)
					listener.onComplete(null);
			}

			public void onProgress(int i, int j) {
			}

			public void onStart() {
			}

		};
		HttpSetting httpsetting = new HttpSetting();
		httpsetting.setFunctionId("key");
		httpsetting.setJsonParams(new JSONObject());
		httpsetting.setListener(onalllistener);
		httpsetting.setPost(true);
		httpgroup.add(httpsetting);
	}

	public static void setCookies(String s) {
		cookies = s;
	}

	public static void setMd5Key(String s) {
		mMd5Key = s;
	}

	public HttpRequest add(final HttpSetting httpSetting) {
		httpIdCounter = 1 + httpIdCounter;
		httpSetting.setId(httpIdCounter);
		final HttpRequest httpRequest = new HttpRequest(httpSetting);
		final OnReadyListener onReadyListener = httpSetting
				.getOnReadyListener();
		if (onReadyListener != null)
			new Thread() {
				public void run() {
					onReadyListener.onReady(httpSetting);
					if (httpSetting.isReady()) {
						add2(httpRequest);
					} else {
						HttpError httperror = new HttpError(new Exception(
								"no ready"));
						httpSetting.onError(httperror);
					}
				}
			}.start();
		else
			add2(httpRequest);
		return httpRequest;
	}

	public HttpRequest add(String s, Map map, OnAllListener onalllistener) {
		HttpSetting httpsetting = new HttpSetting();
		httpsetting.setUrl(s);
		httpsetting.setMapParams(map);
		httpsetting.setListener(onalllistener);
		return add(httpsetting);
	}

	public HttpRequest add(String s, JSONObject jsonobject,
			OnAllListener onalllistener) {
		HttpSetting httpsetting = new HttpSetting();
		httpsetting.setFunctionId(s);
		httpsetting.setJsonParams(jsonobject);
		httpsetting.setListener(onalllistener);
		return add(httpsetting);
	}

	public void add2(HttpRequest httprequest) {
		HttpSetting httpsetting;
		httpsetting = httprequest.getHttpSetting();
		if (httpsetting.getHost() == null)
			httpsetting.setHost(host);
		if (httpsetting.getType() == 0)
			httpsetting.setType(type);
		if (httpsetting.getPriority() == 0)
			httpsetting.setPriority(priority);
		execute(httprequest);
	}

	protected void addCompletesCount() {
		completesCount = 1 + completesCount;
		if (completesCount == httpCount)
			onEnd();
	}

	protected void addMaxProgress(int i) {
		maxProgress = i + maxProgress;
		onProgress(maxProgress, progress);
	}

	protected void addMaxStep(int i) {
		maxStep = i + maxStep;
		onStep(maxStep, step);
	}

	protected void addProgress(int i) {
		progress = i + progress;
		onProgress(maxProgress, progress);
	}

	protected void addStep(int i) {
		step = i + step;
		onStep(maxStep, step);
	}

	public abstract void execute(HttpRequest httprequest);

	public void onDestroy() {
	}

	protected void onEnd() {
		if (onGroupEndListener != null)
			onGroupEndListener.onEnd();
	}

	protected void onError() {
		if (onGroupErrorListener != null)
			onGroupErrorListener.onError();
	}

	protected void onStart() {
		if (onGroupStartListener != null)
			onGroupStartListener.onStart();
	}

	public void setOnGroupEndListener(OnGroupEndListener ongroupendlistener) {
		onGroupEndListener = ongroupendlistener;
	}

	public void setOnGroupErrorListener(
			OnGroupErrorListener ongrouperrorlistener) {
		onGroupErrorListener = ongrouperrorlistener;
	}

	public void setOnGroupProgressListener(
			OnGroupProgressListener ongroupprogresslistener) {
		onGroupProgressListener = ongroupprogresslistener;
	}

	public void setOnGroupStartListener(
			OnGroupStartListener ongroupstartlistener) {
		onGroupStartListener = ongroupstartlistener;
	}

	public void setOnGroupStepListener(OnGroupStepListener ongroupsteplistener) {
		onGroupStepListener = ongroupsteplistener;
	}

}