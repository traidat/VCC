import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import static spark.Spark.get;
import static spark.Spark.port;

public class guavaCache {
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
    public static void main(String args[]) {
        port(8080);
        LoadingCache<Integer, List<Integer>> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .expireAfterWrite(20, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, List<Integer>>() {
                    @Override
                    public List<Integer> load(Integer integer) throws Exception {
                        List<Integer> list = new ArrayList<>();
                        for (int i = 0; i < integer; i++) {
                            if (isPrime(i) == true) {
                                list.add(i);
                            }

                        }
                        return list;
                    }
                });
        get("/prime", (req, res)-> {
            int n = Integer.parseInt(req.queryParams("n"));
            return cache.get(n);
        });

    }
}