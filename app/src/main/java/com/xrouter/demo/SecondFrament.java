package com.xrouter.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xfragment.FragmentAnimBean;
import com.xfragment.RootFragment;
import com.xfragment.StackModeManager;
import com.xrouter.XRouter;

/**
 * Created by panda on 2017/7/24.
 */
public class SecondFrament extends RootFragment {
    Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getBundle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(container.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(container.getContext());
        textView.setText("bundle data: 【" + bundle.getString("test")+ "】");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        layout.addView(textView);

        Button button = new Button(container.getContext());
        button.setText("BACK");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResult(0, null);
                popStack();
            }
        });
        layout.addView(button);

        Button button2 = new Button(container.getContext());
        button2.setText("go activity");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAnimBean animBean = new FragmentAnimBean();
                animBean.enter = R.anim.in;
                animBean.exit = R.anim.out;
                animBean.popEnter = R.anim.fadein;
                animBean.popExit = R.anim.fadeout;

                new XRouter.XRouterBuilder()
                        .from(SecondFrament.this)
                        .hide()
                        .to("xrouter://sub?test=panhongchao222")
                        .animations(animBean)
                        .mode(StackModeManager.STANDARD)
                        .startForResult(100);
            }
        });
        layout.addView(button2);

        Button button3 = new Button(container.getContext());
        button3.setText("go activityFragment");
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAnimBean animBean = new FragmentAnimBean();
                animBean.enter = R.anim.in;
                animBean.exit = R.anim.out;
                animBean.popEnter = R.anim.fadein;
                animBean.popExit = R.anim.fadeout;

                new XRouter.XRouterBuilder()
                        .from(SecondFrament.this)
                        .hide()
                        .to("xrouter://sub2?test=panhongchao222")
                        .animations(animBean)
                        .mode(StackModeManager.STANDARD)
                        .startForResult(100);
            }
        });
        layout.addView(button3);

        Button button4 = new Button(container.getContext());
        button4.setText("go webview");
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAnimBean animBean = new FragmentAnimBean();
                animBean.enter = R.anim.in;
                animBean.exit = R.anim.out;
                animBean.popEnter = R.anim.fadein;
                animBean.popExit = R.anim.fadeout;

                new XRouter.XRouterBuilder()
                        .from(SecondFrament.this)
                        .hide()
                        .to("xrouter://webview")
                        .animations(animBean)
                        .mode(StackModeManager.STANDARD)
                        .start();
            }
        });
        layout.addView(button4);

        return layout;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent intent) {
        super.onFragmentResult(requestCode, resultCode, intent);
        Toast.makeText(getActivity(), "back from third", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), "from activity i goto", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewIntent(Bundle bundle) {
        super.onNewIntent(bundle);
        Log.e("111", "bundle: "+ bundle.toString());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCoverStop() {
        super.onCoverStop();
    }

    @Override
    public void onReShowResume() {
        super.onReShowResume();
    }
}
