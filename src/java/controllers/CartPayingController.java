/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import cart.Cart;
import daos.ProductDAO;
import dtos.ProductDTO;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hoang
 */
public class CartPayingController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "index.jsp";
    private static final String AUTHORIZE_PAYMENT = "AuthorizePaymentController";
    private static final String INVALID = "CartDataLoadingController";
    private static final String LOGIN = "login.jsp";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;

        try {
            if (request.getSession(false).getAttribute("EMAIL") == null) {
                url = LOGIN;
            } else {
                Cart cart = (Cart) request.getSession(false).getAttribute("CART");
                List<ProductDTO> productsList = cart.getCart();
                String email = request.getSession(false).getAttribute("EMAIL").toString();
                String[] quantities = request.getParameterValues("quantity");
                String[] productNames = request.getParameterValues("productName");
                String billPriceTotal = request.getParameter("billPriceTotal");
                double parsedBillPriceTotal = -1;
                if (billPriceTotal != null) {
                    parsedBillPriceTotal = Double.parseDouble(billPriceTotal);
                }
                String paymentMethod = request.getParameter("paymentMethod");

                List<ProductDTO> shoppingErrors = new ArrayList<>();
                ProductDAO productDAO = new ProductDAO();
                int size = productsList.size();

                if (billPriceTotal != null) {
                    for (int i = 0; i < size; i++) {
                        int quantity = Integer.parseInt(quantities[i]);
                        String productName = productNames[i];
                        if (cart.getCurrentQuantityOfProductFromCart(productName) != quantity) {
                            cart.updateProductQuantityFromCart(productName, quantity);
                        }
                        try {
                            int currentQuantity = productDAO.getCurrentProductQuantity(productName);
                            if (currentQuantity < quantity) {
                                ProductDTO errorProduct = new ProductDTO(productName, currentQuantity);
                                shoppingErrors.add(errorProduct);
                            }
                        } catch (Exception ex) {
                            log("ERROR at CartPayingController: " + ex.getMessage());
                        }
                    }
                }

                if (shoppingErrors.isEmpty()) {
                    Timestamp buyTime = new Timestamp(System.currentTimeMillis());
                    try {
                        if (paymentMethod.equals("cash")) {
                            if (productDAO.recordUserOrder(email, buyTime, paymentMethod, parsedBillPriceTotal)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS    ");
                                int saleID = productDAO.getSaleID(email, sdf.format(buyTime));
                                if (productDAO.recordUserOrderDetail(saleID, productsList)) {
                                    boolean isSuccess = true;
                                    for (int i = 0; i < size; i++) {
                                        int quantity = Integer.parseInt(quantities[i]);
                                        String productName = productNames[i];
                                        if (!productDAO.updateBuyedProductQuantity(productName, quantity)) {
                                            isSuccess = false;
                                        }
                                    }
                                    if (isSuccess) {
                                        cart.removeAllProductsFromCart();
                                        url = SUCCESS;
                                        request.getSession(false).setAttribute("CART", cart);
                                    } else {
                                        request.setAttribute("ERROR", "Execute Paying Failed");
                                    }
                                }
                            } else {
                                request.setAttribute("ERROR", "Execute Paying Failed");
                            }
                        } else if (paymentMethod.equals("paypal")) {
                            url = AUTHORIZE_PAYMENT;
                        }
                    } catch (Exception ex) {
                        log("ERROR at CartPayingController: " + ex.getMessage());
                    }
                } else {
                    url = INVALID;
                    request.setAttribute("PaymentMethod", paymentMethod);
                    request.setAttribute("ShoppingErrors", shoppingErrors);
                }
            }
        } catch (NumberFormatException e) {
            log("ERROR at CartPayingController: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
