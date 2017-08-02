package com.xrouter;

import android.content.Intent;

import com.xfragment.RootFragment;

/**
 * 登录的fragment入口界面必须继承这个
 *
 * Created by panda on 2017/8/2.
 */
public abstract class BaseLoginFragment extends RootFragment {
    public static final int REQUEST_CODE = 10000;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
