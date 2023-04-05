package com.abilava.citylist.integration;

import com.abilava.citylist.repositories.CityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

  private static final PostgreSQLContainer postgresqlContainer;

  static {
    postgresqlContainer =
        new PostgreSQLContainer("postgres:14.5")
            .withDatabaseName("city_list")
            .withUsername("postgres")
            .withPassword("postgres");
    postgresqlContainer.start();
  }

  @DynamicPropertySource
  public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresqlContainer::getUsername);
    registry.add("spring.datasource.password", postgresqlContainer::getPassword);
  }

  @Autowired
  protected CityRepository cityRepository;

  @Autowired
  protected MockMvc mockMvc;

  protected ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  @Before
  public void clearDatabase() {
    cityRepository.deleteAll();
  }
}
