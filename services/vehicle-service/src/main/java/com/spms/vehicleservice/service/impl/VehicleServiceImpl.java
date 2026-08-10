package com.spms.vehicleservice.service.impl;

import com.spms.vehicleservice.client.UserServiceClient;
import com.spms.vehicleservice.dto.req.VehicleSaveReq;
import com.spms.vehicleservice.dto.req.VehicleUpdateReq;
import com.spms.vehicleservice.dto.res.VehicleDetailRes;
import com.spms.vehicleservice.dto.res.VehicleEntryExitRes;
import com.spms.vehicleservice.dto.res.VehicleSummaryRes;
import com.spms.vehicleservice.entity.Vehicle;
import com.spms.vehicleservice.entity.VehicleStatus;
import com.spms.vehicleservice.exceptions.VehicleAlreadyInsideException;
import com.spms.vehicleservice.exceptions.VehicleNotFoundException;
import com.spms.vehicleservice.exceptions.VehicleNotInsideException;
import com.spms.vehicleservice.exceptions.VehicleNumberAlreadyExistsException;
import com.spms.vehicleservice.repo.VehicleRepo;
import com.spms.vehicleservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepo vehicleRepo;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public List<VehicleSummaryRes> getAll() {
        return vehicleRepo.findAll().stream()
            .map(vehicle -> modelMapper.map(vehicle, VehicleSummaryRes.class))
            .toList();
    }

    @Override
    public VehicleDetailRes getById(Long id) {
        Vehicle vehicle = vehicleRepo.findById(id).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));
        return modelMapper.map(vehicle, VehicleDetailRes.class);
    }

    @Override
    public VehicleDetailRes getByVehicleNumber(String vehicleNumber) {
        Vehicle vehicle = vehicleRepo.getByVehicleNumber(vehicleNumber)
            .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with number: " + vehicleNumber));
        return modelMapper.map(vehicle, VehicleDetailRes.class);
    }

    @Override
    public List<VehicleSummaryRes> getVehiclesByUserId(Long userId) {
        return vehicleRepo.getAllByUserId(userId).stream()
            .map(vehicle -> modelMapper.map(vehicle, VehicleSummaryRes.class))
            .toList();
    }

    @Override
    public VehicleDetailRes save(VehicleSaveReq req) {
        if (req.getUserId() != null) {
            userServiceClient.validateUserExists(req.getUserId());
        }
        if (vehicleRepo.existsByVehicleNumber(req.getVehicleNumber())) {
            throw new VehicleNumberAlreadyExistsException("Vehicle number already exists: " + req.getVehicleNumber());
        }
        Vehicle vehicle = modelMapper.map(req, Vehicle.class);
        return modelMapper.map(vehicleRepo.save(vehicle), VehicleDetailRes.class);
    }

    @Override
    public VehicleDetailRes update(Long id, VehicleUpdateReq req) {
        Vehicle vehicle = vehicleRepo.findById(id).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));
        vehicle.setVehicleNumber(req.getVehicleNumber());
        vehicle.setVehicleType(req.getVehicleType());
        vehicle.setBrand(req.getBrand());
        vehicle.setModel(req.getModel());
        vehicle.setColor(req.getColor());
        vehicle.setStatus(req.getStatus());
        return modelMapper.map(vehicleRepo.save(vehicle), VehicleDetailRes.class);
    }

    @Override
    public void delete(Long id) {
        Vehicle vehicle = vehicleRepo.findById(id).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));
        vehicleRepo.delete(vehicle);
    }

    @Transactional
    @Override
    public VehicleEntryExitRes registerEntry(Long vehicleId) {
        Vehicle vehicle = vehicleRepo.findByIdForUpdate(vehicleId).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + vehicleId));
        if (vehicle.getStatus() == VehicleStatus.INSIDE) {
            throw new VehicleAlreadyInsideException("Vehicle is already inside");
        }
        vehicle.setStatus(VehicleStatus.INSIDE);
        vehicleRepo.save(vehicle);
        return buildEntryExitRes(vehicle);
    }

    @Transactional
    @Override
    public VehicleEntryExitRes registerExit(Long vehicleId) {
        Vehicle vehicle = vehicleRepo.findByIdForUpdate(vehicleId).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + vehicleId));
        if (vehicle.getStatus() == VehicleStatus.OUTSIDE) {
            throw new VehicleNotInsideException("Vehicle is not inside, nothing to exit");
        }
        vehicle.setStatus(VehicleStatus.OUTSIDE);
        vehicleRepo.save(vehicle);
        return buildEntryExitRes(vehicle);
    }

    private VehicleEntryExitRes buildEntryExitRes(Vehicle vehicle) {
        return VehicleEntryExitRes.builder()
            .vehicleId(vehicle.getId())
            .vehicleNumber(vehicle.getVehicleNumber())
            .status(vehicle.getStatus())
            .timestamp(LocalDateTime.now())
            .build();
    }
}
