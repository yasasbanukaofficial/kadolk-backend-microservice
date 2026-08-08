package com.spms.parkingservice.service;

import com.spms.parkingservice.dto.req.ParkingSaveReq;
import com.spms.parkingservice.dto.req.ParkingUpdateReq;
import com.spms.parkingservice.dto.res.ParkingRes;

import java.util.List;

public interface ParkingService {
    List<ParkingRes> getAll();

    ParkingRes getById(Long id);

    ParkingRes save(ParkingSaveReq req);

    ParkingRes update(Long id, ParkingUpdateReq req);

    void delete(Long id);
}
