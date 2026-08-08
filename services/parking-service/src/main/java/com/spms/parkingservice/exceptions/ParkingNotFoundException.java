package com.spms.parkingservice.exceptions;

public class ParkingNotFoundException extends RuntimeException {
    public ParkingNotFoundException(String message) {
        super(message);
    }
}
