package com.nixmash.springdata.mvc.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nixmash.springdata.mvc.containers.Pager;
import com.nixmash.springdata.mvc.containers.UserQuery;
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
	private static final String MODEL_ATTRIBUTE_USERQUERY = "userQuery";

	private static final int PRODUCT_LIST_PAGE_SIZE = 5;
	private static final String PRODUCT_LIST_BASEURL = "/products/page/";

	private static final String PRODUCT_LIST_VIEW = "products/list";
	private static final String PRODUCT_SEARCH_VIEW = "products/search";
	private static final String PRODUCT_VIEW = "products/view";

	private static final String MODEL_ATTRIBUTE_PAGER = "pager";

	private static final String SESSION_ATTRIBUTE_PRODUCTLIST = "productList";

	@Autowired
	public SolrController(ProductService productService) {
		this.productService = productService;
	}

	@RequestMapping(value = "/products")
	public String productsRedirect(HttpServletRequest request) {
		request.getSession().setAttribute(SESSION_ATTRIBUTE_PRODUCTLIST, null);
		return "redirect:/products/page/1";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/products/page/{pageNumber}", method = RequestMethod.GET)
	public String pagedProductsPage(HttpServletRequest request, @PathVariable Integer pageNumber, Model uiModel) {

		logger.info("Showing paged products page # {}", pageNumber);
		PagedListHolder<Product> pagedListHolder = (PagedListHolder<Product>) request.getSession()
				.getAttribute(SESSION_ATTRIBUTE_PRODUCTLIST);

		if (pagedListHolder == null) {
			pagedListHolder = new PagedListHolder<Product>(productService.getProducts());
			pagedListHolder.setPageSize(PRODUCT_LIST_PAGE_SIZE);

		} else {

			final int goToPage = pageNumber - 1;
			if (goToPage <= pagedListHolder.getPageCount() && goToPage >= 0) {
				pagedListHolder.setPage(goToPage);
			}
		}

		request.getSession().setAttribute(SESSION_ATTRIBUTE_PRODUCTLIST, pagedListHolder);

		uiModel.addAttribute(MODEL_ATTRIBUTE_PAGER, currentPage(pagedListHolder));
		uiModel.addAttribute(MODEL_ATTRIBUTE_PRODUCTS, pagedListHolder);

		return PRODUCT_LIST_VIEW;
	}

	@RequestMapping(value = "/products/search", method = GET)
	public String productSearch(Model model) {
		model.addAttribute(MODEL_ATTRIBUTE_USERQUERY, new UserQuery());
		return PRODUCT_SEARCH_VIEW;
	}

	@RequestMapping(value = "/products/list", method = RequestMethod.GET)
	public String processFindForm(UserQuery userQuery, BindingResult result, Model model, HttpServletRequest request) {
		List<Product> results = null;

		if (StringUtils.isEmpty(userQuery.getQuery())) {
			return "redirect:/products/search";
		} else
			try {
				results = this.productService.getProductsWithUserQuery(userQuery.getQuery());
			} catch (UncategorizedSolrException ex) {
				logger.info(MessageFormat.format("Bad Query: {0}", userQuery.getQuery()));
				result.rejectValue("query", "product.search.error", new Object[] { userQuery.getQuery() }, "not found");
				return PRODUCT_SEARCH_VIEW;
			}

		if (results.size() < 1) {
			result.rejectValue("query", "product.search.noresults", new Object[] { userQuery.getQuery() }, "not found");
			return PRODUCT_SEARCH_VIEW;
		}

		if (results.size() > 1) {
			PagedListHolder<Product> pagedListHolder = new PagedListHolder<Product>(results);
			pagedListHolder.setPageSize(PRODUCT_LIST_PAGE_SIZE);
			request.getSession().setAttribute(SESSION_ATTRIBUTE_PRODUCTLIST, pagedListHolder);
			return "redirect:/products/page/1";
		} else {
			Product product = results.iterator().next();
			return "redirect:/products/" + product.getId();
		}
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

	private Pager currentPage(PagedListHolder<?> pagedListHolder) {
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
		return pager;
	}
}
