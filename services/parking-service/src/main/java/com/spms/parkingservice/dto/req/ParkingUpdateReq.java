package com.spms.parkingservice.dto.req;

import com.spms.parkingservice.entity.ParkingStatus;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class ParkingUpdateReq {

    private Long vehicleId;

    private String city;

    private String zone;

    private String location;

    private String address;

    private String lat;

    private String lng;

    private ParkingStatus status;
}
