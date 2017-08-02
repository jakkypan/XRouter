package com.xrouter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * 标记只有在跳转到fragment时才有用
 *
 * Created by panda on 2017/8/2.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={FIELD, METHOD})
public @interface XFragment {

}
