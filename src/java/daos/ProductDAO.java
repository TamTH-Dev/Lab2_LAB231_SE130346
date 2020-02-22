/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import db.MyConnection;
import dtos.ProductDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author hoang
 */
public class ProductDAO {

    private Connection conn;
    private PreparedStatement preStm;
    private ResultSet rs;

    public ProductDAO() {
    }

    private void closeConnection() throws Exception {
        if (rs != null) {
            rs.close();
        }
        if (preStm != null) {
            preStm.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

    public boolean createProduct(String productName, String imgPath, String description, int quantity, double price, String category, Timestamp createdTime) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "insert into Product(ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status) values(?, ?, ?, ?, ?, ?, ?, 'Active')";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, productName);
            preStm.setString(2, imgPath);
            preStm.setString(3, description);
            preStm.setInt(4, quantity);
            preStm.setDouble(5, price);
            preStm.setString(6, category);
            preStm.setTimestamp(7, createdTime);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public List<ProductDTO> getAllProductsForAdminPage(int page, int numOfbBlogsPerPage) throws Exception {
        List<ProductDTO> productsList = null;

        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status, ROW_NUMBER() over (order by CreatedTime desc) as rowNum from Product) as product where product.rowNum between ? and ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setInt(1, (page - 1) * numOfbBlogsPerPage + 1);
            preStm.setInt(2, (page - 1) * numOfbBlogsPerPage + numOfbBlogsPerPage);
            rs = preStm.executeQuery();
            productsList = new ArrayList<>();
            while (rs.next()) {
                String productName = rs.getString("ProductName");
                String imgPath = rs.getString("ImgPath");
                String description = rs.getString("Description");
                int quantity = Integer.parseInt(rs.getString("Quantity"));
                double price = Double.parseDouble(rs.getString("Price"));
                String category = rs.getString("Category");
                Timestamp createdTime = rs.getTimestamp("CreatedTime");
                String status = rs.getString("Status");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createdTime), status);
                productsList.add(product);
            }
        } finally {
            closeConnection();
        }

        return productsList;
    }

    public int getProductsTotalForAdminPage() throws Exception {
        int total = 0;

        try {
            String sql = "select count(*) from Product";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            rs = preStm.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } finally {
            closeConnection();
        }

        return total;
    }

    public boolean deleteProduct(String productName) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "update Product set Status = 'Inactive' where ProductName = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, productName);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public boolean restoreProduct(String productName) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "update Product set Status = 'Active' where ProductName = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, productName);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public boolean deleteSelectedProducts(List<String> selectedProducts) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "update Product set Status = 'Inactive' where ProductName in (?)";
            String sqlIn = selectedProducts.stream().map(x -> "'" + x + "'").collect(Collectors.joining(",", "(", ")"));
            sql = sql.replace("(?)", sqlIn);
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public boolean recordDeletedProducts(List<String> selectedProducts, Timestamp deleteTime) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "insert into ProductUpdatingRecord(ProductName, UpdateTime, Action) values (?)";
            String sqlIn = selectedProducts.stream().map(x -> "('" + x + "','" + deleteTime + "'," + "'Delete" + "')").collect(Collectors.joining(","));
            sql = sql.replace("(?)", sqlIn);
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public boolean recordUpdatedProduct(String productName, Timestamp updateTime) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "insert into ProductUpdatingRecord(ProductName, UpdateTime, Action) values (?, ?, 'Update')";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, productName);
            preStm.setTimestamp(2, updateTime);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public boolean recordDeletedProduct(String productName, Timestamp deleteTime) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "insert into ProductUpdatingRecord(ProductName, UpdateTime, Action) values (?, ?, 'Delete')";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, productName);
            preStm.setTimestamp(2, deleteTime);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public boolean recordRestoredProduct(String productName, Timestamp restoreTime) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "insert into ProductUpdatingRecord(ProductName, UpdateTime, Action) values (?, ?, 'Restore')";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, productName);
            preStm.setTimestamp(2, restoreTime);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public ProductDTO getProductDetailByProductName(String pdName) throws Exception {
        ProductDTO product = null;

        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status from Product where ProductName = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, pdName);
            rs = preStm.executeQuery();
            if (rs.next()) {
                String productName = rs.getString("ProductName");
                String imgPath = rs.getString("ImgPath");
                String description = rs.getString("Description");
                int quantity = Integer.parseInt(rs.getString("quantity"));
                double price = Double.parseDouble(rs.getString("Price"));
                String category = rs.getString("Category");
                Timestamp createdTime = rs.getTimestamp("CreatedTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                String status = rs.getString("Status");

                product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createdTime), status);
            }
        } finally {
            closeConnection();
        }

        return product;
    }

    public boolean updateProduct(String productName, String imgPath, String description, int quantity, double price, String category) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "update Product set ImgPath = ?, Description = ?, Quantity = ?, Price = ?, Category = ? where ProductName = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, imgPath);
            preStm.setString(2, description);
            preStm.setInt(3, quantity);
            preStm.setDouble(4, price);
            preStm.setString(5, category);
            preStm.setString(6, productName);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }
}
