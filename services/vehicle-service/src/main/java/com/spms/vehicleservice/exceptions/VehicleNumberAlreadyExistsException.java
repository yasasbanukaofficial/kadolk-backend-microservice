package com.spms.vehicleservice.exceptions;

public class VehicleNumberAlreadyExistsException extends RuntimeException {
    public VehicleNumberAlreadyExistsException(String message) {
        super(message);
    }
}
