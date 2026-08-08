package com.spms.parkingservice.dto.req;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class ParkingSaveReq {

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s.'-]*$", message = "City must contain only letters, spaces, dots, apostrophes and hyphens")
    private String city;

    @NotBlank(message = "Zone is required")
    @Size(min = 1, max = 50, message = "Zone must be between 1 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9\\s/.-]*$", message = "Zone must contain only letters, numbers, spaces, slashes, dots and hyphens")
    private String zone;

    @NotBlank(message = "Location is required")
    @Size(min = 3, max = 150, message = "Location must be between 3 and 150 characters")
    private String location;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @NotBlank(message = "Latitude is required")
    @Pattern(regexp = "^[-+]?([0-8]?[0-9](\\.[0-9]+)?|90(\\.0+)?)$", message = "Latitude must be a valid value between -90 and 90")
    private String lat;

    @NotBlank(message = "Longitude is required")
    @Pattern(regexp = "^[-+]?((1[0-7][0-9]|[0-9]?[0-9])(\\.[0-9]+)?|180(\\.0+)?)$", message = "Longitude must be a valid value between -180 and 180")
    private String lng;
}
