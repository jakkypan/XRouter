package com.xrouter;

import android.net.Uri;
import android.support.v4.util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * uri解析器
 *
 * Created by panda on 2017/8/2.
 */
public class UriParser {

    private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final String DEFAULT_CHARSET = "utf-8";

    public static List<Pair<String, String>> parseQuery(final Uri uri) {
        return parseQuery(uri, DEFAULT_CHARSET);
    }

    /**
     * 解析出uri中的query信息
     *
     * @param uri
     * @param encoding
     * @return
     */
    public static List<Pair<String, String>> parseQuery(final Uri uri, final String encoding) {
        final String query = uri.getQuery();
        if (query != null && query.length() > 0) {
            List<Pair<String, String>> result = new ArrayList<>();
            Scanner scanner = new Scanner(query);
            parseQuery(result, scanner, encoding);
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    private static void parseQuery(List<Pair<String, String>> parameters, Scanner scanner, String charset) {
        scanner.useDelimiter(PARAMETER_SEPARATOR);
        while (scanner.hasNext()) {
            String name = null;
            String value = null;
            String token = scanner.next();
            int i = token.indexOf(NAME_VALUE_SEPARATOR);
            if (i != -1) {
                name = decode(token.substring(0, i).trim(), charset);
                value = decode(token.substring(i + 1).trim(), charset);
            } else {
                name = decode(token.trim(), charset);
            }
            parameters.add(new Pair<>(name, value));
        }
    }

    /**
     * query参数编码
     *
     * @param content
     * @param charset
     * @return
     */
    public static String decode (final String content, final String charset) {
        if (content == null) {
            return null;
        }
        try {
            return URLDecoder.decode(content, charset != null ? charset : DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * query参数编码
     *
     * @param content
     * @param charset
     * @return
     */
    public static String decode (final String content, final Charset charset) {
        if (content == null) {
            return null;
        }
        return decode(content, charset != null ? charset.name() : null);
    }

    /**
     * query参数解码
     *
     * @param content
     * @param charset
     * @return
     */
    public static String encode(final String content, final String charset) {
        if (content == null) {
            return null;
        }
        try {
            return URLEncoder.encode(content, charset != null ? charset : DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * query参数解码
     *
     * @param content
     * @param charset
     * @return
     */
    public static String encode(final String content, final Charset charset) {
        if (content == null) {
            return null;
        }
        return encode(content, charset != null ? charset.name() : null);
    }

}
