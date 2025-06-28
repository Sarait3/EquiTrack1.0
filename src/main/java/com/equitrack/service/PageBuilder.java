package com.equitrack.service;

/**
 * An abstract class for building HTML pages dynamically Subclasses must
 * implement the buildPage() method to return a complete HTML page as a string.
 * 
 */
public abstract class PageBuilder {

	/**
	 * Builds and returns the HTML content for the page
	 *
	 * @return a String containing the complete HTML of the page
	 */
	public abstract String buildPage();
}
