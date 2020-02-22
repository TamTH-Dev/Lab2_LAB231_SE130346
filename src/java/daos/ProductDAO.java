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

    public boolean updateProductWithImage(String productName, String imgPath, String description, int quantity, double price, String category) throws Exception {
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

    public boolean updateProductWithoutImage(String productName, String description, int quantity, double price, String category) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "update Product set Description = ?, Quantity = ?, Price = ?, Category = ? where ProductName = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, description);
            preStm.setInt(2, quantity);
            preStm.setDouble(3, price);
            preStm.setString(4, category);
            preStm.setString(5, productName);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public List<ProductDTO> getSearchedProductsDataForAdminPage(String searchedProductName, String searchedPriceLevel, int page, int numOfProductsPerPage) throws Exception {
        List<ProductDTO> productsList = null;

        try {
            conn = MyConnection.getMyConnection();

            if (!searchedProductName.equals("") && searchedPriceLevel == null) {
                searchDataByProductNameForAdminPage(searchedProductName, page, numOfProductsPerPage);
            } else if (searchedProductName.equals("") && searchedPriceLevel != null) {
                searchDataByPriceLevelForAdminPage(searchedPriceLevel, page, numOfProductsPerPage);
            } else if (!searchedProductName.equals("") && searchedPriceLevel != null) {
                searchDataByProductNameAndPriceLevelForAdminPage(searchedProductName, searchedPriceLevel, page, numOfProductsPerPage);
            }

            productsList = new ArrayList<>();
            int count = 0;
            while (rs.next()) {
                System.out.println(++count);
                String productName = rs.getString("ProductName");
                String imgPath = rs.getString("ImgPath");
                String description = rs.getString("Description");
                int quantity = Integer.parseInt(rs.getString("Quantity"));
                double price = Double.parseDouble(rs.getString("Price"));
                String category = rs.getString("Category");
                String status = rs.getString("Status");
                Timestamp createdTime = rs.getTimestamp("CreatedTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createdTime), status);
                productsList.add(product);
            }
        } finally {
            closeConnection();
        }

        return productsList;
    }

    private void searchDataByProductNameForAdminPage(String searchedProductName, int page, int numOfProductsPerPage) throws Exception {
        String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status, ROW_NUMBER() over (order by CreatedTime desc) as rowNum from Product where ProductName like ?) as product where product.rowNum between ? and ?";
        preStm = conn.prepareStatement(sql);
        preStm.setString(1, "%" + searchedProductName + "%");
        preStm.setInt(2, (page - 1) * numOfProductsPerPage + 1);
        preStm.setInt(3, (page - 1) * numOfProductsPerPage + numOfProductsPerPage);
        rs = preStm.executeQuery();
    }

    private void searchDataByPriceLevelForAdminPage(String searchedPriceLevel, int page, int numOfProductsPerPage) throws Exception {
        String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status, ROW_NUMBER() over (order by CreatedTime desc) as rowNum from Product where Price between ? and ?) as product where product.rowNum between ? and ?";

        double minValue;
        double maxValue;
        switch (searchedPriceLevel) {
            case "level-1":
                minValue = 0;
                maxValue = 20;
                break;
            case "level-2":
                minValue = 20;
                maxValue = 50;
                break;
            default:
                minValue = 50;
                maxValue = 999999;
                break;
        }

        preStm = conn.prepareStatement(sql);
        preStm.setDouble(1, minValue);
        preStm.setDouble(2, maxValue);
        preStm.setInt(3, (page - 1) * numOfProductsPerPage + 1);
        preStm.setInt(4, (page - 1) * numOfProductsPerPage + numOfProductsPerPage);
        rs = preStm.executeQuery();
    }

    private void searchDataByProductNameAndPriceLevelForAdminPage(String searchedProductName, String searchedPriceLevel, int page, int numOfProductsPerPage) throws Exception {
        String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreatedTime, Status, ROW_NUMBER() over (order by CreatedTime desc) as rowNum from Product where ProductName like ? and Price between ? and ?) as product where product.rowNum between ? and ?";

        double minValue;
        double maxValue;
        switch (searchedPriceLevel) {
            case "level-1":
                minValue = 0;
                maxValue = 20;
                break;
            case "level-2":
                minValue = 20;
                maxValue = 50;
                break;
            default:
                minValue = 50;
                maxValue = 999999;
                break;
        }

        preStm = conn.prepareStatement(sql);
        preStm.setString(1, "%" + searchedProductName + "%");
        preStm.setDouble(2, minValue);
        preStm.setDouble(3, maxValue);
        preStm.setInt(4, (page - 1) * numOfProductsPerPage + 1);
        preStm.setInt(5, (page - 1) * numOfProductsPerPage + numOfProductsPerPage);
        rs = preStm.executeQuery();
    }

    public int getSearchedProductsTotalForAdminPage(String searchedProductName, String searchedPriceLevel) throws Exception {
        int total = 0;
        try {
            conn = MyConnection.getMyConnection();
            if (!searchedProductName.equals("") && searchedPriceLevel == null) {
                total = getSearchedProductsTotalByProductNameForAdminPage(searchedProductName);
            } else if (searchedProductName.equals("") && searchedPriceLevel != null) {
                total = getSearchedProductsTotalByPriceLevelForAdminPage(searchedPriceLevel);
            } else if (!searchedProductName.equals("") && searchedPriceLevel != null) {
                total = getSearchedProductsTotalByProductNameAndPriceLevelForAdminPage(searchedProductName, searchedPriceLevel);
            }
        } finally {
            closeConnection();
        }

        return total;
    }

    private int getSearchedProductsTotalByProductNameForAdminPage(String searchedProductName) throws Exception {
        int total = 0;

        String sql = "select count(*) from Product where ProductName like ?";
        preStm = conn.prepareStatement(sql);
        preStm.setString(1, "%" + searchedProductName + "%");
        rs = preStm.executeQuery();
        if (rs.next()) {
            total = rs.getInt(1);
        }

        return total;
    }

    private int getSearchedProductsTotalByPriceLevelForAdminPage(String searchedPriceLevel) throws Exception {
        int total = 0;
        String sql = "select count(*) from Product where Price between ? and ?";

        double minValue;
        double maxValue;
        switch (searchedPriceLevel) {
            case "level-1":
                minValue = 0;
                maxValue = 20;
                break;
            case "level-2":
                minValue = 20;
                maxValue = 50;
                break;
            default:
                minValue = 50;
                maxValue = 999999;
        }

        preStm = conn.prepareStatement(sql);
        preStm.setDouble(1, minValue);
        preStm.setDouble(2, maxValue);
        rs = preStm.executeQuery();
        if (rs.next()) {
            total = rs.getInt(1);
        }

        return total;
    }

    private int getSearchedProductsTotalByProductNameAndPriceLevelForAdminPage(String searchedProductName, String searchedPriceLevel) throws Exception {
        int total = 0;
        String sql = "select count(*) from Product where ProductName like ? and Price between ? and ?";

        double minValue;
        double maxValue;
        switch (searchedPriceLevel) {
            case "level-1":
                minValue = 0;
                maxValue = 20;
                break;
            case "level-2":
                minValue = 20;
                maxValue = 50;
                break;
            default:
                minValue = 50;
                maxValue = 999999;
        }

        preStm = conn.prepareStatement(sql);
        preStm.setString(1, "%" + searchedProductName + "%");
        preStm.setDouble(2, minValue);
        preStm.setDouble(3, maxValue);
        rs = preStm.executeQuery();
        if (rs.next()) {
            total = rs.getInt(1);
        }

        return total;
    }
}
