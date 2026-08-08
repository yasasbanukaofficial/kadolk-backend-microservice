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
        if (parkingRepo.findById(parking.getId()).isPresent()) {
            parking.setCity(req.getCity());
            parking.setZone(req.getZone());
            parking.setLocation(req.getLocation());
            parking.setStatus(req.getStatus());
            return modelMapper.map(parkingRepo.save(parking), ParkingDetailRes.class);
        } else {
            throw new ParkingNotAvailable("Parking is not available, Please try again!");
        }
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
    public ParkingDetailRes getParkingByVehicleId(Long vehicleId) {
        return modelMapper.map(parkingRepo.getParkingByVehicleId(vehicleId), ParkingDetailRes.class);
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

    @Override
    public ParkingReservationRes releaseParking(Long parkingId) {
        Parking parking = parkingRepo.findById(parkingId).orElseThrow(() -> new ParkingNotFoundException("Parking not found with id: " + parkingId));
        parking.setVehicleId(null);
        parking.setStatus(ParkingStatus.AVAILABLE);
        return modelMapper.map(parkingRepo.save(parking), ParkingReservationRes.class);
    }
}
