package com.nixmash.springdata.mvc.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nixmash.springdata.solr.common.SolrUtils;
import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.model.ProductDTO;
import com.nixmash.springdata.solr.service.ProductService;

@Controller
public class SolrController {

	private final ProductService productService;

	private static final Logger logger = LoggerFactory.getLogger(SolrController.class);

	protected static final String MODEL_ATTRIBUTE_PRODUCTS = "products";
	private static final String MODEL_ATTRIBUTE_PRODUCT = "product";

	protected static final String PRODUCT_LIST_VIEW = "products/list";
	private static final String PRODUCT_VIEW = "products/view";

	@Autowired
	public SolrController(ProductService productService) {
		this.productService = productService;
	}

	@ModelAttribute(MODEL_ATTRIBUTE_PRODUCTS)
	public List<ProductDTO> allProducts() {
		List<Product> products = productService.getProducts();
		return createDTOs(products);
	}

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String showProductsPage() {
		logger.debug("Showing all products page. Found {} products", allProducts().size());
		return PRODUCT_LIST_VIEW;
	}

	@RequestMapping(value = "/products/{id}", method = GET)
	public String productPage(@PathVariable("id") String id, Model model)  {
		logger.info("Showing product page for product with id: {}", id);

		Product found = productService.getProduct(id);
		logger.info("Found product: {}", found);

		model.addAttribute(MODEL_ATTRIBUTE_PRODUCT, found);
		return PRODUCT_VIEW;
	}
	
	private List<ProductDTO> createDTOs(List<Product> products) {
		List<ProductDTO> dtos = new ArrayList<ProductDTO>();
		for (Product product : products) {
			dtos.add(SolrUtils.productToProductDTO(product));
		}
		return dtos;
	}

}
