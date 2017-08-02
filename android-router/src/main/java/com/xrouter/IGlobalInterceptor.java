package com.xrouter;

import android.net.Uri;

import com.xfragment.RootFragment;

/**
 * 更加广义的拦截，在发起跳转前的对所有的跳转进行拦截
 *
 * Created by panda on 2017/8/2.
 */
public interface IGlobalInterceptor {
    /**
     *
     * @param uri
     * @param from
     */
    public void interceptor(Uri uri, RootFragment from);
}
