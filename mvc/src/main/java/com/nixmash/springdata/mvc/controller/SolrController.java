package com.nixmash.springdata.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nixmash.springdata.solr.common.SolrUtils;
import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.model.ProductDTO;
import com.nixmash.springdata.solr.service.ProductService;

@Controller
public class SolrController {

	private final ProductService productService;
	
    private static final Logger logger = LoggerFactory.getLogger(SolrController.class);

    protected static final String MODEL_ATTRIBUTE_PRODUCTS = "products";
    
    @Autowired
    public SolrController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/solr/products", method = RequestMethod.GET)
    @ResponseBody
    public List<ProductDTO> findAll() {
        logger.debug("Finding all products");

        List<Product> products = productService.getProducts();
        logger.debug("Found {} products", products.size());

        return createDTOs(products);
    }
    
    private List<ProductDTO> createDTOs(List<Product> products) {
        List<ProductDTO> dtos = new ArrayList<ProductDTO>();
        for (Product product: products) {
            dtos.add(SolrUtils.productToProductDTO(product));
        }
        return dtos;
    }

}
