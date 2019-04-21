package com.bright.productsearch;

public class SimilarItemCard {

    private String imageURL;
    private String productTitle;
    private String Shipping;
    private String daysLeft;
    private String price;
    private String URL;

    public SimilarItemCard(String imageURL, String productTitle, String shipping, String daysLeft, String price, String URL) {
        this.imageURL = imageURL;
        this.productTitle = productTitle;
        Shipping = shipping;
        this.daysLeft = daysLeft;
        this.price = price;
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public float getPriceNumeric() {
        return Float.parseFloat(price);
    }

    public int getDaysNumeric() {
        return Integer.parseInt(daysLeft);
    }

    public String getShipping() {
        if (Shipping.equals("0.00"))
            return "Free Shipping";
        else
            return "$" + Shipping;
    }

    public String getDaysLeft() {
        return daysLeft + " Days Left";
    }

    public String getPrice() {
        return "$" + price;
    }
}
