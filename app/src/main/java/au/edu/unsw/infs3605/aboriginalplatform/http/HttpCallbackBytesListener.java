package au.edu.unsw.infs3605.aboriginalplatform.http;

/**
 * HttpURLConnection网络请求返回监听器
 */
public interface HttpCallbackBytesListener {

    /**
     * 网络请求成功
     *
     * @param response
     */
    void onFinish(byte[] response);

    /**
     * 网络请求失败
     *
     * @param e
     */
    void onError(Exception e);
}
