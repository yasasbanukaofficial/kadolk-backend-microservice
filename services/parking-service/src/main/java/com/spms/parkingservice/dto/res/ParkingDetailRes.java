package com.spms.parkingservice.dto.res;

import com.spms.parkingservice.entity.ParkingStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class ParkingDetailRes {
    private Long id;
    private Long ownerId;
    private Long vehicleId;
    private String city;
    private String zone;
    private String location;
    private String address;
    private String lat;
    private String lng;
    private ParkingStatus status;
    private LocalDateTime updatedAt;
}
