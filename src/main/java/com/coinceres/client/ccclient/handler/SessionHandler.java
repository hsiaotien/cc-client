package com.coinceres.client.ccclient.handler;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * session handler
 */
public class SessionHandler extends StompSessionHandlerAdapter {

    private final AtomicReference<Throwable> failure;

    public SessionHandler(AtomicReference<Throwable> failure) {
        this.failure = failure;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        this.failure.set(new Exception(headers.toString()));
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        this.failure.set(exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        this.failure.set(exception);
    }
}
