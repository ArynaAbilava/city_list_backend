package com.abilava.citylist.services.impl;

import com.abilava.citylist.entities.City;
import com.abilava.citylist.exceptions.NotFoundException;
import com.abilava.citylist.mappers.CityMapper;
import com.abilava.citylist.dtos.CityResponse;
import com.abilava.citylist.dtos.CityUpdateRequest;
import com.abilava.citylist.dtos.SearchCriteria;
import com.abilava.citylist.repositories.CityRepository;
import com.abilava.citylist.services.CityService;
import com.abilava.citylist.specifications.CitySpecification;
import com.abilava.citylist.utils.LogUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

  private final CityMapper cityMapper;
  private final CityRepository cityRepository;

  @Override
  public Page<CityResponse> getAll(List<SearchCriteria> searchCriteria, Pageable pageable) {
    LogUtil.log("Get all", List.of(searchCriteria, pageable));
    Specification<City> specification = CitySpecification.getSpecification(searchCriteria);
    return cityRepository.findAll(specification, pageable).map(cityMapper::toCityResponse);
  }

  @Override
  @Transactional
  public CityResponse update(CityUpdateRequest request) {
    LogUtil.log("Update", List.of(request));
    City city = cityRepository.findById(request.getId()).orElseThrow(() ->
        new NotFoundException(String.format("City with id %s not found", request.getId()))
    );
    City cityToSave = cityMapper.toCity(city, request);
    City updatedCity = cityRepository.save(cityToSave);
    return cityMapper.toCityResponse(updatedCity);
  }
}
