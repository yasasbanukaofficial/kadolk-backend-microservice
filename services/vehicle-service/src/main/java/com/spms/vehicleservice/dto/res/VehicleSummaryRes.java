package com.spms.vehicleservice.dto.res;

import com.spms.vehicleservice.entity.VehicleStatus;
import com.spms.vehicleservice.entity.VehicleType;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class VehicleSummaryRes {
    private Long id;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private String brand;
    private String model;
    private VehicleStatus status;
}
