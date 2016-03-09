package cn.jbricks.toolkit.util;

import java.util.*;

public class PrimaryKeyUtil {

    private static Queue<String> queue = new LinkedList();

    public static long getUid() {
        long key = System.currentTimeMillis();
        key = key * 10 + new Random().nextInt(9);
        key = key * 10 + new Random().nextInt(9);
        key = key * 10 + new Random().nextInt(9);
        key = key * 10 + new Random().nextInt(9);

        if (queue.contains(String.valueOf(key))) {
            return getUid();
        } else {
            queue.add(String.valueOf(key));
            if (queue.size() >= 10000) {
                queue.poll();
            }
            return key;
        }
    }

    public static final void main(String[] arg) {
        System.out.println(PrimaryKeyUtil.getUid());
        System.out.println(PrimaryKeyUtil.getUid());
        System.out.println(PrimaryKeyUtil.getUid());
        System.out.println(PrimaryKeyUtil.getUid());
        System.out.println(PrimaryKeyUtil.getUid());

        long s = System.currentTimeMillis();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            String key = String.valueOf(PrimaryKeyUtil.getUid());
            if (list.contains(key)) {
                System.out.println("exist");
            } else {
                list.add(key);
            }
        }
        System.out.println("time=" + (System.currentTimeMillis() - s));

        System.out.println(Long.MAX_VALUE);
    }
}
