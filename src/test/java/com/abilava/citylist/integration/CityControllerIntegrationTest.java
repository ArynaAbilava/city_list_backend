package com.abilava.citylist.integration;

import static com.abilava.citylist.TestUtils.CITY_ROOT_URL;
import static com.abilava.citylist.TestUtils.DEFAULT_CITY_IMAGE_URL;
import static com.abilava.citylist.TestUtils.DEFAULT_CITY_NAME;
import static com.abilava.citylist.TestUtils.getCityUpdateRequestWithExistId;
import static com.abilava.citylist.TestUtils.getCityUpdateRequestWithMultipleInvalidFields;
import static com.abilava.citylist.TestUtils.getPageable;
import static com.abilava.citylist.TestUtils.getSearchCriteriaList;
import static com.abilava.citylist.TestUtils.getSearchCriteriaListWithInvalidParams;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abilava.citylist.dtos.CityResponse;
import com.abilava.citylist.dtos.CityUpdateRequest;
import com.abilava.citylist.dtos.ExceptionResponse;
import com.abilava.citylist.dtos.SearchCriteria;
import com.abilava.citylist.entities.City;
import com.abilava.citylist.exceptions.NotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MvcResult;

class CityControllerIntegrationTest extends AbstractIntegrationTest {

  @Test
  void update_happyPath() throws Exception {
    //given
    City city = createCity(DEFAULT_CITY_NAME, DEFAULT_CITY_IMAGE_URL);
    CityUpdateRequest cityUpdateRequest = CityUpdateRequest.builder()
        .id(city.getId())
        .name("San Jose")
        .imageUrl(null)
        .build();

    //when
    MvcResult mvcResult =
        mockMvc.perform(put(CITY_ROOT_URL)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cityUpdateRequest)))
            .andExpect(status().isOk())
            .andReturn();

    CityResponse response = objectMapper.readValue(
        mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {});

    City updatedCity = getById(response.getId());
    //then
    assertSoftly(softly -> {
      softly.assertThat(response).isNotNull();
      softly.assertThat(updatedCity).isNotNull();
      softly.assertThat(response.getId()).isEqualTo(cityUpdateRequest.getId());
      softly.assertThat(response.getName()).isEqualTo(cityUpdateRequest.getName());
      softly.assertThat(response.getImageUrl()).isEqualTo(cityUpdateRequest.getImageUrl());
      softly.assertThat(response.getId()).isEqualTo(updatedCity.getId());
      softly.assertThat(response.getName()).isEqualTo(updatedCity.getName());
      softly.assertThat(response.getImageUrl()).isEqualTo(updatedCity.getImageUrl());
    });
  }

  @Test
  void update_withMultipleInvalidFields() throws Exception {
    //given
    CityUpdateRequest cityUpdateRequest = getCityUpdateRequestWithMultipleInvalidFields();

    //when
    MvcResult mvcResult =
        mockMvc.perform(put(CITY_ROOT_URL)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cityUpdateRequest)))
            .andExpect(status().isBadRequest())
            .andReturn();

    ExceptionResponse response = objectMapper.readValue(
        mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {});

    //then
    assertSoftly(softly -> {
      softly.assertThat(response).isNotNull();
      softly.assertThat(response.getDetails()).isNotNull();
    });
  }

  @Test
  void update_cityNotFound() throws Exception {
    //given
    CityUpdateRequest cityUpdateRequest = getCityUpdateRequestWithExistId();

    //when
    MvcResult mvcResult =
        mockMvc.perform(put(CITY_ROOT_URL)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cityUpdateRequest)))
            .andExpect(status().isNotFound())
            .andReturn();

    ExceptionResponse response = objectMapper.readValue(
        mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {});

    //then
    assertSoftly(softly -> {
      softly.assertThat(response).isNotNull();
      softly.assertThat(response.getDetails()).isEqualTo(String.format("City with id %s not found", cityUpdateRequest.getId()));
    });
  }

  @Test
  void getAll_happyPath() throws Exception {
    //given
    Pageable pageable = getPageable();
    List<SearchCriteria> searchCriteria = getSearchCriteriaList();

    //when
    MvcResult mvcResult =
        mockMvc.perform(post(CITY_ROOT_URL)
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(searchCriteria)))
            .andExpect(status().isOk())
            .andReturn();

    //then
    assertSoftly(softly -> softly.assertThat(mvcResult.getResponse()).isNotNull());
  }

  @Test
  void getAll_withMultipleInvalidFields() throws Exception {
    //given
    Pageable pageable = getPageable();
    List<SearchCriteria> searchCriteria = getSearchCriteriaListWithInvalidParams();

    //when
    MvcResult mvcResult =
        mockMvc.perform(post(CITY_ROOT_URL)
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(searchCriteria)))
            .andExpect(status().isBadRequest())
            .andReturn();

    ExceptionResponse response = objectMapper.readValue(
        mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {});

    //then
    assertSoftly(softly -> softly.assertThat(response.getDetails()).isNotNull());
  }

  private City createCity(String name, String imageUrl) {
    City city = City.builder().name(name).imageUrl(imageUrl).build();
    return cityRepository.save(city);
  }

  private City getById(UUID id) {
    return cityRepository.findById(id).orElseThrow(
        () -> new NotFoundException(String.format("City with id %s not found", id))
    );
  }
}
