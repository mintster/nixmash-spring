package com.nixmash.springdata.mvc.containers;

public class ProductCategory implements java.io.Serializable {

	private static final long serialVersionUID = 5580569251842603417L;

	public ProductCategory() {}

	public ProductCategory(String category, int productCount) {
		super();
		this.category = category;
		this.productCount = productCount;
	}

	private  String category;
	private  int productCount;

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
}

