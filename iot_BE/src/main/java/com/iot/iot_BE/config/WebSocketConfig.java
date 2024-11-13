package com.iot.iot_BE.config;

import com.iot.iot_BE.socket.MyHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyHandler(), "/websocket")
                .setAllowedOrigins("*");  // Cấu hình CORS cho WebSocket
    }

    // Cấu hình CORS cho các request khác nếu cần
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Cho phép credentials
        config.addAllowedOriginPattern("*"); // Cho phép tất cả các origin
        config.addAllowedHeader("*"); // Cho phép tất cả các header
        config.addAllowedMethod("*"); // Cho phép tất cả các method (GET, POST, PUT, DELETE...)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
