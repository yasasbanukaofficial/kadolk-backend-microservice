package com.spms.parkingservice.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class ParkingSaveReq {

    @NotNull(message = "Owner id is required")
    private Long ownerId;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Zone is required")
    private String zone;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Latitude is required")
    private String lat;

    @NotBlank(message = "Longitude is required")
    private String lng;
}
