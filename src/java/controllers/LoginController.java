/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.GeneralSecurityException;
import java.util.Collections;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import daos.AccountDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import supportMethods.SHA_256;

/**
 *
 * @author hoang
 */
public class LoginController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String ADMIN = "admin.jsp";
    private static final String USER = "index.jsp";
    private static final String INVALID = "login.jsp";
    private static final String CLIENT_ID = "1091231205111-1go7q8tg3q4h7vgs7lkp530hs31lr3dd.apps.googleusercontent.com";

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
        String idToken = request.getParameter("id_token");
        HttpSession session = request.getSession(true);
        AccountDAO accountDAO = new AccountDAO();
        String url = ERROR;
        String email = null;
        String password = null;
        String name = null;
        String role = null;

        try {
            if (idToken != null) {
                JacksonFactory jacksonFactory = new JacksonFactory();
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jacksonFactory)
                        .setAudience(Collections.singletonList(CLIENT_ID))
                        .build();
                try {
                    GoogleIdToken accessToken = verifier.verify(idToken);
                    if (accessToken != null) {
                        Payload payload = accessToken.getPayload();
                        email = payload.getEmail();
                        name = (String) payload.get("name");
                        try {
                            if (!accountDAO.checkDuplicate(email)) {
                                accountDAO.handleLoginByGmail(email, name);
                            }
                            role = "User";
                            url = USER;
                        } catch (Exception ex) {
                            log("Error at LoginController: " + ex.getMessage());
                        }
                    } else {
                        log("Error at LoginController: Invalid Token!");
                    }
                } catch (GeneralSecurityException e) {
                    log("Error at LoginController:" + e.getMessage());
                }
            } else {
                try {
                    email = request.getParameter("email");
                    password = request.getParameter("password");

                    SHA_256 sha = new SHA_256();
                    String encodedPassword = sha.getEncodedString(password);
                    role = accountDAO.handleLogin(email, encodedPassword);

                    if (role.equals("failed")) {
                        request.setAttribute("InvalidAccount", "Username or Password is invalid!");
                        url = INVALID;
                    } else {
                        name = accountDAO.getLoginName(email, encodedPassword);

                        switch (role) {
                            case "Admin":
                                url = ADMIN;
                                break;
                            case "User":
                                url = USER;
                                break;
                            default:
                                request.setAttribute("ERROR", "Your role is invalid");
                                break;
                        }
                    }
                } catch (Exception e) {
                    log("Error at LoginController:" + e.getMessage());
                }
            }
            session.setAttribute("EMAIL", email);
            session.setAttribute("NAME", name);
            session.setAttribute("ROLE", role);
        } catch (IOException e) {
            log("ERROR at LoginController: " + e.getMessage());
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
