package com.spms.parkingservice.dto.res;

import com.spms.parkingservice.entity.ParkingStatus;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class ParkingSummaryRes {
    private Long id;
    private String city;
    private String zone;
    private String location;
    private ParkingStatus status;
}
