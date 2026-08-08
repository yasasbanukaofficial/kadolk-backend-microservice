package com.spms.parkingservice.exceptions;

public class VehicleAlreadyReservedException extends RuntimeException {
    public VehicleAlreadyReservedException(String message) {
        super(message);
    }
}
