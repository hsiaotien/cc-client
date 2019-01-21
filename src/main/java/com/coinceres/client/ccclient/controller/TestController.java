package com.coinceres.client.ccclient.controller;

import com.coinceres.client.ccclient.pojo.Greeting;
import com.coinceres.client.ccclient.pojo.HelloMessage;
import com.coinceres.client.ccclient.pojo.Ping;
import com.coinceres.client.ccclient.pojo.Pong;
import com.coinceres.client.ccclient.service.CallBackService;
import com.coinceres.client.ccclient.service.CoinceresWebSocketClient;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 */
@RestController
@RequestMapping("cc")
public class TestController {

    @GetMapping("test")
    public ResponseEntity test01() throws InterruptedException {
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
        return ResponseEntity.ok().build();
    }

    @GetMapping("test2")
    @Scheduled(fixedRate = 1000*5)
    public ResponseEntity test02() throws InterruptedException {
        CoinceresWebSocketClient ccClient = new CoinceresWebSocketClient();
        String topic = "/topic/pong";
        String target = "/app/ping";
        String url = "ws://localhost:8080/openmarket";
        ccClient.connect(topic, target, Pong.class, new CallBackService() {
            @Override
            public void callBack(Object obj) {
                System.out.println("greeting.getContent() = " + obj);
            }
        }, url, new Ping("ping"));
        return ResponseEntity.ok().build();
    }

}
