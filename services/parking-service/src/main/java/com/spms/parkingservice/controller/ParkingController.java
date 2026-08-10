package com.spms.parkingservice.controller;

import com.spms.parkingservice.dto.req.ParkingSaveReq;
import com.spms.parkingservice.dto.req.ParkingUpdateReq;
import com.spms.parkingservice.dto.res.ParkingDetailRes;
import com.spms.parkingservice.dto.res.ParkingReservationRes;
import com.spms.parkingservice.dto.res.ParkingSummaryRes;
import com.spms.parkingservice.service.ParkingService;
import com.spms.parkingservice.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/parking")
public class ParkingController {
    private final ParkingService parkingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ParkingSummaryRes>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved All Parking Details Successfully",
                parkingService.getAll()
            )
        );
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<ParkingSummaryRes>>> getAvailableParking() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Available Parking Details Successfully",
                parkingService.getAvailableParking()
            )
        );
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<ApiResponse<List<ParkingSummaryRes>>> getByLocation(@PathVariable String location) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Parking Details by Location Successfully",
                parkingService.getParkingByLocation(location)
            )
        );
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<ParkingDetailRes>> getParkingByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Parking Details Successfully",
                parkingService.getParkingByVehicleId(vehicleId)
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingDetailRes>> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Parking Details Successfully",
                parkingService.getById(id)
            )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParkingDetailRes>> save(@Valid @RequestBody ParkingSaveReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Parking Created Successfully",
                parkingService.save(req)
            )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingDetailRes>> update(@PathVariable Long id, @Valid @RequestBody ParkingUpdateReq req) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Parking Updated Successfully",
                parkingService.update(id, req)
            )
        );
    }

    @PostMapping("/{parkingId}/reserve/{vehicleId}")
    public ResponseEntity<ApiResponse<ParkingReservationRes>> reserveParking(@PathVariable Long vehicleId, @PathVariable Long parkingId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Parking Reserved Successfully",
                parkingService.reserveParking(vehicleId, parkingId)
            )
        );
    }

    @PostMapping("/{parkingId}/release")
    public ResponseEntity<ApiResponse<ParkingReservationRes>> releaseParking(@PathVariable Long parkingId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Parking Released Successfully",
                parkingService.releaseParking(parkingId)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        parkingService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Parking Deleted Successfully",
                null
            )
        );
    }
}
