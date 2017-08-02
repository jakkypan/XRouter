package com.xrouter.demo;

import android.app.Application;
import android.net.Uri;

import com.xfragment.RootFragment;
import com.xrouter.IGlobalInterceptor;
import com.xrouter.XRouter;

/**
 * Created by panda on 2017/8/2.
 */
public class BaseApp extends Application implements IGlobalInterceptor {
    @Override
    public void onCreate() {
        super.onCreate();
        XRouter.init(this, this);
    }

    @Override
    public void interceptor(Uri uri, RootFragment from) {

    }
}
