package com.spms.parkingservice.service;

import com.spms.parkingservice.dto.req.ParkingSaveReq;
import com.spms.parkingservice.dto.req.ParkingUpdateReq;
import com.spms.parkingservice.dto.res.ParkingDetailRes;
import com.spms.parkingservice.dto.res.ParkingReservationRes;
import com.spms.parkingservice.dto.res.ParkingSummaryRes;

import java.util.List;

public interface ParkingService {
    List<ParkingSummaryRes> getAll();

    ParkingDetailRes getById(Long id);

    ParkingDetailRes save(ParkingSaveReq req);

    ParkingDetailRes update(Long id, ParkingUpdateReq req);

    void delete(Long id);

    List<ParkingSummaryRes> getAvailableParking();

    ParkingDetailRes getParkingByVehicleId(Long vehicleId);

    ParkingReservationRes reserveParking(Long vehicleId, Long parkingId);

    ParkingReservationRes releaseParking(Long parkingId);
}
