package com.group.gastos.others.services;


import com.group.gastos.services.Intefaces.ResumenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyTaskExecutor {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    @Autowired
    ResumenService myTask;
    volatile boolean isStopIssued;

    public void startExecutionAt(int targetHour, int targetMin, int targetSec) {
        Runnable taskWrapper = new Runnable() {

            @Override
            public void run() {
                try {
                    //                myTask.execute();
                    System.out.println("\nEL PROGRAMA FUNCIONA A LAS " + LocalDateTime.now());
                    TimeUnit.SECONDS.sleep(1);
                    startExecutionAt(targetHour, targetMin, targetSec);
                } catch (Exception e) {
                    System.out.println("\nERROR EN EL PROGRAMA" + e.getMessage() + e.getCause());
                }
            }
        };
        long delay = computeNextDelay(targetHour, targetMin, targetSec);
        executorService.schedule(taskWrapper, delay, TimeUnit.SECONDS);
    }

    private long computeNextDelay(int targetHour, int targetMin, int targetSec) {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.systemDefault();
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
        if (zonedNow.compareTo(zonedNextTarget) > 0)
            System.out.println("entro al if");
        zonedNextTarget = zonedNextTarget.plusMinutes(1);

        System.out.println("Proxima ejecucion: " + zonedNextTarget.getHour() + ":" + zonedNextTarget.getMinute() + ":" + zonedNextTarget.getSecond());

        Duration duration = Duration.between(zonedNow, zonedNextTarget);
        System.out.println(duration.getSeconds());
        return duration.getSeconds();
    }

    public void stop()
    {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyTaskExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}