package com.foodcloud.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthGatewayFilterFactory  extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);  // Указываем класс конфигурации
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();

            // 1. Проверяем excluded paths
            if (isExcluded(path, config.getExcludedPaths())) {
                return chain.filter(exchange);
            }

            // 2. Извлекаем токен из header
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7); // Убираем "Bearer "

            // 3. Валидируем JWT
            try {
                Claims claims = parseToken(token, config.getSecretKey());

                // 4. Передаём данные в downstream через headers
                ServerHttpRequest.Builder requestBuilder = request.mutate()
                        .header("X-User-Id", claims.getSubject());

                // Добавляем role только если он присутствует в токене
                String role = claims.get("role", String.class);
                if (role != null) {
                    requestBuilder.header("X-User-Role", role);
                }

                return chain.filter(exchange.mutate().request(requestBuilder.build()).build());

            } catch (JwtException e) {
                return onError(exchange, "Invalid token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Claims parseToken(String token, String secretKey) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isExcluded(String path, List<String> excludedPaths) {
        PathPatternParser parser = new PathPatternParser();
        return excludedPaths.stream()
                .map(parser::parse)
                .anyMatch(pattern -> pattern.matches(PathContainer.parsePath(path)));
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = """                                                                                                                                                                                                    
          {"error": "%s", "status": %d}                                                                                                                                                                                    
          """.formatted(message, status.value());

        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    // Класс для конфигурации из YAML
    @Data
    public static class Config {
        private String secretKey;
        private List<String> excludedPaths = new ArrayList<>();
    }
}
