
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

import static spark.Spark.*;

public class webService {
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {

        port(8080);
        Cache myCache = new Cache();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("hay");
//                if (System.currentTimeMillis() - myCache.currentTime > 10000) {
//                    myCache.cache.clear();
//                    System.out.println("clear cache");
//                }
                return;
            }
        }, 0, 10, TimeUnit.SECONDS);
        scheduledExecutorService.schedule(new Runnable() {
            public void run() {
                get("/prime", (req, res)->{
                    myCache.currentTime = System.currentTimeMillis();
                    int n = Integer.parseInt(req.queryParams("n"));
                    if (myCache.cache.containsKey(n) == true) {
                        System.out.println(n + " da co trong cache");
                        return myCache.cache.get(n);

                    }
                    else {
                        List<Integer> list = new ArrayList<>();
                        for (int i = 0; i < n; i++) {
                            if (isPrime(i) == true) {
                                list.add(i);
                            }
                        }
                        myCache.cache.put(n, list);
                        System.out.println(n + " chua co trong cache");
                        return list;
                    }
                });
            }
        },0,TimeUnit.SECONDS);

        scheduledExecutorService.shutdown();
    }
}
