package com.nixmash.springdata.solr.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.nixmash.springdata.solr.exceptions.GeoLocationException;
import com.nixmash.springdata.solr.model.Product;

public interface ProductService {

	public List<Product> getAvailableProducts();

	Iterable<Product> getAllRecords();

	List<Product> getProductsByFilter();

	List<Product> getProducts();

	Product getProduct(String Id);

	void updateProductName(Product product);

	Iterable<Product> getProductsByNameOrCategory(String searchTerm);

	Page<Product> getProductsByPopularity(int popularity);

	Page<Product> getTestRecords();

	List<Product> searchWithCriteria(String searchTerm);

	List<Product> getProductsByNameOrCategoryAnnotatedQuery(String searchTerm);

	List<Product> getProductsByStartOfName(String nameStart);

	FacetPage<Product> getFacetedProductsAvailable();

	FacetPage<Product> getFacetedProductsCategory();

	Page<Product> getProductsPaged(Pageable page);

	List<Product> getProductsWithUserQuery(String userQuery);

	List<Product> getProductsByCategory(String category);

	FacetPage<Product> autocompleteNameFragment(String fragment, Pageable pageable);

	HighlightPage<Product> findByHighlightedNameCriteria(String searchTerm);

	HighlightPage<Product> findByHighlightedName(String searchTerm, Pageable pageable);

	List<Product> getProductsByLocation(String LatLng)  throws GeoLocationException;
	
}
