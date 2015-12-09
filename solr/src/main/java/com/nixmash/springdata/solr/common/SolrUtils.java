package com.nixmash.springdata.solr.common;

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
		
		return dto;
	}
}
