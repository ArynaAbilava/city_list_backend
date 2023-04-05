package com.abilava.citylist.unit.controller;

import static com.abilava.citylist.TestUtils.CITY_ROOT_URL;
import static com.abilava.citylist.TestUtils.DEFAULT_CITY_IMAGE_URL;
import static com.abilava.citylist.TestUtils.DEFAULT_CITY_NAME;
import static com.abilava.citylist.TestUtils.getCityResponse;
import static com.abilava.citylist.TestUtils.getCityResponsePage;
import static com.abilava.citylist.TestUtils.getCityUpdateRequest;
import static com.abilava.citylist.TestUtils.getCityUpdateRequestWithMultipleInvalidFields;
import static com.abilava.citylist.TestUtils.getPageable;
import static com.abilava.citylist.TestUtils.getSearchCriteriaList;
import static com.abilava.citylist.TestUtils.getSearchCriteriaListWithInvalidParams;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abilava.citylist.dtos.CityResponse;
import com.abilava.citylist.dtos.CityUpdateRequest;
import com.abilava.citylist.dtos.SearchCriteria;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class CityControllerTest extends AbstractControllerTest {

  @Test
  void update_happyPath() throws Exception {
    //given
    CityUpdateRequest cityUpdateRequest = getCityUpdateRequest();
    CityResponse cityResponse = getCityResponse();

    when(cityService.update(cityUpdateRequest)).thenReturn(cityResponse);

    //when
    mockMvc.perform(put(CITY_ROOT_URL)
        .content(objectMapper.writeValueAsString(cityUpdateRequest))
        .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name", is(DEFAULT_CITY_NAME)))
        .andExpect(jsonPath("$.imageUrl", is(DEFAULT_CITY_IMAGE_URL)));
  }

  @Test
  void update_withMultipleInvalidFields() throws Exception {
    //given
    CityUpdateRequest cityUpdateRequest = getCityUpdateRequestWithMultipleInvalidFields();
    List<String> expectedExceptions = List.of(
        "Field: name. Message: City name cannot be empty.",
        "Field: id. Message: Id cannot be null.",
        "Field: imageUrl. Message: Image link cannot contain less then 3 symbols.");

    //when
    mockMvc.perform(put(CITY_ROOT_URL)
        .content(objectMapper.writeValueAsString(cityUpdateRequest))
        .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details", containsString(expectedExceptions.get(0))))
        .andExpect(jsonPath("$.details", containsString(expectedExceptions.get(1))))
        .andExpect(jsonPath("$.details", containsString(expectedExceptions.get(2))));

  }

  @Test
  void getAll_happyPath() throws Exception {
    //given
    List<SearchCriteria> searchCriteriaList = getSearchCriteriaList();
    Pageable pageable = getPageable();
    Page<CityResponse> cityResponsePage = getCityResponsePage();

    when(cityService.getAll(searchCriteriaList, pageable)).thenReturn(cityResponsePage);

    //when
    mockMvc.perform(post(CITY_ROOT_URL)
        .param("page", String.valueOf(pageable.getPageNumber()))
        .param("size", String.valueOf(pageable.getPageNumber()))
        .content(objectMapper.writeValueAsString(searchCriteriaList))
        .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void getAll_withMultipleInvalidFields() throws Exception {
    //given
    final List<SearchCriteria> searchCriteriaList = getSearchCriteriaListWithInvalidParams();
    final Pageable pageable = getPageable();
    List<String> expectedExceptions = List.of(
        "Field: getAll.searchCriteria[0].value. Message: Searched value cannot be null.",
        "Field: getAll.searchCriteria[0].field. Message: Searched field cannot be null");

    //when
    mockMvc.perform(post(CITY_ROOT_URL)
        .param("page", String.valueOf(pageable.getPageNumber()))
        .param("size", String.valueOf(pageable.getPageNumber()))
        .content(objectMapper.writeValueAsString(searchCriteriaList))
        .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details", containsString(expectedExceptions.get(0))))
        .andExpect(jsonPath("$.details", containsString(expectedExceptions.get(1))));
  }
}
