package dnn.service;

import dnn.web.InterfaceDoc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ike on 16-10-8.
 */

public class TestTimerListener implements ServletContextListener {

    private Timer timer = null;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 12); // 控制时
//        calendar.set(Calendar.MINUTE, 0);    // 控制分
        calendar.set(Calendar.SECOND, 25);    // 控制秒

        Date time = calendar.getTime();
         timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InterfaceDoc interfaceDoc =new InterfaceDoc();
//                interfaceDoc.FirstMethod();
            }
        },1000*6, 1000*6);// 设定指定的时间time,此处为1小时
//        timer.schedule(new TestTask(),time, 1000*60);// 设定指定的时间time,此处为2000毫秒
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (timer != null) {
            timer.cancel();
            event.getServletContext().log("定时器销毁");
        }
    }
}

