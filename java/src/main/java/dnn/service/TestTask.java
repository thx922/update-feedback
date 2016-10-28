package dnn.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.TimerTask;

/**
 * Created by ike on 16-10-8.
 */
public class TestTask extends TimerTask {
    protected final Log logger = LogFactory.getLog(getClass());

    private static boolean isRunning = false;

    @Override
    public void run() {
        // 是否整点
        // Calendar c = Calendar.getInstance();
        // if (C_SCHEDULE_HOUR == c.get(Calendar.HOUR_OF_DAY))

        if (!isRunning) {

            isRunning = true;
            logger.info("开始执行任务。");

            int i = 0;
            while (i++ < 3) {
                logger.info("已完成任务的" + i + "/" + 3);
            }

            isRunning = false;
            logger.info("任务执行结束。");

        } else {
            logger.info("上一次任务执行还未结束，本次任务不能执行。");
        }

    }
}

