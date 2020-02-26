/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;

/**
 *
 * @author hoang
 */
public class PaymentDTO implements Serializable {

    private String paymentMethod, productName, buyTime;
    private double billPriceTotal, productPriceTotal;
    private int saleID, quantity;

    public PaymentDTO() {
    }

    public PaymentDTO(int saleID, String buyTime, String paymentMethod, double billPriceTotal) {
        this.saleID = saleID;
        this.buyTime = buyTime;
        this.paymentMethod = paymentMethod;
        this.billPriceTotal = billPriceTotal;
    }

    public PaymentDTO(int saleID, String productName, int quantity, double producePriceTotal) {
        this.saleID = saleID;
        this.productName = productName;
        this.quantity = quantity;
        this.productPriceTotal = producePriceTotal;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getBillPriceTotal() {
        return billPriceTotal;
    }

    public void setBillPriceTotal(double billPriceTotal) {
        this.billPriceTotal = billPriceTotal;
    }

    public double getProductPriceTotal() {
        return productPriceTotal;
    }

    public void setProductPriceTotal(double productPriceTotal) {
        this.productPriceTotal = productPriceTotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }
}
