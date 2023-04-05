package com.abilava.citylist.unit.services;

import static org.mockito.Mockito.mockStatic;

import com.abilava.citylist.mappers.CityMapper;
import com.abilava.citylist.repositories.CityRepository;
import com.abilava.citylist.specifications.CitySpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractServiceTest {

  @Mock
  protected CityRepository cityRepository;

  @Mock
  protected CityMapper cityMapper;

  protected static MockedStatic<CitySpecification> mockedCitySpecification;

  @BeforeAll
  public static void init() {
    mockedCitySpecification = mockStatic(CitySpecification.class);
  }

  @AfterAll
  public static void close() {
    mockedCitySpecification.close();
  }

}
