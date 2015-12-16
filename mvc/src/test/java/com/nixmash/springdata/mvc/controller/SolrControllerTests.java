package com.nixmash.springdata.mvc.controller;

import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
public class SolrControllerTests extends AbstractContext {

	private static final String PRODUCT_ID = "LOMAX7";
	private SolrController solrController;
	private MockMvc mockMvc;
	private List<Product> allProducts;
	private Product product;

	@Autowired
	private ProductService mockProductService;

	@Before
	public void setUp() {

		mockProductService = mock(ProductService.class);
		solrController = new SolrController(mockProductService);
		mockMvc = MockMvcBuilders.standaloneSetup(solrController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
		
		product = createProduct(1000);
		when(mockProductService.getProduct(PRODUCT_ID)).thenReturn(product);

		allProducts = createProductList(10);
		when(mockProductService.getProducts()).thenReturn(allProducts);

	}

	// @formatter:off
	
	@Test
	public void testProductsRedirectionPage() throws Exception {
		mockMvc.perform(get("/products"))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	public void getPagedProducts() throws Exception {
		mockMvc.perform(get("/products/page/1"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("products"))
			.andExpect(model().attribute("products",  isA(PagedListHolder.class)));
	}

	@Test
	public void getProductById() throws Exception {
		mockMvc.perform(get(String.format("/products/%s", PRODUCT_ID)))
			.andExpect(view().name("products/view"))
			.andExpect(model().attributeExists("product"))
			.andExpect(model().attribute("product", product));
	}

	// @formatter:on
	
	private Product createProduct(int id) {
		Product product = new Product();
		product.setId(Integer.toString(id));
		product.setAvailable(id % 2 == 0);
		product.setName("product-" + id);
		product.setPopularity(id * 10);
		product.setPrice((float) id * 100);
		product.setWeight((float) id * 2);
		return product;
	}

	private List<Product> createProductList(int nrProducts) {
		List<Product> products = new ArrayList<Product>(nrProducts);
		for (int i = 0; i < nrProducts; i++) {
			products.add(createProduct(i));
		}
		return products;
	}

}