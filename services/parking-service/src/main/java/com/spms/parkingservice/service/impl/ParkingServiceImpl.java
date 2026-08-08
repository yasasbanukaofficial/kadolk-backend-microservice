package com.spms.parkingservice.service.impl;

import com.spms.parkingservice.dto.req.ParkingSaveReq;
import com.spms.parkingservice.dto.req.ParkingUpdateReq;
import com.spms.parkingservice.dto.res.ParkingRes;
import com.spms.parkingservice.entity.Parking;
import com.spms.parkingservice.exceptions.ParkingNotAvailable;
import com.spms.parkingservice.exceptions.ParkingNotFoundException;
import com.spms.parkingservice.repo.ParkingRepo;
import com.spms.parkingservice.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {
    private final ParkingRepo parkingRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<ParkingRes> getAll() {
        return parkingRepo.findAll().stream()
            .map(parking -> modelMapper.map(parking, ParkingRes.class))
            .toList();
    }

    @Override
    public ParkingRes getById(Long id) {
        Parking parking =  parkingRepo.findById(id).orElseThrow(() -> new ParkingNotFoundException("Parking not found with id: " + id));
        return modelMapper.map(parking, ParkingRes.class);
    }

    @Override
    public ParkingRes save(ParkingSaveReq req) {
        Parking parking = modelMapper.map(req, Parking.class);
        return modelMapper.map(parkingRepo.save((parking)), ParkingRes.class);
    }

    @Override
    public ParkingRes update(Long id, ParkingUpdateReq req) {
        Parking parking = parkingRepo.findById(id).orElseThrow(() -> new ParkingNotFoundException("Parking not found with id: " + id));
        if (parkingRepo.findByIdForUpdate(parking.getId()).isPresent()) {
            parking.setVehicleId(req.getVehicleId());
            parking.setCity(req.getCity());
            parking.setZone(req.getZone());
            parking.setLocation(req.getLocation());
            parking.setStatus(req.getStatus());
            return modelMapper.map(parkingRepo.save(parking), ParkingRes.class);
        } else {
            throw new ParkingNotAvailable("Parking is not available, Please try again!");
        }
    }

    @Override
    public void delete(Long id) {
        Parking parking = parkingRepo.findById(id).orElseThrow(() -> new ParkingNotFoundException("Parking not found with id: " + id));
        parkingRepo.delete(parking);
    }
}
