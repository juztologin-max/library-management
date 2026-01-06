package com.library.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import tools.jackson.databind.JsonNode;

@Component
public class SearchSpecification<T> implements Specification<T> {

	private JsonNode jsonNode;
	@Autowired
	private ConversionService conversionService;

	public void setJsonNode(JsonNode jsonNode) {
		this.jsonNode = jsonNode;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public @Nullable Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
			throws IllegalArgumentException {
		List<Predicate> predicateList = new ArrayList<>();
		for (Map.Entry<String, JsonNode> entry : this.jsonNode.properties()) {
			String key = entry.getKey();
			// System.out.println(key);
			Path<Object> column;
			if (key.contains(".")) {
				String[] parts = key.split("\\.");
				column = root.get(parts[0]);
				parts = Arrays.copyOfRange(parts, 1, parts.length);
				for (String part : parts) {
					column = column.get(part);
				}
			} else
				column = root.get(entry.getKey());

			System.out.println(entry.getKey());
			Map.Entry<String, JsonNode> innerEntry = entry.getValue().properties().iterator().next();
			System.out.println(innerEntry.getKey());

			switch (innerEntry.getKey()) {
			case "Like" -> {
				String term = innerEntry.getValue().properties().iterator().next().getValue().asString();
				predicateList.add(criteriaBuilder.like(column.as(String.class), term));

			}
			case "Equal" -> {
				Class<?> type = column.getJavaType();
				Object term = innerEntry.getValue().properties().iterator().next().getValue().asString();

				predicateList.add(criteriaBuilder.equal(column, conversionService.convert(term, type)));

			}

			case "NotLike" -> {
				String term = innerEntry.getValue().properties().iterator().next().getValue().asString();
				predicateList.add(criteriaBuilder.notLike(column.as(String.class), term));
			}
			case "NotEqual" -> {
				Class<?> type = column.getJavaType();
				Object term = innerEntry.getValue().properties().iterator().next().getValue().asString();

				predicateList.add(criteriaBuilder.notEqual(column, conversionService.convert(term, type)));
			}
			case "Between" -> {
				Class<?> type = column.getJavaType();
				Iterator<Entry<String, JsonNode>> iter=innerEntry.getValue().properties().iterator();
				Object firstTerm = iter.next().getValue().asString();
				Object secondTerm = iter.next().getValue().asString();
				System.out.println(firstTerm+" "+secondTerm);
				predicateList.add(criteriaBuilder.between(column.as((Class<? extends Comparable>) type),(Comparable) conversionService.convert(firstTerm,type),(Comparable) conversionService.convert(secondTerm,type)));
			}
			default -> {
				throw new IllegalArgumentException("Unsupported operation: " + innerEntry.getKey());
			}
			}
		}
		
		return !predicateList.isEmpty() ? criteriaBuilder.and(predicateList.toArray(new Predicate[0])) : null;
	}

}
