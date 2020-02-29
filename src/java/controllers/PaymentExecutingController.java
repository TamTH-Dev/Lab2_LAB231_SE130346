/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import cart.Cart;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import daos.PaymentDAO;
import daos.ProductDAO;
import dtos.ProductDTO;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import supportMethods.PaymentServices;

/**
 *
 * @author hoang
 */
public class PaymentExecutingController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "payment-receipt.jsp";

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
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");

        try {
            PaymentServices paymentServices = new PaymentServices();
            Payment payment = paymentServices.executePayment(paymentId, payerId);

            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);

            PaymentDAO paymentDAO = new PaymentDAO();
            String email = (String) request.getSession(false).getAttribute("EMAIL");
            double billPriceTotal = Double.parseDouble(transaction.getAmount().getTotal());
            Timestamp buyTime = new Timestamp(System.currentTimeMillis());

            try {
                if (paymentDAO.recordUserOrder(email, buyTime, "paypal", billPriceTotal)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    int saleID = paymentDAO.getSaleID(email, sdf.format(buyTime));
                    Cart cart = (Cart) request.getSession(false).getAttribute("CART");

                    List<ProductDTO> productList = cart.getCart();
                    if (paymentDAO.recordUserOrderDetail(saleID, productList)) {
                        ProductDAO productDAO = new ProductDAO();
                        boolean isSuccess = true;
                        for (ProductDTO product : productList) {
                            int quantity = product.getQuantity();
                            String productName = product.getProductName();
                            if (!productDAO.updateBuyedProductQuantity(productName, quantity)) {
                                isSuccess = false;
                            }
                        }
                        if (isSuccess) {
                            cart.removeAllProductsFromCart();
                            url = SUCCESS;
                            request.getSession(false).setAttribute("CART", cart);
                            request.setAttribute("payer", payerInfo);
                            request.setAttribute("transaction", transaction);
                        } else {
                            request.setAttribute("ERROR", "Execute Paying Failed");
                        }
                    }
                }
            } catch (Exception ex) {
                log("ERROR at PaymentExecutingController: " + ex.getMessage());
            }
        } catch (PayPalRESTException ex) {
            request.setAttribute("ERROR", ex.getMessage());
            log("ERROR at PaymentExecutingController: " + ex.getMessage());
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
