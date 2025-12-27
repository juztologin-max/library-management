package com.library.dto;

import java.util.Map;

public class TableRequest {
	private int start;
	private int limit;
	Map<String, String> sortables;

	public TableRequest() {
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Map<String, String> getSortables() {
		return sortables;
	}

	public void setSortables(Map<String, String> sortables) {
		this.sortables = sortables;
	}

}
