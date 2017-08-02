package com.xrouter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.xfragment.FragmentAnimBean;
import com.xfragment.RootActivity;
import com.xfragment.RootFragment;
import com.xfragment.StackModeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panda on 2017/8/1.
 */
public class XRouter {
    private static MappingManager mappingManager;
    private static List<IGlobalInterceptor> interceptors;

    public static void init(Context context) {
        init(context, null);
    }

    /**
     * 初始化操作
     */
    public static void init(Context context, IGlobalInterceptor interceptor) {
        if (mappingManager == null) {
            mappingManager = new MappingManager(context);
            if (interceptor != null) {
                interceptors = new ArrayList<>();
                interceptors.add(interceptor);
            }
        }
    }

    /**
     * 添加新的拦截
     *
     * @param interceptor
     */
    public static void addGlobalInterceptor(IGlobalInterceptor interceptor) {
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
    }

    /**
     * 构造器
     */
    public static class XRouterBuilder {
//        private RootActivity fromActivity;
        /**
         * Xfragments的架构只会从fragment发起跳转
         */
        private RootFragment fromFragment;
        private Uri uri;
        private FragmentAnimBean animBean;
        private boolean isHideFromView = false;
        private @StackModeManager.StackMode int stackMode;
        private List<Integer> intentFlags;

//        /**
//         * 跳转是从哪里来的
//         *
//         * @param from 来源，只能是fragment或activity
//         * @return
//         */
//        public XRouterBuilder from(RootActivity from) {
//            this.fromActivity = from;
//            return this;
//        }

        /**
         * 栈模式，只对跳转到fragment有效果
         *
         * @param mode
         * @return
         */
        @XFragment
        public XRouterBuilder mode(@StackModeManager.StackMode int mode) {
            this.stackMode = mode;
            return this;
        }

        public XRouterBuilder from(RootFragment from) {
            this.fromFragment = from;
            return this;
        }

        /**
         * 是否隐藏来源的界面，设置了这个就无法获得result
         *
         * @return
         */
        public XRouterBuilder hide() {
            isHideFromView = true;
            return this;
        }

        /**
         * activity跳转的intent flag设置，可重复设置
         *
         * @param flag
         * @return
         */
        @XActivity
        public XRouterBuilder flag(int flag) {
            if (intentFlags == null) {
                intentFlags = new ArrayList<>();
            }
            intentFlags.add(flag);
            return this;
        }

        /**
         * 跳转到的地址
         *
         * @param url
         * @return
         */
        public XRouterBuilder to(String url) {
            return to(Uri.parse(url));
        }

        /**
         * 跳转到的地址
         *
         * @param url
         * @return
         */
        public XRouterBuilder to(Uri url) {
            this.uri = url;
            return this;
        }

        /**
         * 跳转动画
         *
         * @param animBean
         * @return
         */
        public XRouterBuilder animations(FragmentAnimBean animBean) {
            this.animBean = animBean;
            return this;
        }

        /**
         * 不获取返回值的跳转
         */
        public void start() {
            startForResult(0);
        }

        /**
         * 需要返回值得跳转
         *
         * @param requestCode
         */
        public void startForResult(final int requestCode) {
            if (uri == null) {
                throw new IllegalArgumentException("url must be passed");
            }

            final MappingItemBean itemBean = mappingManager.matchRouter(uri);
            if (itemBean == null) {
                return;
            }

            try {
                final Class<?> fragment = TextUtils.isEmpty(itemBean.fragment) ? null : Class.forName(itemBean.fragment);
                final Class<?> activity = TextUtils.isEmpty(itemBean.activity) ? null : Class.forName(itemBean.activity);
                if (fragment == null && activity == null) {
                    throw new IllegalArgumentException("class must be activity or fragment");
                }

                // 不等于0，则需要先走login逻辑
                if (itemBean.isLogin != 0) {
                    String loginActivity = mappingManager.loginActivity();
                    if (loginActivity != null) {
                        fromFragment.gotoActivity((Class<? extends RootActivity>) Class.forName(loginActivity));
                        LoginInterceptorManager.push(new LoginInterceptorManager.LoginFinishedCallback() {
                            @Override
                            public void finished(boolean isSuccess, Bundle bundle) {
                                if (isSuccess) {
                                    // 在跳转前，给出全局的回调
                                    if (interceptors != null) {
                                        for (IGlobalInterceptor interceptor : interceptors) {
                                            interceptor.interceptor(uri, fromFragment);
                                        }
                                    }

                                    try {
                                        if (bundle != null) {
                                            itemBean.bundle.putAll(bundle);
                                        }
                                        if (activity == null) {
                                            RootFragment targetFragment = (RootFragment) fragment.newInstance();
                                            if (isHideFromView) {
                                                fromFragment.addFragmentForResult(fromFragment, targetFragment, itemBean.bundle, animBean, requestCode, stackMode);
                                            } else {
                                                fromFragment.addFragmentForResult(targetFragment, itemBean.bundle, animBean, requestCode, stackMode);
                                            }
                                        } else {
                                            if (fragment == null) {
                                                fromFragment.gotoActivityForResult(fromFragment, (Class<? extends RootActivity>) activity, itemBean.bundle, animBean, requestCode);
                                            } else {
                                                fromFragment.gotoActivityFragmentForResult(fromFragment, (Class<? extends RootActivity>) activity,
                                                        (Class<? extends RootFragment>) fragment, itemBean.bundle, animBean, requestCode);
                                            }
                                        }
                                    } catch (InstantiationException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        return;
                    }
                }

                // 在跳转前，给出全局的回调
                if (interceptors != null) {
                    for (IGlobalInterceptor interceptor : interceptors) {
                        interceptor.interceptor(uri, fromFragment);
                    }
                }

                if (activity == null) {
                    RootFragment targetFragment = (RootFragment) fragment.newInstance();
                    if (isHideFromView) {
                        fromFragment.addFragmentForResult(fromFragment, targetFragment, itemBean.bundle, animBean, requestCode, stackMode);
                    } else {
                        fromFragment.addFragmentForResult(targetFragment, itemBean.bundle, animBean, requestCode, stackMode);
                    }
                } else {
                    if (fragment == null) {
                        fromFragment.gotoActivityForResult(fromFragment, (Class<? extends RootActivity>) activity, itemBean.bundle, animBean, requestCode);
                    } else {
                        fromFragment.gotoActivityFragmentForResult(fromFragment, (Class<? extends RootActivity>) activity,
                                (Class<? extends RootFragment>) fragment, itemBean.bundle, animBean, requestCode);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
