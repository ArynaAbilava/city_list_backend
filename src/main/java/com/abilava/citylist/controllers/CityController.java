package com.abilava.citylist.controllers;

import com.abilava.citylist.dtos.CityResponse;
import com.abilava.citylist.dtos.CityUpdateRequest;
import com.abilava.citylist.dtos.SearchCriteria;
import com.abilava.citylist.services.CityService;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/cities")
public class CityController {

  private final CityService cityService;

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public CityResponse update(@RequestBody @Valid CityUpdateRequest request) {
    return cityService.update(request);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<CityResponse> getAll(
      @ParameterObject @PageableDefault Pageable pageable,
      @RequestBody List<@Valid SearchCriteria> searchCriteria
  ) {
    return cityService.getAll(searchCriteria, pageable);
  }
}
