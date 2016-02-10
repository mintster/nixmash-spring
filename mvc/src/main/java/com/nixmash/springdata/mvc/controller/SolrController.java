package com.nixmash.springdata.mvc.controller;

import static java.lang.Math.toIntExact;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nixmash.springdata.mvc.containers.Pager;
import com.nixmash.springdata.mvc.containers.ProductCategory;
import com.nixmash.springdata.mvc.containers.UserQuery;
import com.nixmash.springdata.solr.common.SolrUtils;
import com.nixmash.springdata.solr.exceptions.GeoLocationException;
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
	private static final String MODEL_ATTRIBUTE_PAGER = "pager";
	private static final String MODEL_ATTRIBUTE_PRODUCT_CATEGORIES = "productCategories";
	private static final String MODEL_ATTRIBUTE_CATEGORY = "category";

	private static final int PRODUCT_LIST_PAGE_SIZE = 5;
	private static final String PRODUCT_LIST_BASEURL = "/products/page/";

	private static final String PRODUCT_LIST_VIEW = "products/list";
	private static final String PRODUCT_SEARCH_VIEW = "products/search";
	private static final String PRODUCT_CATEGORIES_VIEW = "products/categories";
	private static final String PRODUCT_VIEW = "products/view";
	private static final String PRODUCTS_BYCATEGORY_VIEW = "products/category";
	private static final String PRODUCT_MAP_VIEW = "products/map";

	private static final String SESSION_ATTRIBUTE_PRODUCTLIST = "productList";

	private static final String LOCATION_ATTRIBUTE_NAME = "location";

	private List<Product> locationProducts;

	@Autowired
	public SolrController(ProductService productService) {
		this.productService = productService;
	}

	@RequestMapping(value = "/products/json", method = RequestMethod.GET)
	public @ResponseBody List<Product> getProductsByLocation() {
		return locationProducts;
	}

	@RequestMapping(value = "/products/map/bad", method = GET)
	public ModelAndView badLocationMap(HttpServletRequest request) throws GeoLocationException {
		return productMap(request, "35.453487-97.5184727");
	}

	@RequestMapping(value = "/products/map", method = GET)
	public ModelAndView goodLocationMap(HttpServletRequest request) throws GeoLocationException {
		return productMap(request, "35.453487,-97.5184727");
	}

	public ModelAndView productMap(HttpServletRequest request, String location) throws GeoLocationException {

		request.setAttribute("location", location);
		ModelAndView mav = new ModelAndView();
		mav.addObject(LOCATION_ATTRIBUTE_NAME, location);

		locationProducts = productService.getProductsByLocation(location);
		logger.info("Found {} products for location: {}", locationProducts.size(), location);

		mav.setViewName(PRODUCT_MAP_VIEW);
		return mav;
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

	@RequestMapping(value = "/products/categories", method = RequestMethod.GET)
	public String productCategories(Model model) {

		FacetPage<Product> catfacetPage = productService.getFacetedProductsCategory();
		Page<FacetFieldEntry> catPage = catfacetPage.getFacetResultPage(Product.CATEGORY_FIELD);

		List<ProductCategory> results = new ArrayList<ProductCategory>();
		for (FacetFieldEntry entry : catPage) {
			results.add(new ProductCategory(entry.getValue(), toIntExact(entry.getValueCount())));
		}

		model.addAttribute(MODEL_ATTRIBUTE_PRODUCT_CATEGORIES, results);
		return PRODUCT_CATEGORIES_VIEW;
	}

	@RequestMapping(value = "/products/categories/{category}", method = GET)
	public String productByCategory(@PathVariable("category") String category, Model model) {
		logger.info("Showing product page for category: {}", category);

		List<Product> found = productService.getProductsByCategory(category);
		logger.info("Found {} products for category: {}", found.size(), category);

		model.addAttribute(MODEL_ATTRIBUTE_CATEGORY, StringUtils.capitalize(category));
		model.addAttribute(MODEL_ATTRIBUTE_PRODUCTS, found);
		return PRODUCTS_BYCATEGORY_VIEW;
	}

	@RequestMapping(value = "/products/list", method = RequestMethod.GET)
	public String processFindForm(UserQuery userQuery, BindingResult result, Model model, HttpServletRequest request) {
		List<Product> results = null;

		Boolean isSimpleTermQuery = userQuery.getQuery().matches("[a-zA-Z_0-9 ]*");

		if (StringUtils.isEmpty(userQuery.getQuery())) {
			return "redirect:/products/search";
		} else
			try {
				if (isSimpleTermQuery) {
					HighlightPage<Product> highlightedResults = productService
							.findByHighlightedNameCriteria(userQuery.getQuery());
					results = SolrUtils.highlightPagesToList(highlightedResults);
				} else {
					results = productService.getProductsWithUserQuery(userQuery.getQuery());
				}
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

	@ResponseBody
	@RequestMapping(value = "/products/autocomplete", produces = "application/json")
	public Set<String> autoComplete(Model model, @RequestParam("term") String query) {
		if (StringUtils.isBlank(query)) {
			return Collections.emptySet();
		}

		PageRequest pageRequest = new PageRequest(0, 1);
		FacetPage<Product> result = productService.autocompleteNameFragment(query, pageRequest);

		Set<String> titles = new LinkedHashSet<String>();
		for (Page<FacetFieldEntry> page : result.getFacetResultPages()) {
			for (FacetFieldEntry entry : page) {
				if (entry.getValue().contains(query)) {
					titles.add(entry.getValue());
				}
			}
		}

		// To display complete Product Name field in dropdown ----------------------------------- */
		//
		// List<Product> result = productService.getProductsByStartOfName(query);
		//
		// Set<String> titles = new LinkedHashSet<String>();
		// for (Product product : result) {
		// if (product.getName().toLowerCase().contains(query.toLowerCase())) {
		// titles.add(product.getName());
		// }
		// }

		return titles;
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
