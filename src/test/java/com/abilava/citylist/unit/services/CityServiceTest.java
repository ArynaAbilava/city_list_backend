package com.abilava.citylist.unit.services;

import static com.abilava.citylist.TestUtils.DEFAULT_ID;
import static com.abilava.citylist.TestUtils.getCity;
import static com.abilava.citylist.TestUtils.getCityPage;
import static com.abilava.citylist.TestUtils.getCityResponse;
import static com.abilava.citylist.TestUtils.getCityUpdateRequest;
import static com.abilava.citylist.TestUtils.getPageable;
import static com.abilava.citylist.TestUtils.getSearchCriteriaList;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.abilava.citylist.dtos.CityResponse;
import com.abilava.citylist.dtos.CityUpdateRequest;
import com.abilava.citylist.dtos.SearchCriteria;
import com.abilava.citylist.entities.City;
import com.abilava.citylist.exceptions.NotFoundException;
import com.abilava.citylist.services.impl.CityServiceImpl;
import com.abilava.citylist.specifications.CitySpecification;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

class CityServiceTest extends AbstractServiceTest {

  @InjectMocks
  private CityServiceImpl cityService;

  @Test
  void update_happyPath() {
    //given
    CityUpdateRequest cityUpdateRequest = getCityUpdateRequest();
    City city = getCity();
    CityResponse cityResponse = getCityResponse();

    when(cityRepository.findById(cityUpdateRequest.getId())).thenReturn(Optional.of(city));
    when(cityMapper.toCity(city, cityUpdateRequest)).thenReturn(city);
    when(cityRepository.save(city)).thenReturn(city);
    when(cityMapper.toCityResponse(city)).thenReturn(cityResponse);

    //when
    final CityResponse response = cityService.update(cityUpdateRequest);

    //then
    verify(cityRepository).findById(DEFAULT_ID);
    verify(cityRepository).save(city);
    assertSoftly(softly -> {
      softly.assertThat(cityResponse.getId()).isEqualTo(response.getId());
      softly.assertThat(cityResponse.getName()).isEqualTo(response.getName());
      softly.assertThat(cityResponse.getImageUrl()).isEqualTo(response.getImageUrl());
    });
  }

  @Test
  void update_cityNotFound() {
    //given
    CityUpdateRequest cityUpdateRequest = getCityUpdateRequest();
    String exceptionMessage = String.format("City with id %s not found", cityUpdateRequest.getId());

    when(cityRepository.findById(cityUpdateRequest.getId())).thenReturn(Optional.empty());

    //when
    final NotFoundException exception = assertThrows(NotFoundException.class,
        () -> cityService.update(cityUpdateRequest), exceptionMessage);

    //then
    verifyNoInteractions(cityMapper);
    assertEquals(exceptionMessage, exception.getMessage());
  }

  @Test
  void getAll_happyPath() {
    //given
    List<SearchCriteria> searchCriteriaList = getSearchCriteriaList();
    Pageable pageable = getPageable();
    Page<City> cityResponsePage = getCityPage();
    Specification<City> citySpecification = Specification.where(null);

    mockedCitySpecification.when(() -> CitySpecification.getSpecification(searchCriteriaList)).thenReturn(citySpecification);
    when(cityRepository.findAll(citySpecification, pageable)).thenReturn(cityResponsePage);

    //when
    final Page<CityResponse> response = cityService.getAll(searchCriteriaList, pageable);

    //then
    verify(cityRepository).findAll(citySpecification, pageable);
    assertNotNull(response);
  }
}
