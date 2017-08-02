package com.xrouter.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xrouter.BaseLoginFragment;
import com.xrouter.LoginInterceptorManager;

/**
 * Created by panda on 2017/7/25.
 */
public class LoginFragment extends BaseLoginFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button button = new Button(container.getContext());
        button.setText("loign");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginInterceptorManager.pop(true);
                getRoot().finish();
            }
        });
        return button;
    }
}
