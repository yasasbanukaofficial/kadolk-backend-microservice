package com.spms.vehicleservice.dto.res;

import com.spms.vehicleservice.entity.VehicleStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class VehicleEntryExitRes {
    private Long vehicleId;
    private String vehicleNumber;
    private VehicleStatus status;
    private LocalDateTime timestamp;
}
