/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ProductDAO;
import java.io.IOException;
import java.sql.Timestamp;
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
public class ProductsDeletingController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "DataLoadingController";
    private static final String INVALID = "DataLoadingController";

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
            ProductDAO productDAO = new ProductDAO();
            String[] slProducts = request.getParameterValues("selectedProducts");

            if (slProducts != null) {
                List<String> selectedBlogs = new ArrayList<>();
                int slProductsLength = slProducts.length;
                for (int i = 0; i < slProductsLength; i++) {
                    selectedBlogs.add((slProducts[i]));
                }

                boolean isSuccess = productDAO.deleteSelectedProducts(selectedBlogs);
                if (isSuccess) {
                    Timestamp deleteTime = new Timestamp(System.currentTimeMillis());
                    productDAO.recordDeletedProducts(selectedBlogs, deleteTime);
                    url = SUCCESS;
                } else {
                    request.setAttribute("ERROR", "Delete Selected Products Failed!");
                }
            } else {
                url = INVALID;
                request.setAttribute("DeleteError", "Select at least 1 blog to delete!");
            }
        } catch (Exception e) {
            log("ERROR at ProductsDeletingController: " + e.getMessage());
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
