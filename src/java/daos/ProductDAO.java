/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

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
}
