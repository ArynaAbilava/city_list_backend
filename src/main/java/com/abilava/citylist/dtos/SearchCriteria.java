package com.abilava.citylist.dtos;

import com.abilava.citylist.dtos.enums.QueryOperator;
import javax.validation.constraints.NotBlank;
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
public class SearchCriteria {

  QueryOperator operator;
  @NotBlank(message = "Searched field cannot be null")
  String field;
  @NotBlank(message = "Searched value cannot be null")
  String value;
}
