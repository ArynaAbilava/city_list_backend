package com.abilava.citylist.specifications;

import static org.springframework.data.jpa.domain.Specification.where;

import com.abilava.citylist.entities.AbstractEntity;
import com.abilava.citylist.entities.City;
import com.abilava.citylist.dtos.SearchCriteria;
import com.abilava.citylist.exceptions.SpecificationException;
import java.util.Objects;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CitySpecification {

  public Specification<City> getSpecification(List<SearchCriteria> searchCriteria) {
    return where(getSearchCriteriaSpecification(searchCriteria));
  }

  private Specification<City> getSearchCriteriaSpecification(
      List<SearchCriteria> searchCriteriaList) {
    Specification<City> criteriaSpecification = null;
    if (Objects.nonNull(searchCriteriaList) && !searchCriteriaList.isEmpty()) {
      SearchCriteria firstElement = searchCriteriaList.remove(0);
      criteriaSpecification = where(createSpecification(firstElement));

      for (SearchCriteria searchCriteria : searchCriteriaList) {
        criteriaSpecification = criteriaSpecification.or(createSpecification(searchCriteria));
      }
    }
    return criteriaSpecification;
  }

  private Specification<City> createSpecification(SearchCriteria searchCriteria) {
    if (Objects.isNull(searchCriteria.getField()) || searchCriteria.getField().trim().isEmpty()) {
      return null;
    }

    return (root, query, criteriaBuilder) -> {
      FetchParent<? extends AbstractEntity, ? extends AbstractEntity> from = root;
      String field = searchCriteria.getField();
      String value = searchCriteria.getValue();

      return switch (searchCriteria.getOperator()) {
        case LIKE -> likePredicate(criteriaBuilder, from, field, value);
        default -> throw new SpecificationException("Operation not supported yet");
      };
    };
  }

  private Predicate likePredicate(CriteriaBuilder criteriaBuilder,
      FetchParent<? extends AbstractEntity, ? extends AbstractEntity> fetchParent,
      String field,
      String value) {

    From<? extends AbstractEntity, ? extends AbstractEntity> from =
        (From<? extends AbstractEntity, ? extends AbstractEntity>) fetchParent;
    return criteriaBuilder.like(criteriaBuilder.lower(from.get(field)),
        "%" + value.toLowerCase() + "%");
  }
}
