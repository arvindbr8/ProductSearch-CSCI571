package com.bright.productsearch;


public class SearchResultCard {


        private String productImageURL;
    private String productTitle;
    private String zipcode;
    private String shipping;
    private String cart;
    private String condition;
    private String price;

    public SearchResultCard(String productImageURL, String productTitle, String zipcode, String shipping, String cart, String condition, String price) {
        this.productImageURL = productImageURL;
        this.productTitle = productTitle;
        this.zipcode = zipcode;
        this.shipping = shipping;
        this.cart = cart;
        this.condition = condition;
        this.price = price;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getShipping() {
        return shipping;
    }

    public String getCart() {
        return cart;
    }

    public String getCondition() {
        return condition;
    }

    public String getPrice() {
        return price;
    }
}
