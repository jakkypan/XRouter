package com.xrouter.demo;

import android.content.Intent;
import android.widget.Toast;

import com.xfragment.RootActivity;
import com.xfragment.RootFragment;

/**
 * Created by panda on 2017/7/25.
 */
public class SubActivity extends RootActivity {
    @Override
    public RootFragment rootFragment() {
        return new SubFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "ok!!!!", Toast.LENGTH_SHORT).show();
    }
}
