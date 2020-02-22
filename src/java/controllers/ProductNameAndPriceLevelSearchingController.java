/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

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
public class ProductNameAndPriceLevelSearchingController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String ADMIN = "admin.jsp";
    private static final String INDEX = "index.jsp";

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
        String pg = request.getParameter("pg");
        int numOfProductsPerPage = 20;
        String url = ERROR;
        String searchedProductName = request.getParameter("searchedProductName").trim();
        String searchedPriceLevel = request.getParameter("searchedPriceLevel");

        try {
            ProductDAO productDAO = new ProductDAO();
            PagingHandler pagingHandler = new PagingHandler();

            int productsTotal = productDAO.getSearchedProductsTotalForAdminPage(searchedProductName, searchedPriceLevel);
            int page = pagingHandler.getPage(pg);
            int totalPage = pagingHandler.getTotalPage(pg, productsTotal, numOfProductsPerPage);

            List<ProductDTO> productsData = productDAO.getSearchedProductsDataForAdminPage(searchedProductName, searchedPriceLevel, page, numOfProductsPerPage);
            if (productsData != null) {
                url = INDEX;
                if (session.getAttribute("ROLE") != null) {
                    String role = session.getAttribute("ROLE").toString();
                    if (role.equals("Admin")) {
                        url = ADMIN;
                    }
                }
                if (page > 0 && page <= totalPage) {
                    request.setAttribute("TotalPage", totalPage);
                }
                request.setAttribute("ProductsData", productsData);
            } else {
                request.setAttribute("SearchError", "Search Products Failed");
            }
        } catch (Exception e) {
            log("ERROR at SearchController: " + e.getMessage());
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
