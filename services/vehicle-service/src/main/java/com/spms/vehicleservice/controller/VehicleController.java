package com.spms.vehicleservice.controller;

import com.spms.vehicleservice.dto.req.VehicleSaveReq;
import com.spms.vehicleservice.dto.req.VehicleUpdateReq;
import com.spms.vehicleservice.dto.res.VehicleDetailRes;
import com.spms.vehicleservice.dto.res.VehicleEntryExitRes;
import com.spms.vehicleservice.dto.res.VehicleSummaryRes;
import com.spms.vehicleservice.service.VehicleService;
import com.spms.vehicleservice.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleSummaryRes>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved All Vehicle Details Successfully",
                vehicleService.getAll()
            )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<VehicleSummaryRes>>> getVehiclesByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Vehicle Details by User Successfully",
                vehicleService.getVehiclesByUserId(userId)
            )
        );
    }

    @GetMapping("/number/{vehicleNumber}")
    public ResponseEntity<ApiResponse<VehicleDetailRes>> getByVehicleNumber(@PathVariable String vehicleNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Vehicle Details Successfully",
                vehicleService.getByVehicleNumber(vehicleNumber)
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleDetailRes>> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Vehicle Details Successfully",
                vehicleService.getById(id)
            )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleDetailRes>> save(@Valid @RequestBody VehicleSaveReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Vehicle Created Successfully",
                vehicleService.save(req)
            )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleDetailRes>> update(@PathVariable Long id, @Valid @RequestBody VehicleUpdateReq req) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Vehicle Updated Successfully",
                vehicleService.update(id, req)
            )
        );
    }

    @PostMapping("/{id}/entry")
    public ResponseEntity<ApiResponse<VehicleEntryExitRes>> registerEntry(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Vehicle Entry Registered Successfully",
                vehicleService.registerEntry(id)
            )
        );
    }

    @PostMapping("/{id}/exit")
    public ResponseEntity<ApiResponse<VehicleEntryExitRes>> registerExit(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Vehicle Exit Registered Successfully",
                vehicleService.registerExit(id)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Vehicle Deleted Successfully",
                null
            )
        );
    }
}
