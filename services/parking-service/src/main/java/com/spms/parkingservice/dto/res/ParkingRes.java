package com.spms.parkingservice.dto.res;

import com.spms.parkingservice.entity.ParkingStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class ParkingRes {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @Positive
    private Long ownerId;

    @Positive
    private Long vehicleId;

    @NotBlank
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s.'-]*$", message = "City must contain only letters, spaces, dots, apostrophes and hyphens")
    private String city;

    @NotBlank
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9\\s/.-]*$", message = "Zone must contain only letters, numbers, spaces, slashes, dots and hyphens")
    private String zone;

    @NotBlank
    @Size(min = 3, max = 150)
    private String location;

    @NotBlank
    @Size(min = 5, max = 255)
    private String address;

    @NotBlank
    @Pattern(regexp = "^[-+]?([0-8]?[0-9](\\.[0-9]+)?|90(\\.0+)?)$", message = "Latitude must be a valid value between -90 and 90")
    private String lat;

    @NotBlank
    @Pattern(regexp = "^[-+]?((1[0-7][0-9]|[0-9]?[0-9])(\\.[0-9]+)?|180(\\.0+)?)$", message = "Longitude must be a valid value between -180 and 180")
    private String lng;

    @NotNull
    private ParkingStatus status;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;
}
