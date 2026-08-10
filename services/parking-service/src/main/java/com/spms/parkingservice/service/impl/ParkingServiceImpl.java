package com.spms.parkingservice.service.impl;

import com.spms.parkingservice.dto.req.ParkingSaveReq;
import com.spms.parkingservice.dto.req.ParkingUpdateReq;
import com.spms.parkingservice.dto.res.ParkingDetailRes;
import com.spms.parkingservice.dto.res.ParkingReservationRes;
import com.spms.parkingservice.dto.res.ParkingSummaryRes;
import com.spms.parkingservice.entity.Parking;
import com.spms.parkingservice.entity.ParkingStatus;
import com.spms.parkingservice.exceptions.ParkingNotAvailable;
import com.spms.parkingservice.exceptions.ParkingNotFoundException;
import com.spms.parkingservice.exceptions.VehicleAlreadyReservedException;
import com.spms.parkingservice.repo.ParkingRepo;
import com.spms.parkingservice.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {
    private final ParkingRepo parkingRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<ParkingSummaryRes> getAll() {
        return parkingRepo.findAll().stream()
            .map(parking -> modelMapper.map(parking, ParkingSummaryRes.class))
            .toList();
    }

    @Override
    public ParkingDetailRes getById(Long id) {
        Parking parking =  parkingRepo.findById(id).orElseThrow(() -> new ParkingNotFoundException("Parking not found with id: " + id));
        return modelMapper.map(parking, ParkingDetailRes.class);
    }

    @Override
    public ParkingDetailRes save(ParkingSaveReq req) {
        Parking parking = modelMapper.map(req, Parking.class);
        return modelMapper.map(parkingRepo.save((parking)), ParkingDetailRes.class);
    }

    @Override
    public ParkingDetailRes update(Long id, ParkingUpdateReq req) {
        Parking parking = parkingRepo.findById(id).orElseThrow(() -> new ParkingNotFoundException("Parking not found with id: " + id));
        parking.setCity(req.getCity());
        parking.setZone(req.getZone());
        parking.setLocation(req.getLocation());
        parking.setAddress(req.getAddress());
        parking.setLat(req.getLat());
        parking.setLng(req.getLng());
        parking.setStatus(req.getStatus());
        return modelMapper.map(parkingRepo.save(parking), ParkingDetailRes.class);
    }

    @Override
    public void delete(Long id) {
        Parking parking = parkingRepo.findById(id).orElseThrow(() -> new ParkingNotFoundException("Parking not found with id: " + id));
        parkingRepo.delete(parking);
    }

    @Override
    public List<ParkingSummaryRes> getAvailableParking() {
        return parkingRepo.getAllByStatus(ParkingStatus.AVAILABLE).stream()
            .map(parking -> modelMapper.map(parking, ParkingSummaryRes.class))
            .toList();
    }

    @Override
    public List<ParkingSummaryRes> getParkingByLocation(String location) {
        return parkingRepo.getAllByLocationContainingIgnoreCase(location).stream()
            .map(parking -> modelMapper.map(parking, ParkingSummaryRes.class))
            .toList();
    }

    @Override
    public ParkingDetailRes getParkingByVehicleId(Long vehicleId) {
        Parking parking = parkingRepo.getParkingByVehicleId(vehicleId)
            .orElseThrow(() -> new ParkingNotFoundException("Parking not found for vehicle id: " + vehicleId));
        return modelMapper.map(parking, ParkingDetailRes.class);
    }

    @Transactional
    @Override
    public ParkingReservationRes reserveParking(Long vehicleId, Long parkingId) {
        boolean isVehicleReserved = parkingRepo.existsByVehicleId(vehicleId);
        if (isVehicleReserved) {
            throw new VehicleAlreadyReservedException("Vehicle is already reserved");
        }

        Parking parking = parkingRepo.findByIdForUpdate(parkingId).orElseThrow(() -> new ParkingNotFoundException("Parking not found with id: " + parkingId));
        if (parking.getStatus() != ParkingStatus.AVAILABLE) {
            throw new ParkingNotAvailable("Parking is not available, Please try again!");
        }

        parking.setVehicleId(vehicleId);
        parking.setStatus(ParkingStatus.OCCUPIED);
        return modelMapper.map(parkingRepo.save(parking), ParkingReservationRes.class);
    }

    @Transactional
    @Override
    public ParkingReservationRes releaseParking(Long parkingId) {
        Parking parking = parkingRepo.findByIdForUpdate(parkingId).orElseThrow(() -> new ParkingNotFoundException("Parking not found with id: " + parkingId));
        if (parking.getStatus() != ParkingStatus.OCCUPIED) {
            throw new ParkingNotAvailable("Parking is not currently occupied, nothing to release");
        }
        parking.setVehicleId(null);
        parking.setStatus(ParkingStatus.AVAILABLE);
        return modelMapper.map(parkingRepo.save(parking), ParkingReservationRes.class);
    }
}
