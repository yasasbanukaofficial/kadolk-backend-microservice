package com.spms.parkingservice.controller;

import com.spms.parkingservice.dto.req.ParkingSaveReq;
import com.spms.parkingservice.dto.req.ParkingUpdateReq;
import com.spms.parkingservice.dto.res.ParkingRes;
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
    public ResponseEntity<ApiResponse<List<ParkingRes>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved All Parking Details Successfully",
                parkingService.getAll()
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingRes>> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Parking Details Successfully",
                parkingService.getById(id)
            )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParkingRes>> save(@Valid @RequestBody ParkingSaveReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Parking Created Successfully",
                parkingService.save(req)
            )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingRes>> update(@PathVariable Long id, @Valid @RequestBody ParkingUpdateReq req) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Parking Updated Successfully",
                parkingService.update(id, req)
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
