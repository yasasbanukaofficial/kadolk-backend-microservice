package com.spms.vehicleservice.exceptions;

public class VehicleAlreadyInsideException extends RuntimeException {
    public VehicleAlreadyInsideException(String message) {
        super(message);
    }
}
