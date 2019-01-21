package com.coinceres.client.ccclient.service;

import com.coinceres.client.ccclient.factory.WebSocketClientFactory;
import com.coinceres.client.ccclient.handler.SessionHandler;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class CoinceresWebSocketClient<T> {

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    public void connect(String topic, String target, Class<T> tClass, CallBackService callBackService, String url, Object object) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        //
        StompSessionHandler handler = new SessionHandler(failure) {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

                session.subscribe(topic, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders stompHeaders) {
                        return tClass;
                    }

                    @Override
                    public void handleFrame(StompHeaders stompHeaders, Object payload) {
                        T t = (T) payload;
                        try {
                            callBackService.callBack(t);
                        } catch (Throwable t1) {
                            failure.set(t1);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }

                    }
                });
                try {
                    session.send(target, object);
                } catch (Throwable t2) {
                    failure.set(t2);
                    latch.countDown();
                }
            }
        };
        //
        WebSocketStompClient webSocketClient = WebSocketClientFactory.getWebSocketClient();
        //
        webSocketClient.connect(url, this.headers, handler);

        if (latch.await(5, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        } else {
            throw new RuntimeException("not received");
        }
    }
}
