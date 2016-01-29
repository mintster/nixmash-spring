package com.nixmash.springdata.solr.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.geo.GeoConverters;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.nixmash.springdata.solr.enums.SolrDocType;
import com.nixmash.springdata.solr.exceptions.GeoLocationException;
import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.repository.custom.CustomProductRepository;
import com.nixmash.springdata.solr.repository.simple.SimpleProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	private static final Pattern IGNORED_CHARS_PATTERN = Pattern.compile("\\p{Punct}");

	@Resource
	SimpleProductRepository simpleProductRepo;

	@Resource
	CustomProductRepository productRepo;

	@Override
	public List<Product> getAvailableProducts() {
		logger.info("Retrieving all available products where inStock:true");
		List<Product> products = productRepo.findByAvailableTrueAndDoctype(SolrDocType.PRODUCT);
		return products;
	}

	@Override
	public List<Product> getProductsByLocation(String LatLng) throws GeoLocationException {
		List<Product> found;
		try {
			Point point = GeoConverters.StringToPointConverter.INSTANCE.convert(LatLng);
			found = 
					productRepo.findByLocationNear(new Point(point.getX(), point.getY()), new Distance(30));
		} catch (Exception e) {
			logger.info("No location found with coordinates: {}", LatLng);
			throw new GeoLocationException("Error in mapping latLng: " + LatLng);
		}
		return found;
	}

	@Override
	public HighlightPage<Product> findByHighlightedName(String searchTerm, Pageable pageable) {
		return productRepo.findByNameIn(splitSearchTermAndRemoveIgnoredCharacters(searchTerm), pageable);
	}

	@Override
	public HighlightPage<Product> findByHighlightedNameCriteria(String searchTerm) {
		return productRepo.searchProductsWithHighlights(searchTerm);
	}

	@Override
	public FacetPage<Product> getFacetedProductsAvailable() {
		logger.info("Retrieving faceted products by available");
		return simpleProductRepo.findByFacetOnAvailable();
	}

	@Override
	public FacetPage<Product> getFacetedProductsCategory() {
		logger.info("Retrieving faceted products by category");
		return productRepo.findProductCategoryFacets(new PageRequest(0, 20));
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		logger.info("Retrieving products by category: {}", category);
		return productRepo.findByCategory(category);
	}

	@Override
	public Iterable<Product> getAllRecords() {
		logger.info("Retrieving all records in index");
		return simpleProductRepo.findAll();
	}

	@Override
	public Page<Product> getTestRecords() {
		return productRepo.findTestCategoryRecords();
	}

	@Override
	public List<Product> getProductsByFilter() {
		logger.info("Retrieving all records and filtering out by 'doctype:product'");
		List<Product> products = Lists.newArrayList(productRepo.findAll());
		return products.stream().filter(p -> p.getDoctype().equals(SolrDocType.PRODUCT)).collect(Collectors.toList());
	}

	@Override
	public List<Product> getProducts() {
		logger.info("Retrieving all products by solr @Query");
		return productRepo.findAllProducts();
	}

	@Override
	public Page<Product> getProductsPaged(Pageable page) {
		logger.info("Retrieving all products by solr @Query");
		return productRepo.findAllProductsPaged(page);
	}

	@Override
	public List<Product> getProductsByStartOfName(String nameStart) {
		logger.info("Named Method Query -  findByNameStartingWith()");
		return productRepo.findByNameStartingWith(nameStart);
	}

	@Override
	public List<Product> getProductsWithUserQuery(String userQuery) {
		logger.info("SimpleQuery from user search string -  findProductsBySimpleQuery()");
		return productRepo.findProductsBySimpleQuery(userQuery);
	}

	@Override
	public Iterable<Product> getProductsByNameOrCategory(String searchTerm) {
		logger.info("Using 'Product.findByNameOrCategory' named query - ('name:*?0* OR cat:*?0*')");
		return productRepo.findByNameOrCategory(searchTerm, sortByIdDesc());
	}

	@Override
	public List<Product> getProductsByNameOrCategoryAnnotatedQuery(String searchTerm) {
		logger.info("Using annotated @query  - ('(name:*?0* OR cat:*?0*) AND doctype:product'");
		return productRepo.findByAnnotatedQuery(searchTerm, sortByIdDesc());
	}

	@Override
	public Page<Product> getProductsByPopularity(int popularity) {
		logger.info("Using JPA Method Name Query - findByPopularityGreaterThanEqual()");
		return productRepo.findByPopularityGreaterThanEqual(popularity, new PageRequest(0, 10));
	}

	@Override
	public Product getProduct(String Id) {
		return productRepo.findOne(Id);
	}

	@Override
	public void updateProductName(Product product) {
		productRepo.updateProductName(product);
	}

	@Override
	public List<Product> searchWithCriteria(String searchTerm) {
		return productRepo.searchWithCriteria(searchTerm);
	}

	public Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Product.ID_FIELD);
	}

	@Override
	public FacetPage<Product> autocompleteNameFragment(String fragment, Pageable pageable) {
		if (StringUtils.isBlank(fragment)) {
			return new SolrResultPage<Product>(Collections.<Product> emptyList());
		}
		return productRepo.findByNameStartingWith(splitSearchTermAndRemoveIgnoredCharacters(fragment), pageable);
	}

	private Collection<String> splitSearchTermAndRemoveIgnoredCharacters(String searchTerm) {
		String[] searchTerms = StringUtils.split(searchTerm, " ");
		List<String> result = new ArrayList<String>(searchTerms.length);
		for (String term : searchTerms) {
			if (StringUtils.isNotEmpty(term)) {
				result.add(IGNORED_CHARS_PATTERN.matcher(term).replaceAll(" "));
			}
		}
		return result;
	}

}
