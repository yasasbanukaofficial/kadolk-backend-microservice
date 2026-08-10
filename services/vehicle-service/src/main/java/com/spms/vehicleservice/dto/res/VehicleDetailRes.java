package com.spms.vehicleservice.dto.res;

import com.spms.vehicleservice.entity.VehicleStatus;
import com.spms.vehicleservice.entity.VehicleType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class VehicleDetailRes {
    private Long id;
    private Long userId;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private String brand;
    private String model;
    private String color;
    private VehicleStatus status;
    private LocalDateTime updatedAt;
}
