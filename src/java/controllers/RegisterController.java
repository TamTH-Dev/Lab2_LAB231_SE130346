/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.AccountDAO;
import java.io.IOException;
import static javax.servlet.DispatcherType.ERROR;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static jdk.nashorn.tools.Shell.SUCCESS;
import static org.apache.tomcat.dbcp.pool2.PooledObjectState.INVALID;
import supportMethods.SHA_256;

/**
 *
 * @author hoang
 */
public class RegisterController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "index.jsp";
    private static final String INVALID = "register.jsp";
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
            AccountDAO accountDAO = new AccountDAO();
            String email = request.getParameter("email");
            String name = request.getParameter("name");
            String password = request.getParameter("password");

            boolean isDuplicate = accountDAO.checkDuplicate(email);
            if (!isDuplicate) {
                HttpSession session = request.getSession();
                SHA_256 sha = new SHA_256();

                String encodedPassword = sha.getEncodedString(password);
                String role = accountDAO.createAccount(email, name, encodedPassword);
                if (role != null) {
                    session.setAttribute("EMAIL", email);
                    session.setAttribute("NAME", name);
                    session.setAttribute("ROLE", role);
                    url = SUCCESS;
                } else {
                    request.setAttribute("ERROR", "Register Account Failed!");
                }
            } else {
                request.setAttribute("DuplicateError", "The email existed!");
                url = INVALID;
            }
            
        } catch (Exception e) {
            log("Error at RegisterController: " + e.getMessage());
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
