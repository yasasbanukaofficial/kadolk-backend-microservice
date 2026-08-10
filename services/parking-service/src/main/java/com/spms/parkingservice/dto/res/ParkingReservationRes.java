package com.spms.parkingservice.dto.res;

import com.spms.parkingservice.entity.ParkingStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class ParkingReservationRes {
    private Long parkingId;
    private Long vehicleId;
    private ParkingStatus status;
    private LocalDateTime timestamp;
}
