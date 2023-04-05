package com.abilava.citylist;

import com.abilava.citylist.dtos.CityResponse;
import com.abilava.citylist.dtos.CityUpdateRequest;
import com.abilava.citylist.dtos.SearchCriteria;
import com.abilava.citylist.dtos.enums.QueryOperator;
import com.abilava.citylist.entities.City;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class TestUtils {

  public static final String CITY_ROOT_URL = "/cities";

  public static final UUID DEFAULT_ID = new UUID(0, 0);
  public static final String DEFAULT_CITY_NAME = "test";
  public static final String DEFAULT_CITY_IMAGE_URL = "https://test";

  public static City getCity() {
    return City.builder().id(DEFAULT_ID).name(DEFAULT_CITY_NAME).imageUrl(DEFAULT_CITY_IMAGE_URL).build();
  }

  public static CityUpdateRequest getCityUpdateRequest() {
    return CityUpdateRequest.builder().id(DEFAULT_ID).name(DEFAULT_CITY_NAME).imageUrl(DEFAULT_CITY_IMAGE_URL).build();
  }

  public static CityUpdateRequest getCityUpdateRequestWithExistId() {
    return CityUpdateRequest.builder().id(UUID.randomUUID()).name(DEFAULT_CITY_NAME).imageUrl(DEFAULT_CITY_IMAGE_URL).build();
  }

  public static CityUpdateRequest getCityUpdateRequestWithMultipleInvalidFields() {
    return CityUpdateRequest.builder().id(null).name("").imageUrl("t").build();
  }

  public static CityResponse getCityResponse() {
    return CityResponse.builder().id(DEFAULT_ID).name(DEFAULT_CITY_NAME).imageUrl(DEFAULT_CITY_IMAGE_URL).build();
  }

  public static Pageable getPageable() {
    return Pageable.ofSize(1);
  }

  public static List<SearchCriteria> getSearchCriteriaList() {
    final SearchCriteria searchCriteria = SearchCriteria
        .builder()
        .field("name")
        .operator(QueryOperator.LIKE)
        .value("a")
        .build();
    return List.of(searchCriteria);
  }

  public static List<SearchCriteria> getSearchCriteriaListWithInvalidParams() {
    final SearchCriteria searchCriteria = SearchCriteria
        .builder()
        .field(null)
        .operator(null)
        .value("")
        .build();
    return List.of(searchCriteria);
  }

  public static Page<CityResponse> getCityResponsePage() {
    CityResponse cityResponse = CityResponse.builder().id(DEFAULT_ID).name("name").imageUrl(null).build();
    List<CityResponse> cityResponses = List.of(cityResponse);
    return new PageImpl<>(cityResponses, getPageable(), cityResponses.size());
  }

  public static Page<City> getCityPage() {
    City city = getCity();
    List<City> cityResponses = List.of(city);
    return new PageImpl<>(cityResponses, getPageable(), cityResponses.size());
  }
}
