package au.edu.unsw.infs3605.aboriginalplatform;

import android.app.Application;

import au.edu.unsw.infs3605.aboriginalplatform.utils.SPUtil;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SPUtil.getInstance().init(this);
    }
}
