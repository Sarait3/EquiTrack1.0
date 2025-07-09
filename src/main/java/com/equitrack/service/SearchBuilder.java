package com.equitrack.service;

public class SearchBuilder extends FormBuilder {
	public SearchBuilder() {
		super();
	}
	
	public SearchBuilder addFilter(String[][] options) {
		String html = "<select id='statusFilter' name='statusFilter' class='search-input'";
		
		for (String[] option : options) {
			html = html + String.format("<option value='%s'>%s</option>", option[0], option[1]);
		}
		
		form.addInput(html);
		
		return this;
	}
	
	public String createSearch() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("<form class='search-form' method='get' action='%s'>", form.getFormAction()))
		.append("<input type='text' name='searchInput' class='search-input' placeholder='Search'>");
		
		for (String input : form.getInputs()) {
			sb.append(input);
		}
		
		sb.append("<button type='submit' class='search-btn'>Search</button>"
				+ "</form>");
		
		return sb.toString();
	}
}
