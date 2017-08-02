package com.xrouter.demo;

import com.xfragment.RootActivity;
import com.xfragment.RootFragment;

/**
 * Created by panda on 2017/7/25.
 */
public class LoginActivity extends RootActivity {
    @Override
    public RootFragment rootFragment() {
        return new LoginFragment();
    }
}
