package com.spms.vehicleservice.dto.req;

import com.spms.vehicleservice.entity.VehicleStatus;
import com.spms.vehicleservice.entity.VehicleType;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class VehicleUpdateReq {

    @NotBlank(message = "Vehicle number is required")
    @Size(min = 5, max = 20, message = "Vehicle number must be between 5 and 20 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9\\s.-]*$", message = "Vehicle number must contain only letters, numbers, spaces, dots and hyphens")
    private String vehicleNumber;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotBlank(message = "Brand is required")
    @Size(min = 2, max = 50, message = "Brand must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s.'-]*$", message = "Brand must contain only letters, spaces, dots, apostrophes and hyphens")
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(min = 1, max = 50, message = "Model must be between 1 and 50 characters")
    private String model;

    @NotBlank(message = "Color is required")
    @Size(min = 3, max = 30, message = "Color must be between 3 and 30 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s.-]*$", message = "Color must contain only letters, spaces, dots and hyphens")
    private String color;

    @NotNull(message = "Status is required")
    private VehicleStatus status;
}
