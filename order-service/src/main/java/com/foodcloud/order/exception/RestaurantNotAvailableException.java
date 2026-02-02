package com.foodcloud.order.exception;

public class RestaurantNotAvailableException extends RuntimeException {
    public RestaurantNotAvailableException(String message) {
        super(message);
    }
}
