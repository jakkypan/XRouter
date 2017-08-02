package com.xrouter;

import android.os.Bundle;

import java.util.LinkedList;

/**
 * 登录拦截的处理类，主要是完成登录后，怎么继续后续的流程
 *
 * Created by panda on 2017/8/2.
 */
public class LoginInterceptorManager {
    public static LinkedList<LoginFinishedCallback> callbacks;

    public static synchronized void push(LoginFinishedCallback callback) {
        if (callbacks == null) {
            callbacks = new LinkedList<>();
        }
        callbacks.push(callback);
    }

    public static synchronized void pop(boolean isSuccess) {
        pop(isSuccess, null);
    }

    public static synchronized void pop(boolean isSuccess, Bundle bundle) {
        if (callbacks != null && !callbacks.isEmpty()) {
            callbacks.pop().finished(isSuccess, bundle);
        }
    }


    /**
     * 通知完成
     */
    public interface LoginFinishedCallback {
        /**
         *
         * @param isSuccess 登录是否成功，不成功就不往下走了
         * @param bundle 登录成功后给出的额外的数据
         */
        public void finished(boolean isSuccess, Bundle bundle);
    }
}
