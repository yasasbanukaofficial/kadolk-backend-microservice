package com.spms.vehicleservice.service;

import com.spms.vehicleservice.dto.req.VehicleSaveReq;
import com.spms.vehicleservice.dto.req.VehicleUpdateReq;
import com.spms.vehicleservice.dto.res.VehicleDetailRes;
import com.spms.vehicleservice.dto.res.VehicleEntryExitRes;
import com.spms.vehicleservice.dto.res.VehicleSummaryRes;

import java.util.List;

public interface VehicleService {
    List<VehicleSummaryRes> getAll();

    VehicleDetailRes getById(Long id);

    VehicleDetailRes getByVehicleNumber(String vehicleNumber);

    List<VehicleSummaryRes> getVehiclesByUserId(Long userId);

    VehicleDetailRes save(VehicleSaveReq req);

    VehicleDetailRes update(Long id, VehicleUpdateReq req);

    void delete(Long id);

    VehicleEntryExitRes registerEntry(Long vehicleId);

    VehicleEntryExitRes registerExit(Long vehicleId);
}
