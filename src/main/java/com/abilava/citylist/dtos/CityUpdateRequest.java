package com.abilava.citylist.dtos;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CityUpdateRequest {

  @NotNull(message = "Id cannot be null")
  UUID id;
  @NotBlank(message = "City name cannot be empty")
  String name;
  @Size(min = 3, message = "Image link cannot contain less then 3 symbols")
  String imageUrl;
}
