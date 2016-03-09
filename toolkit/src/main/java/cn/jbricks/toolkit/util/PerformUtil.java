package cn.jbricks.toolkit.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformUtil {

    public static final Logger logger = LoggerFactory.getLogger(PerformUtil.class);
    public static boolean isTest = false;

    private String name;

    private long startTime;

    private long recordTime;

    public PerformUtil(String name) {
        if (isTest || logger.isDebugEnabled()) {
            this.name = name;
            recordTime = System.currentTimeMillis();
            startTime = recordTime;
        }
    }

    public void record(String action) {
        if (logger.isDebugEnabled()) {
            long currentTimeMillis = System.currentTimeMillis();
            logger.debug("[" + name + "-" + action + "]" + action + ",use time=" + (currentTimeMillis - recordTime) + ",Total time=" + (currentTimeMillis - startTime));
            recordTime = currentTimeMillis;
        }
    }

    /**
     * 单元测试使用
     *
     * @param action
     */
    @Deprecated
    public void testRecord(String action) {
        if (isTest) {
            long currentTimeMillis = System.currentTimeMillis();
            System.out.println("[" + name + "-" + action + "]" + action + ",use time=" + (currentTimeMillis - recordTime) + ",Total time=" + (currentTimeMillis - startTime));
            recordTime = currentTimeMillis;
        }
    }

    public void reset(String name) {
        if (logger.isDebugEnabled()) {
            this.name = name;
            recordTime = System.currentTimeMillis();
            startTime = recordTime;
        }
    }
}
