package com.nixmash.springdata.solr;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nixmash.springdata.solr.model.Product;
import com.nixmash.springdata.solr.repository.custom.CustomProductRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class SolrLocationTests extends SolrContext {

	@Autowired
	SolrOperations solrOperations;
	
	private Product locatedInYonkers;
	private Product locatedInBedfordFalls;

	@Before
	public void setup() {
		locatedInYonkers = SolrTestUtils.createProduct(1000);
		locatedInYonkers.setLocation("22.17614,-90.87341");

		locatedInBedfordFalls = SolrTestUtils.createProduct(1001);
		locatedInBedfordFalls.setLocation("40.7143,-74.006");

		repo.save(Arrays.asList(locatedInYonkers, locatedInBedfordFalls));
	}

	@After
	public void tearDown() {
		Query query = new SimpleQuery(new SimpleStringCriteria("cat:test"));
		solrOperations.delete(query);
		solrOperations.commit();
	}

	@Resource
	CustomProductRepository repo;

	@Test
	public void missingLocationsProduceNegativePointValues() {
		Product product = SolrTestUtils.createProduct(1002);
		repo.save(product);
		assertNull(product.getLocation());
		assertThat(product.getPoint().getX(), is(lessThan((double) 0)));
	}

	@Test
	public void testFindByLocationWithin() {
		List<Product> found = 
				repo.findByLocationWithin(new Point(22.15, -90.85), new Distance(5));
		locationAsserts(found);
	}

	@Test
	public void testFindByNear() {
		List<Product> found = 
				repo.findByLocationNear(new Point(22.15, -90.85), new Distance(5));
		locationAsserts(found);
	}
	
	@Test
	public void testFindByAnnotatedQueryNear() {
		List<Product> found = 
				repo.findByLocationSomewhereNear(new Point(22.15, -90.85), new Distance(5));
		locationAsserts(found);
	}

	@Test
	public void testFindByLocationCriteria() {
		Point location = new Point(22.15, -90.85);
		Criteria criteria = new Criteria("store").near(location, new Distance(5));
		Page<Product> result = 
				solrOperations.queryForPage(new SimpleQuery(criteria), Product.class);
		
		Assert.assertEquals(1, result.getTotalElements());
		Assert.assertEquals(locatedInYonkers.getId(), result.getContent().get(0).getId());
	}
	
	@Test
	public void testFindByNearWithBox() {
		//  locatedinYonkers location: 22.17614, -90.87341
		List<Product> found = 
				repo.findByLocationNear(new Box(new Point(22, -91), new Point(23, -90)));
		locationAsserts(found);
	}

	private void locationAsserts(List<Product> found) {
		Assert.assertEquals(1, found.size());
		Assert.assertEquals(locatedInYonkers.getId(), found.get(0).getId());
	}
	
	
}
