/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.PaymentDAO;
import dtos.PaymentDTO;
import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
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
public class ProductNameAndShoppingTimeSearchingController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "payment-history.jsp";

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
        List<PaymentDTO> paymentHistory = null;
        List<PaymentDTO> searchedPaymentHistoryDetail = null;
        List<PaymentDTO> searchedPaymentHistory = null;
        String searchedProductName = null;
        String searchedStartingShoppingTime = null;
        String searchedEndingShoppingTime = null;
        String email = null;

        try {
            PaymentDAO paymentDAO = new PaymentDAO();
            email = request.getSession(false).getAttribute("EMAIL").toString();
            searchedProductName = request.getParameter("searchedProductName");
            searchedStartingShoppingTime = request.getParameter("searchedStartingShoppingTime");
            searchedEndingShoppingTime = request.getParameter("searchedEndingShoppingTime");

            if (!searchedProductName.equals("") && (searchedStartingShoppingTime.equals("") && searchedEndingShoppingTime.equals(""))) {
                paymentHistory = paymentDAO.getPaymentHistory(email);
                if (paymentHistory != null) {
                    searchedPaymentHistoryDetail = paymentDAO.getPaymentHistoryDetailByProductName(email, searchedProductName);
                    if (searchedPaymentHistoryDetail != null) {
                        searchedPaymentHistory = new ArrayList<>();
                        for (PaymentDTO item : paymentHistory) {
                            for (PaymentDTO it : searchedPaymentHistoryDetail) {
                                if (item.getSaleID() == it.getSaleID()) {
                                    searchedPaymentHistory.add(item);
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (searchedProductName.equals("") && (!searchedStartingShoppingTime.equals("") && !searchedEndingShoppingTime.equals(""))) {
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date startingDate = formatter.parse(searchedStartingShoppingTime);
                Timestamp parsedStartingShoppingTime = new Timestamp(startingDate.getTime());
                Date endingDate = formatter.parse(searchedEndingShoppingTime);
                Timestamp parsedEndingShoppingTime = new Timestamp(endingDate.getTime());

                searchedPaymentHistory = paymentDAO.getPaymentHistoryByShoppingTime(email, parsedStartingShoppingTime, parsedEndingShoppingTime);
                if (searchedPaymentHistory != null) {
                    searchedPaymentHistoryDetail = paymentDAO.getPaymentHistoryDetailByShoppingTime(email, parsedStartingShoppingTime, parsedEndingShoppingTime);
                }
            } else if (!searchedProductName.equals("") && (!searchedStartingShoppingTime.equals("") && !searchedEndingShoppingTime.equals(""))) {
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date startingDate = formatter.parse(searchedStartingShoppingTime);
                Timestamp parsedStartingShoppingTime = new Timestamp(startingDate.getTime());
                Date endingDate = formatter.parse(searchedEndingShoppingTime);
                Timestamp parsedEndingShoppingTime = new Timestamp(endingDate.getTime());

                paymentHistory = paymentDAO.getPaymentHistoryByShoppingTime(email, parsedStartingShoppingTime, parsedEndingShoppingTime);
                if (paymentHistory != null) {
                    searchedPaymentHistoryDetail = paymentDAO.getPaymentHistoryDetailByProductNameAndShoppingTime(email, searchedProductName, parsedStartingShoppingTime, parsedEndingShoppingTime);
                    searchedPaymentHistory = new ArrayList<>();
                    for (PaymentDTO item : paymentHistory) {
                        for (PaymentDTO it : searchedPaymentHistoryDetail) {
                            if (item.getSaleID() == it.getSaleID()) {
                                searchedPaymentHistory.add(item);
                                break;
                            }
                        }
                    }
                }
            }

            url = SUCCESS;
            request.setAttribute("PaymentHistory", searchedPaymentHistory);
            request.setAttribute("PaymentHistoryDetail", searchedPaymentHistoryDetail);
        } catch (Exception e) {
            log("ERROR at ProductNameAndShoppingTimeSearchingController: " + e.getMessage());
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
