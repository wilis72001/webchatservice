package register;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class RegisterController {
    private RegisterApi registerApi;
    public RegisterController(RegisterApi registerApi) {
        this.registerApi = registerApi;
    }

    @PostMapping("/register")
    @ResponseBody
    public Long register(@RequestBody Map<String, String> request) {
        String userName = request.get("userName");
        return registerApi.register(userName);
    }

    @PostMapping("/batchRegister")
    @ResponseBody
    public String batchRegister(@RequestBody Map<String, Object> request) {
        List<Long> mapResTime = Collections.synchronizedList(new ArrayList<>());

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int sum = 0;

        int usernameFrom = convertToInt(request.get("username_from"));
        int usernameTo = convertToInt(request.get("username_to"));
        String usernameEnd = convertToString(request.get("username_end"));
        int numThreads = convertToInt(request.get("numThreads"));

        int transactionPerSecond = 30;
        int totalRegistrations = usernameTo - usernameFrom;
        int registrationsPerThread = totalRegistrations / numThreads;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int startUsername = usernameFrom + (i * registrationsPerThread);
            int endUsername = startUsername + registrationsPerThread;

            Thread thread = new Thread(() -> {
                for (int j = startUsername; j < endUsername; j++) {
                    String userName = j + usernameEnd;
                    Long respTime = registerApi.register(userName);
                    System.out.println(respTime);
                    mapResTime.add(respTime);
                    long delayMilliseconds = 1000 / transactionPerSecond;
                    try {
                        Thread.sleep(delayMilliseconds);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (long num : mapResTime) {
            max = (int) Math.max(max, num);
            min = (int) Math.min(min, num);
            sum += num;
        }

        double average = mapResTime.isEmpty() ? 0 : (double) sum / mapResTime.size();
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        String formattedAverage = decimalFormat.format(average);

        String responseAll = "最大值为：" + max + "，最小值为：" + min + "，平均数为：" + formattedAverage;
        System.out.println("执行完毕！");

        return responseAll;
    }

    private int convertToInt(Object obj) {
        if (obj instanceof Integer) {
            return (int) obj;
        } else if (obj instanceof String) {
            return Integer.parseInt((String) obj);
        } else {
            throw new IllegalArgumentException("Invalid object type: " + obj.getClass());
        }
    }
    private String convertToString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }
}
