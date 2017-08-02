package com.xrouter.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xfragment.FragmentAnimBean;
import com.xfragment.RootFragment;
import com.xfragment.StackModeManager;
import com.xrouter.XRouter;

/**
 * Created by panda on 2017/7/24.
 */
public class MainFragment extends RootFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button textView = new Button(container.getContext());
        textView.setText("Click me");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("test", "test111");

                FragmentAnimBean animBean = new FragmentAnimBean();
                animBean.enter = R.anim.in;
                animBean.exit = R.anim.out;
                animBean.popEnter = R.anim.fadein;
                animBean.popExit = R.anim.fadeout;
//                addFragmentForResult(MainFragment.this, new SecondFrament(), bundle, animBean, 100);

                new XRouter.XRouterBuilder()
                        .from(MainFragment.this)
                        .hide()
                        .to("xrouter://second?test=panhongchao222")
                        .animations(animBean)
                        .mode(StackModeManager.STANDARD)
                        .startForResult(100);
            }
        });
        return textView;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent intent) {
        super.onFragmentResult(requestCode, resultCode, intent);
        Toast.makeText(getActivity(), "back from second", Toast.LENGTH_SHORT).show();
    }
}
