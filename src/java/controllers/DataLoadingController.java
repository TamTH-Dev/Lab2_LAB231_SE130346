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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import supportMethods.PagingHandler;

/**
 *
 * @author hoang
 */
public class DataLoadingController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String INDEX = "index.jsp";
    private static final String ADMIN = "admin.jsp";

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
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("EMAIL");
        String url = ERROR;
        int signal = 0;

        if (session.getAttribute("CART") == null) {
            Cart cart = new Cart();
            request.getSession(true).setAttribute("CART", cart);
        }

        if (session.getAttribute("ROLE") != null) {
            String role = request.getSession(false).getAttribute("ROLE").toString();
            if (role.equals("Admin")) {
                signal = 1;
            }
        }

        try {
            ProductDAO productDAO = new ProductDAO();
            PagingHandler pagingHandler = new PagingHandler();

            String pg = request.getParameter("pg");
            int numOfBlogsPerPage = 20;
            int page = pagingHandler.getPage(pg);
            List<ProductDTO> productsData = null;
            List<ProductDTO> recommendationProductByUserPreferencesData = null;
            int productsTotal;

            if (signal == 1) {
                productsTotal = productDAO.getProductsTotalForAdminPage();
                productsData = productDAO.getAllProductsForAdminPage(page, numOfBlogsPerPage);
            } else {
                productsTotal = productDAO.getProductsTotalForUserPage();
                productsData = productDAO.getAllProductsForUserPage(page, numOfBlogsPerPage);
                if (email != null) {
                    recommendationProductByUserPreferencesData = productDAO.getRecommendationProductListByUserPreferences(email);
                }
            }

            int totalPage = pagingHandler.getTotalPage(pg, productsTotal, numOfBlogsPerPage);

            if (productsData != null) {
                url = INDEX;
                if (signal == 1) {
                    url = ADMIN;
                }

                if (page > 0 && page <= totalPage) {
                    request.setAttribute("TotalPage", totalPage);
                }
                request.setAttribute("ProductsData", productsData);
                if (email != null) {
                    request.setAttribute("RecommendationProductByUserPreferencesData", recommendationProductByUserPreferencesData);
                }
            } else {
                request.setAttribute("ERROR", "Load Data Failed");
            }
        } catch (Exception e) {
            log("Error at DataLoadingController: " + e.getMessage());
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
