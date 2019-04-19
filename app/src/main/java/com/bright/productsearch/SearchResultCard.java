package com.bright.productsearch;


import org.json.JSONObject;

public class SearchResultCard {


    private String productImageURL;
    private String productTitle;
    private String zipcode;
    private String shipping;
    private String cart;
    private String condition;
    private String price;
    private String Id;
    private JSONObject ShippingDetail;

    public SearchResultCard(String productImageURL, String productTitle, String zipcode, String shipping, String cart, String condition, String price, String id, JSONObject shippingDetail) {
        this.productImageURL = productImageURL;
        this.productTitle = productTitle;
        this.zipcode = zipcode;
        this.shipping = shipping;
        this.cart = cart;
        this.condition = condition;
        this.price = price;
        Id = id;
        ShippingDetail = shippingDetail;
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

    public String getId() {
        return Id;
    }

    public JSONObject getShippingDetail() {
        return ShippingDetail;
    }
}
