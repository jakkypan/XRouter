package com.xrouter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.Pair;

import com.google.gson.Gson;
import com.demo.guard.AssetGuard;

/**
 * Created by panda on 2017/8/1.
 */
public class MappingManager {
    private Context context;
    private MappingBean mappingBean;

    public MappingManager(Context context) {
        this.context = context;
        getBean();
    }

    public final MappingBean getBean() {
        if (mappingBean == null) {
            mappingBean = read();
        }
        return mappingBean;
    }

    /**
     * 通用拦截的login页面
     *
     * @return
     */
    public final String loginActivity() {
        MappingBean bean = getBean();
        if (bean != null) {
            return bean.loginActivity;
        }
        return null;
    }

    /**
     * 获取当前版本号
     *
     * @return
     */
    public final int version() {
        MappingBean bean = getBean();
        if (bean != null) {
            return bean.version;
        }
        return 0;
    }

    /**
     * 获取schema
     *
     * @return
     */
    public final String schema() {
        MappingBean bean = getBean();
        if (bean != null) {
            return bean.schema;
        }
        return null;
    }

    /**
     * 跳转的合法性检查，同时返回匹配到的java bean对象
     *
     * @param url
     * @return
     */
    public final MappingItemBean matchRouter(Uri url) {
        if (!url.getScheme().equals(schema())) {
            return null;
        }

        String host = url.getHost();
        MappingBean mappingBean = getBean();
        for (MappingItemBean item : mappingBean.mappings) {
            if (item.host.equals(host)) {
                Bundle bundle = new Bundle();
                for (Pair<String, String> pair : UriParser.parseQuery(url)) {
                    bundle.putString(pair.first, pair.second);
                }
                item.bundle = bundle;
                return item;
            }
        }
        return null;
    }

    /**
     * 可重写，改变数据的来源
     *
     * @return
     */
    public MappingBean read() {
        String json = AssetGuard.deGuard(context.getAssets(), "mappings.json");
        return new Gson().fromJson(json, MappingBean.class);
    }
}
