package com.spms.vehicleservice.exceptions;

public class VehicleNotInsideException extends RuntimeException {
    public VehicleNotInsideException(String message) {
        super(message);
    }
}
