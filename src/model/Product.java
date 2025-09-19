package model;

public class Product {
    private String flowerID;
    private String flowerName;
    private String type;
    private int price;

    public Product(String flowerID, String flowerName, String type, int price) {
        this.flowerID = flowerID;
        this.flowerName = flowerName;
        this.type = type;
        this.price = price;
    }

    public String getFlowerID() {
        return flowerID;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }
}