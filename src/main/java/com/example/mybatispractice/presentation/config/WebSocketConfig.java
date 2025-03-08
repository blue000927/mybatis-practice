package com.example.mybatispractice.presentation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.unit.DataSize;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final int HEARTBEAT_INTERVAL = (int) TimeUnit.SECONDS.toMillis(10);
    private static final int MESSAGE_SIZE_LIMIT = (int) DataSize.ofKilobytes(64).toBytes();
    private static final int SEND_TIME_LIMIT = (int) TimeUnit.SECONDS.toMillis(20);
    private static final int SEND_BUFFER_SIZE_LIMIT = (int) DataSize.ofKilobytes(512).toBytes();

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue")
                .setTaskScheduler(webSocketHeartbeatScheduler())
                .setHeartbeatValue(new long[]{HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL});
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(MESSAGE_SIZE_LIMIT)
                .setSendTimeLimit(SEND_TIME_LIMIT)
                .setSendBufferSizeLimit(SEND_BUFFER_SIZE_LIMIT);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                        message, StompHeaderAccessor.class
                );

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // TODO: JWT 토큰 검증 로직 추가
                }
                return message;
            }
        });
    }

    private TaskScheduler webSocketHeartbeatScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("websocket-heartbeat-thread-");
        scheduler.setDaemon(true);
        scheduler.initialize();
        return scheduler;
    }
}
