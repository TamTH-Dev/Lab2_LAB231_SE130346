package dtos;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hoang
 */
public class ProductDTO implements Serializable {

    private String productName, imgPath, description, category, createdTime, status;
    private int quantity;
    private double price;

    public ProductDTO() {
    }

    public ProductDTO(String productName, String imgPath, String description, int quantity, double price, String category, String createdTime, String status) {
        this.productName = productName;
        this.imgPath = imgPath;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.createdTime = createdTime;
        this.status = status;
    }

    public ProductDTO(String productName, int quantity, double price, String category, String imgPath) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.imgPath = imgPath;
    }

    public ProductDTO(String description, int quantity, double price, String category) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
