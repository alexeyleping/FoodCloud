package com.foodcloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();

        // Генерируем Correlation ID только если его нет в запросе
        String correlationId = request.getHeaders().getFirst(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString().replace("-", "");
        }

        // PRE: логируем входящий запрос с методом, путём и query params
        log.info("Request: {} {} queryParams={}, correlationId={}",
                request.getMethod(),
                request.getPath(),
                request.getQueryParams(),
                correlationId);

        // Мутируем request, добавляя correlation ID в headers
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(CORRELATION_ID_HEADER, correlationId)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        // Сохраняем в final для использования в лямбде
        String finalCorrelationId = correlationId;

        return chain.filter(mutatedExchange)
                .then(Mono.fromRunnable(() -> {
                    // POST: добавляем correlation ID в response и логируем результат
                    long duration = System.currentTimeMillis() - startTime;
                    mutatedExchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, finalCorrelationId);
                    log.info("Response: {}, duration: {} ms, correlationId={}",
                            mutatedExchange.getResponse().getStatusCode(),
                            duration,
                            finalCorrelationId);
                }));
    }

    @Override
    public int getOrder() {
        return -1; // чем меньше — тем раньше выполняется
    }
}
