/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ProductDAO;
import dtos.ProductDTO;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import supportMethods.CurrentPathGetting;

/**
 *
 * @author hoang
 */
public class ProductCreatingController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "DataLoadingController";
    private static final String INVALID = "product-creating.jsp";

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

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            ProductDAO productDAO = new ProductDAO();
            String url = ERROR;
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(request);

                Hashtable params = new Hashtable();
                int itemsSize = items.size();

                for (int i = 0; i < itemsSize - 1; i++) {
                    FileItem item = items.get(i);
                    params.put(item.getFieldName(), item.getString());
                }

                String productName = (String) params.get("productName");
                String description = (String) params.get("description");
                String quantity = (String) params.get("quantity");
                int parsedQuantity = Integer.parseInt(quantity);
                String price = (String) params.get("price");
                double parsedPrice = Double.parseDouble(price);
                String category = (String) params.get("category");

                if (!productDAO.isDuplicate(productName)) {
                    Timestamp createdTime = new Timestamp(System.currentTimeMillis());
                    String imgPath = category;
                    String imageName = null;
                    boolean isImageDuplicate = false;

                    CurrentPathGetting currentPath = new CurrentPathGetting();
                    String uploadPath = currentPath.getPath() + "/web/uploads/" + category;
                    uploadPath = uploadPath.replace('\\', '/');
                    try {
                        FileItem imageItem = items.get(itemsSize - 1);
                        imageName = new File(imageItem.getName()).getName();
                        File newImage = new File(uploadPath + File.separator + imageName);
                        if (!newImage.exists()) {
                            imageItem.write(newImage);
                        } else {
                            isImageDuplicate = true;
                        }
                    } catch (Exception e) {
                        log("ERROR at ProductCreatingController: " + e.getMessage());
                    }

                    if (!isImageDuplicate) {
                        imgPath = imgPath + "/" + imageName;

                        boolean isSuccess = productDAO.createProduct(productName, imgPath, description, parsedQuantity, parsedPrice, category, createdTime);
                        if (isSuccess) {
                            url = SUCCESS;
                        } else {
                            request.setAttribute("ERROR", "Create Product Failed");
                        }
                    } else {
                        url = INVALID;
                        ProductDTO productInformation = new ProductDTO(productName, description, parsedQuantity, parsedPrice, category);
                        request.setAttribute("ProductInformation", productInformation);
                        request.setAttribute("DuplicateError", "Image existed. Please rename or choose another image to upload!");
                    }
                } else {
                    url = INVALID;
                    ProductDTO product = new ProductDTO(description, parsedQuantity, parsedPrice, category);
                    request.setAttribute("CreateError", "Product existed!");
                    request.setAttribute("ProductInformation", product);
                }
            } catch (Exception e) {
                log("ERROR at ProductCreatingController: " + e.getMessage());
            } finally {
                request.getRequestDispatcher(url).forward(request, response);
            }
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
