package com.nixmash.springdata.mvc.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nixmash.springdata.mvc.containers.Pager;
import com.nixmash.springdata.solr.common.SolrUtils;
import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.model.ProductDTO;
import com.nixmash.springdata.solr.service.ProductService;

@Controller
public class SolrController {

	private final ProductService productService;

	private static final Logger logger = LoggerFactory.getLogger(SolrController.class);

	private static final String MODEL_ATTRIBUTE_PRODUCTS = "products";
	private static final String MODEL_ATTRIBUTE_PRODUCT = "product";

	private static final int PRODUCT_LIST_PAGE_SIZE = 3;
	private static final String PRODUCT_LIST_BASEURL = "/products/page/";

	private static final String PRODUCT_LIST_VIEW = "products/list";
	private static final String PRODUCT_VIEW = "products/view";

	@Autowired
	public SolrController(ProductService productService) {
		this.productService = productService;
	}

	@RequestMapping(value = "/products")
	public String productsRedirect(HttpServletRequest request) {
		request.getSession().setAttribute("productList", null);
		return "redirect:/products/page/1";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/products/page/{pageNumber}", method = RequestMethod.GET)
	public String pagedProductsPage(HttpServletRequest request, @PathVariable Integer pageNumber, Model uiModel) {

		logger.info("Showing paged products page # {}", pageNumber);
		PagedListHolder<?> pagedListHolder = (PagedListHolder<?>) request.getSession().getAttribute("productList");

		if (pagedListHolder == null) {

			pagedListHolder = new PagedListHolder(productService.getProducts());
			pagedListHolder.setPageSize(PRODUCT_LIST_PAGE_SIZE);

		} else {

			final int goToPage = pageNumber - 1;
			if (goToPage <= pagedListHolder.getPageCount() && goToPage >= 0) {
				pagedListHolder.setPage(goToPage);
			}
		}

		request.getSession().setAttribute("productList", pagedListHolder);

		int currentIndex = pagedListHolder.getPage() + 1;
		int beginIndex = Math.max(1, currentIndex - PRODUCT_LIST_PAGE_SIZE);
		int endIndex = Math.min(beginIndex + 5, pagedListHolder.getPageCount());
		int totalPageCount = pagedListHolder.getPageCount();
		int totalItems = pagedListHolder.getNrOfElements();
		String baseUrl = PRODUCT_LIST_BASEURL;

		Pager pager = new Pager();
		pager.setBeginIndex(beginIndex);
		pager.setEndIndex(endIndex);
		pager.setCurrentIndex(currentIndex);
		pager.setTotalPageCount(totalPageCount);
		pager.setTotalItems(totalItems);
		pager.setBaseUrl(baseUrl);

		uiModel.addAttribute("pager", pager);
		uiModel.addAttribute(MODEL_ATTRIBUTE_PRODUCTS, pagedListHolder);

		return PRODUCT_LIST_VIEW;
	}

	@RequestMapping(value = "/products/{id}", method = GET)
	public String productPage(@PathVariable("id") String id, Model model) {
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
