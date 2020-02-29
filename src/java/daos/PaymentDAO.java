/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import db.MyConnection;
import dtos.PaymentDTO;
import dtos.ProductDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoang
 */
public class PaymentDAO {

    private Connection conn;
    private PreparedStatement preStm;
    private ResultSet rs;

    public PaymentDAO() {
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

    public List<PaymentDTO> getPaymentHistory(String email) throws Exception {
        List<PaymentDTO> paymentHistoryOfUser = null;

        try {
            String sql = "select SaleID, BuyTime, PaymentMethod, BillPriceTotal from SaleHistory where Email = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, email);
            rs = preStm.executeQuery();
            paymentHistoryOfUser = new ArrayList<>();
            while (rs.next()) {
                int saleID = rs.getInt("SaleID");
                Timestamp buyTime = rs.getTimestamp("BuyTime");
                String paymentMethod = rs.getString("PaymentMethod");
                double billPriceTotal = rs.getDouble("BillPriceTotal");

                PaymentDTO paymentDTO = new PaymentDTO(saleID, buyTime, paymentMethod, billPriceTotal);
                paymentHistoryOfUser.add(paymentDTO);
            }
        } finally {
            closeConnection();
        }

        return paymentHistoryOfUser;
    }

    public List<PaymentDTO> getPaymentHistoryDetail(String email) throws Exception {
        List<PaymentDTO> paymentHistoryDetailOfUser = null;

        try {
            String sql = "select SaleID, ProductName, Quantity, ProductPriceTotal from SaleDetail where SaleID in (select SaleID from SaleHistory where Email = ?)";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, email);
            rs = preStm.executeQuery();
            paymentHistoryDetailOfUser = new ArrayList<>();
            while (rs.next()) {
                int saleID = rs.getInt("SaleID");
                String productName = rs.getString("ProductName");
                int quantity = rs.getInt("Quantity");
                double productPriceTotal = rs.getDouble("ProductPriceTotal");

                PaymentDTO paymentDTO = new PaymentDTO(saleID, productName, quantity, productPriceTotal);
                paymentHistoryDetailOfUser.add(paymentDTO);
            }
        } finally {
            closeConnection();
        }

        return paymentHistoryDetailOfUser;
    }

    public List<PaymentDTO> getPaymentHistoryDetailByProductName(String email, String searchedProductName) throws Exception {
        List<PaymentDTO> searchedList = null;

        try {
            String sql = "select SaleID, ProductName, Quantity, ProductPriceTotal from SaleDetail where SaleID in (select SaleID from SaleHistory where Email = ?) and ProductName like ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, email);
            preStm.setString(2, "%" + searchedProductName + "%");
            rs = preStm.executeQuery();
            searchedList = new ArrayList<>();
            while (rs.next()) {
                int saleID = rs.getInt("SaleID");
                String productName = rs.getString("ProductName");
                int quantity = rs.getInt("Quantity");
                double productPriceTotal = rs.getDouble("ProductPriceTotal");

                PaymentDTO paymentDTO = new PaymentDTO(saleID, productName, quantity, productPriceTotal);
                searchedList.add(paymentDTO);
            }
        } finally {
            closeConnection();
        }

        return searchedList;
    }

    public List<PaymentDTO> getPaymentHistoryByShoppingTime(String email, Timestamp searchedStartingTime, Timestamp searchedEndingTime) throws Exception {
        List<PaymentDTO> searchedList = null;

        try {
            String sql = "select SaleID, BuyTime, PaymentMethod, BillPriceTotal from SaleHistory where Email = ? and BuyTime between ? and ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, email);
            preStm.setTimestamp(2, searchedStartingTime);
            preStm.setTimestamp(3, searchedEndingTime);
            rs = preStm.executeQuery();
            searchedList = new ArrayList<>();
            while (rs.next()) {
                int saleID = rs.getInt("SaleID");
                Timestamp buyTime = rs.getTimestamp("BuyTime");
                String paymentMethod = rs.getString("PaymentMethod");
                double billPriceTotal = rs.getDouble("BillPriceTotal");

                PaymentDTO paymentDTO = new PaymentDTO(saleID, buyTime, paymentMethod, billPriceTotal);
                searchedList.add(paymentDTO);
            }
        } finally {
            closeConnection();
        }

        return searchedList;
    }

    public List<PaymentDTO> getPaymentHistoryDetailByShoppingTime(String email, Timestamp searchedStartingTime, Timestamp searchedEndingTime) throws Exception {
        List<PaymentDTO> searchedList = null;

        try {
            String sql = "select SaleID, ProductName, Quantity, ProductPriceTotal from SaleDetail where SaleID in (select SaleID from SaleHistory where SaleHistory.Email = ? and SaleHistory.BuyTime between ? and ?)";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, email);
            preStm.setTimestamp(2, searchedStartingTime);
            preStm.setTimestamp(3, searchedEndingTime);
            rs = preStm.executeQuery();
            searchedList = new ArrayList<>();
            while (rs.next()) {
                int saleID = rs.getInt("SaleID");
                String productName = rs.getString("ProductName");
                int quantity = rs.getInt("Quantity");
                double productPriceTotal = rs.getDouble("ProductPriceTotal");

                PaymentDTO paymentDTO = new PaymentDTO(saleID, productName, quantity, productPriceTotal);
                searchedList.add(paymentDTO);
            }
        } finally {
            closeConnection();
        }

        return searchedList;
    }

    public List<PaymentDTO> getPaymentHistoryDetailByProductNameAndShoppingTime(String email, String pdName, Timestamp searchedStartingShoppingTime, Timestamp searchedEndingShoppingTime) throws Exception {
        List<PaymentDTO> searchedList = null;

        try {
            String sql = "select SaleID, ProductName, Quantity, ProductPriceTotal from SaleDetail where SaleID in (select SaleID from SaleHistory where SaleHistory.Email = ? and SaleHistory.BuyTime between ? and ?) and ProductName like ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, email);
            preStm.setTimestamp(2, searchedStartingShoppingTime);
            preStm.setTimestamp(3, searchedEndingShoppingTime);
            preStm.setString(4, "%" + pdName + "%");
            rs = preStm.executeQuery();
            searchedList = new ArrayList<>();
            while (rs.next()) {
                int saleID = rs.getInt("SaleID");
                String productName = rs.getString("ProductName");
                int quantity = rs.getInt("Quantity");
                double productPriceTotal = rs.getDouble("ProductPriceTotal");

                PaymentDTO paymentDTO = new PaymentDTO(saleID, productName, quantity, productPriceTotal);
                searchedList.add(paymentDTO);
            }
        } finally {
            closeConnection();
        }

        return searchedList;
    }

    public int getSaleID(String email, String buyTime) throws Exception {
        int saleID = -1;

        try {
            String sql = "select SaleID from SaleHistory where Email = ? and BuyTime = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, email);
            preStm.setString(2, buyTime);
            rs = preStm.executeQuery();
            if (rs.next()) {
                saleID = rs.getInt("SaleID");
            }
        } finally {
            closeConnection();
        }

        return saleID;
    }

    public boolean recordUserOrderDetail(int sailID, List<ProductDTO> productsList) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "insert into SaleDetail(SaleID, ProductName, Quantity, ProductPriceTotal) values (?)";
            String sqlIn = "";
            for (ProductDTO product : productsList) {
                double productPriceTotal = Math.round(product.getPrice() * product.getQuantity() * 100) / 100;
                sqlIn += "(" + sailID + ",'" + product.getProductName() + "'," + product.getQuantity() + "," + productPriceTotal + "), ";
            }
            sqlIn = sqlIn.substring(0, sqlIn.length() - 2);
            sql = sql.replace("(?)", sqlIn);
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public boolean recordUserOrder(String email, Timestamp buyTime, String paymentMethod, double billPriceTotal) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "insert into SaleHistory(Email, BuyTime, PaymentMethod, BillPriceTotal)  values(?, ?, ?, ?)";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, email);
            preStm.setTimestamp(2, buyTime);
            preStm.setString(3, paymentMethod);
            preStm.setDouble(4, billPriceTotal);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }
}
