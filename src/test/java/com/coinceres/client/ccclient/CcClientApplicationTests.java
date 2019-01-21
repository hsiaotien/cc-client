package com.coinceres.client.ccclient;

import com.coinceres.client.ccclient.pojo.Greeting;
import com.coinceres.client.ccclient.pojo.HelloMessage;
import com.coinceres.client.ccclient.service.CallBackService;
import com.coinceres.client.ccclient.service.CoinceresWebSocketClient;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class CcClientApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void test01() throws InterruptedException {
        CoinceresWebSocketClient ccClient = new CoinceresWebSocketClient();
        String topic = "/topic/greetings";
        String target = "/app/hello";
        String url = "ws://localhost:8080/openmarket";
        HelloMessage helloMessage = new HelloMessage("cc-socket");
        ccClient.connect(topic, target, Greeting.class, new CallBackService() {
            @Override
            public void callBack(Object obj) {
                System.out.println("greeting.getContent() = " + obj);
            }
        }, url, helloMessage);
//        Thread thread = Thread.currentThread();
//        thread.sleep(1000*3);
//        ccClient.connect(topic, target, Greeting.class, new CallBackService<Greeting>() {
//            @Override
//            public void callBack(Greeting greeting) {
//                System.out.println("greeting.getContent() = " + greeting.getContent());
//            }
//        }, url, helloMessage);
//        thread.sleep(1000*3);
//        ccClient.connect(topic, target, Greeting.class, new CallBackService<Greeting>() {
//            @Override
//            public void callBack(Greeting greeting) {
//                System.out.println("greeting.getContent() = " + greeting.getContent());
//            }
//        }, url, helloMessage);
    }

}

