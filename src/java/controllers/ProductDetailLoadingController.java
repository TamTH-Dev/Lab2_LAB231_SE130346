/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ProductDAO;
import dtos.ProductDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hoang
 */
public class ProductDetailLoadingController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String PRODUCTDETAIL = "product-detail.jsp";
    private static final String PRODUCTDETAILMANAGEMENT = "product-detail-management.jsp";

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
        int signal = 0;

        if (session.getAttribute("ROLE") != null) {
            String role = request.getSession(false).getAttribute("ROLE").toString();
            if (role.equals("Admin")) {
                signal = 1;
            }
        }
        try {
            ProductDAO productDAO = new ProductDAO();
            ProductDTO productDetail = null;
            String productName = request.getParameter("productName");

            if (signal == 1) {
                productDetail = productDAO.getProductDetailByProductNameForAdminPage(productName);
            } else {
                productDetail = productDAO.getProductDetailByProductNameForUserPage(productName);
            }

            if (productDetail != null) {
                if (signal == 1) {
                    url = PRODUCTDETAILMANAGEMENT;
                } else {
                    url = PRODUCTDETAIL;
                }
                request.setAttribute("ProductDetail", productDetail);
            } else {
                request.setAttribute("ERROR", "Load Product's Detail Failed!");
            }
        } catch (Exception e) {
            log("ERROR at ProductDetailLoadingController: " + e.getMessage());
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
