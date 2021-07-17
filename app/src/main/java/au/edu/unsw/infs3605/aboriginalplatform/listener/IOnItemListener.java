package au.edu.unsw.infs3605.aboriginalplatform.listener;

public interface IOnItemListener<T> {

    /**
     * recyclerview的条目点击事件
     *
     * @param t
     * @param position
     */
    void onItemClick(T t, int position);
}
