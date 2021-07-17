package au.edu.unsw.infs3605.aboriginalplatform.listener;

import android.view.View;

public interface IOnViewListener<T> {

    /**
     * recyclerview的view 点击事件
     *
     * @param view
     * @param t
     * @param position
     */
    void onViewClick(View view, T t, int position);

    /**
     * recyclerview的view 长按事件
     *
     * @param view
     * @param t
     * @param position
     * @return
     */
    boolean onLongViewClick(View view, T t, int position);
}
