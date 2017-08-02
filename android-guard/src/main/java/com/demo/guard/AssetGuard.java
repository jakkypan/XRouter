package com.demo.guard;

import android.content.res.AssetManager;

/**
 * 对于放在assets下的文件进行保护
 *
 * 采用 XOR 加密
 *
 * XOR 运算有一个很奇妙的特点：如果对一个值连续做两次 XOR，会返回这个值本身。
 *
 * Created by panda on 2017/7/28.
 */
public class AssetGuard {

    static {
        System.loadLibrary("guard-lib");
    }

    /**
     * 混淆
     *
     * @return
     */
    public static native String guard(AssetManager assetManager, String assetFileName);

    /**
     * 解混淆
     *
     * @return
     */
    public static native String deGuard(AssetManager assetManager, String assetFileName);
}
