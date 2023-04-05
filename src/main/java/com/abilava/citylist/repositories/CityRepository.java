package com.abilava.citylist.repositories;

import com.abilava.citylist.entities.City;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaSpecificationExecutor<City>, JpaRepository<City, UUID> {}
