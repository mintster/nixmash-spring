package com.nixmash.springdata.solr.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.nixmash.springdata.solr.model.IProduct;
import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.model.ProductDTO;

public class SolrUtils {

	public static ProductDTO productToProductDTO(Product product) {
		ProductDTO dto = new ProductDTO();

		dto.setId(product.getId());
		dto.setName(product.getName());
		dto.setWeight(product.getWeight());
		dto.setPrice(product.getPrice());
		dto.setPopularity(product.getPopularity());
		dto.setAvailable(product.isAvailable());
		dto.setDoctype(product.getDoctype());
		dto.setCategories(product.getCategories());
		dto.setLocation(product.getLocation());
		dto.setPoint(product.getPoint());
		return dto;
	}

	public static HighlightPage<Product> processHighlights(HighlightPage<Product> productPage) {
		int i = 0;
		for (HighlightEntry<Product> product : productPage.getHighlighted()) {
			for (Highlight highlight : product.getHighlights()) {
				for (String snippet : highlight.getSnipplets()) {
					if (highlight.getField().getName().equals(IProduct.NAME_FIELD)) {
						productPage.getContent().get(i).setName(snippet);
					}
				}
			}
			i++;
		}
		return productPage;
	}

	public static List<Product> highlightPagesToList(HighlightPage<Product> productPage) { 
		
		List<Product> products = new ArrayList<Product>();
		for (HighlightEntry<Product> highlightedProduct : productPage.getHighlighted()) {
			
			Product product = new 
					Product(highlightedProduct.getEntity().getId(), highlightedProduct.getEntity().getName());
			products.add(product);
			
			for (Highlight highlight : highlightedProduct.getHighlights()) {
				for (String snippet : highlight.getSnipplets()) {
					if (highlight.getField().getName().equals(IProduct.NAME_FIELD)) {
						product.setName(snippet);
					}
				}
			}
		}
		return products;
	}

}
