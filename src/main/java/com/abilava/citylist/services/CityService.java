package com.abilava.citylist.services;

import com.abilava.citylist.dtos.CityResponse;
import com.abilava.citylist.dtos.CityUpdateRequest;
import com.abilava.citylist.dtos.SearchCriteria;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {

  Page<CityResponse> getAll(List<SearchCriteria> searchCriteria, Pageable pageable);

  CityResponse update(CityUpdateRequest request);
}
