package com.demo.guard;

/**
 * java实现加密的方式
 *
 * Created by panda on 2017/8/1.
 */
public class Main {
    static String KEYS = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        String data = "panhongchao";

        char[] keys = new char[data.length()];
        for (int i = 0; i < data.length(); i++) {
            keys[i] = KEYS.toCharArray()[i % KEYS.length()];
        }

        char[] chars = data.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            int cInt = ((int) chars[i]);
            int kInt = ((int) keys[i]);
            String c = Character.toString((char) (cInt ^ kInt));
            sb.append(c);
        }

        System.out.println();
        System.out.println(sb);
        System.out.println();

        char[] chars2 = sb.toString().toCharArray();
        sb = new StringBuffer();
        for (int i = 0; i < chars2.length; i++) {
            int cInt = ((int) chars2[i]);
            int kInt = ((int) keys[i]);
            sb.append(Character.toString((char) (cInt ^ kInt)));
        }
        System.out.println(sb);
    }
}
