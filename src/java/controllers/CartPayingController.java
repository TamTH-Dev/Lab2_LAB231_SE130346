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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hoang
 */
public class CartPayingController extends HttpServlet {
    
    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "index.jsp";

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
        HttpSession session = request.getSession(false);
        String email = session.getAttribute("EMAIL").toString();
        Cart cart = (Cart) session.getAttribute("CART");
        List<ProductDTO> productsList = cart.getCart();
        String[] quantities = request.getParameterValues("quantity");
        String[] productNames = request.getParameterValues("productName");
        int size = productsList.size();
        
        try {
            ProductDAO productDAO = new ProductDAO();
            
            for (int i = 0; i < size; i++) {
                int quantity = Integer.parseInt(quantities[i]);
                String productName = productNames[i];
                int currentProductQuantity = cart.getCurrentQuantityOfProductFromCart(productName);
                if (currentProductQuantity != quantity) {
                    cart.updateProductQuantityFromCart(productName, quantity);
                }
            }
            
            double priceTotal = cart.getPriceTotal();
            Timestamp buyTime = new Timestamp(System.currentTimeMillis());
            try {
                if (productDAO.recordUserOrder(email, buyTime, "hello", priceTotal)) {
                    url = SUCCESS;                    
                    cart.removeAllProductsFromCart();
                    request.getSession(false).setAttribute("CART", cart);
                } else {
                    request.setAttribute("ERROR", "Execute Paying Failed");
                }
            } catch (Exception ex) {
                log("ERROR at CartPayingController: " + ex.getMessage());
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
