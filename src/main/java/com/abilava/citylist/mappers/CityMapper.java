package com.abilava.citylist.mappers;

import com.abilava.citylist.entities.City;
import com.abilava.citylist.dtos.CityResponse;
import com.abilava.citylist.dtos.CityUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CityMapper {

  CityResponse toCityResponse(City city);

  @Mapping(target = "id", ignore = true)
  City toCity(@MappingTarget City city, CityUpdateRequest cityUpdateRequest);
}
