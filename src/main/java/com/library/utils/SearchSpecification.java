package com.library.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import tools.jackson.databind.JsonNode;

public class SearchSpecification<T> implements Specification<T> {

	private final JsonNode jsonNode;

	public SearchSpecification(JsonNode jsonNode) {
		this.jsonNode = jsonNode;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public @Nullable Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicateList = new ArrayList<>();
		for (Map.Entry<String, JsonNode> entry : this.jsonNode.properties()) {
			
			Path<Object> column = root.get(entry.getKey());
			
			
			System.out.println(entry.getKey());
			Map.Entry<String, JsonNode> innerEntry = entry.getValue().properties().iterator().next();
			System.out.println(innerEntry.getKey());

			switch (innerEntry.getKey()) {
			case "Like": {
				String term = innerEntry.getValue().properties().iterator().next().getValue().asString();
				predicateList.add(criteriaBuilder.like(column.as(String.class), term));
				break;
			}
			case "Equal": {
				String term = innerEntry.getValue().properties().iterator().next().getValue().asString();
				predicateList.add(criteriaBuilder.equal(column.as(String.class), term ));
				break;
			}
			case "NotLike": {
				String term = innerEntry.getValue().properties().iterator().next().getValue().asString();
				predicateList.add(criteriaBuilder.notLike(column.as(String.class), term ));
				break;
			}
			case "Between":{
				String firstTerm = innerEntry.getValue().properties().iterator().next().getValue().asString();
				String secondTerm = innerEntry.getValue().properties().iterator().next().getValue().asString();
				predicateList.add(criteriaBuilder.between(column.as(String.class), firstTerm, secondTerm)) ;
				break;
			}
			}
		}
	return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));	}

}
