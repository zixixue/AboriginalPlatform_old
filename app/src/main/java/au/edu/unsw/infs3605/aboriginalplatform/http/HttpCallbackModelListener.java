package au.edu.unsw.infs3605.aboriginalplatform.http;


/**
 * HttpURLConnection网络请求返回监听器
 */
public interface HttpCallbackModelListener<T> {
    /**
     * 网络请求成功
     *
     * @param response
     */
    void onFinish(T response);

    /**
     * 网络请求失败
     *
     * @param e
     */
    void onError(Exception e);
}
