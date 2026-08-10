package com.spms.parkingservice.exceptions;

public class ParkingNotAvailable extends RuntimeException {
    public ParkingNotAvailable(String message) {
        super(message);
    }
}
