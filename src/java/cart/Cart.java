/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cart;

import dtos.ProductDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class Cart {

    List<ProductDTO> cart = null;

    public Cart() {
        cart = new ArrayList<>();
    }

    public List<ProductDTO> getCart() {
        return cart;
    }

    public void addProductToCart(String productName, int quantity, double price, String category, String imgPath) {
        ProductDTO product = new ProductDTO(productName, quantity, price, category, imgPath);
        if (doesProductExistInCart(productName)) {
            updateProductQuantityFromCart(productName, quantity);
        } else {
            cart.add(product);
        }
    }

    public boolean doesProductExistInCart(String productName) {
        boolean doesExist = cart.stream().anyMatch(product -> product.getProductName().equals(productName));
        return doesExist;
    }

    public void removeProductFromCart(String productName) {
        cart.stream().filter(product -> product.getProductName().equals(productName)).forEach((product) -> {
            cart.remove(product);
        });
    }

    public void updateProductQuantityFromCart(String productName, int quantity) {
        cart.stream().filter(product -> product.getProductName().equals(productName)).forEach((product) -> {
            product.setQuantity(quantity);
        });
    }

    public int getCurrentQuantityOfProductFromCart(String productName) {
        int curQuantity = 0;

        cart.stream().filter(product -> product.getProductName().equals(productName)).forEach((product) -> {
            curQuantity = product.getQuantity();
        });

        return curQuantity;
    }
}
