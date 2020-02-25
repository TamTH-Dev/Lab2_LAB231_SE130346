/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import db.MyConnection;
import dtos.ProductDTO;
import java.io.Serializable;
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
public class ProductDAO implements Serializable {

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

    public boolean createProduct(String productName, String imgPath, String description, int quantity, double price, String category, Timestamp createTime) throws Exception {
        boolean isSuccess = false;

        try {
            String sql = "insert into Product(ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status) values(?, ?, ?, ?, ?, ?, ?, 'Active')";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, productName);
            preStm.setString(2, imgPath);
            preStm.setString(3, description);
            preStm.setInt(4, quantity);
            preStm.setDouble(5, price);
            preStm.setString(6, category);
            preStm.setTimestamp(7, createTime);
            isSuccess = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }

        return isSuccess;
    }

    public boolean isDuplicate(String productName) throws Exception {
        boolean isDuplicate = false;

        try {
            String sql = "select ProductName from Product where ProductName like ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, productName);
            rs = preStm.executeQuery();
            if (rs.next()) {
                isDuplicate = true;
            }
        } finally {
            closeConnection();
        }

        return isDuplicate;
    }

    public List<ProductDTO> getAllProductsForAdminPage(int page, int numOfbBlogsPerPage) throws Exception {
        List<ProductDTO> productsList = null;

        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status, ROW_NUMBER() over (order by CreateTime desc) as rowNum from Product) as product where product.rowNum between ? and ?";
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
                Timestamp createTime = rs.getTimestamp("CreateTime");
                String status = rs.getString("Status");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
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

    public ProductDTO getProductDetailByProductNameForAdminPage(String pdName) throws Exception {
        ProductDTO product = null;

        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from Product where ProductName = ?";
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
                Timestamp createTime = rs.getTimestamp("CreateTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                String status = rs.getString("Status");

                product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
            }
        } finally {
            closeConnection();
        }

        return product;
    }

    public ProductDTO getProductDetailByProductNameForUserPage(String pdName) throws Exception {
        ProductDTO product = null;

        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from Product where ProductName = ? and Status = 'Active'";
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
                Timestamp createTime = rs.getTimestamp("CreateTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                String status = rs.getString("Status");

                product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
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

    public String getCurrentImgPath(String productName) throws Exception {
        String currentImgPath = null;

        try {
            String sql = "select ImgPath from Product where ProductName = ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, productName);
            rs = preStm.executeQuery();
            if (rs.next()) {
                currentImgPath = rs.getString("ImgPath");
            }
        } finally {
            closeConnection();
        }

        return currentImgPath;
    }

    public boolean isImageUsedByOtherProducts(String productName, String imgPath) throws Exception {
        boolean isUsed = false;

        try {
            String sql = "select ProductName from Product where ImgPath = ? and ProductName <> ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, imgPath);
            preStm.setString(2, productName);
            rs = preStm.executeQuery();
            if (rs.next()) {
                isUsed = true;
            }
        } finally {
            closeConnection();
        }

        return isUsed;
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

    public List<ProductDTO> searchDataByProductNameForAdminPage(String searchedProductName, int page, int numOfProductsPerPage) throws Exception {
        List<ProductDTO> productsList = null;
        String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status, ROW_NUMBER() over (order by CreateTime desc) as rowNum from Product where ProductName like ?) as product where product.rowNum between ? and ?";
        try {
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, "%" + searchedProductName + "%");
            preStm.setInt(2, (page - 1) * numOfProductsPerPage + 1);
            preStm.setInt(3, (page - 1) * numOfProductsPerPage + numOfProductsPerPage);
            rs = preStm.executeQuery();

            productsList = new ArrayList<>();
            while (rs.next()) {
                String productName = rs.getString("ProductName");
                String imgPath = rs.getString("ImgPath");
                String description = rs.getString("Description");
                int quantity = Integer.parseInt(rs.getString("Quantity"));
                double price = Double.parseDouble(rs.getString("Price"));
                String category = rs.getString("Category");
                String status = rs.getString("Status");
                Timestamp createTime = rs.getTimestamp("CreateTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
                productsList.add(product);
            }
        } finally {
            closeConnection();
        }
        return productsList;
    }

    public List<ProductDTO> searchDataByPriceLevelForAdminPage(double minValue, double maxValue, int page, int numOfProductsPerPage) throws Exception {
        List<ProductDTO> productsList = null;
        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status, ROW_NUMBER() over (order by CreateTime desc) as rowNum from Product where Price between ? and ?) as product where product.rowNum between ? and ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setDouble(1, minValue);
            preStm.setDouble(2, maxValue);
            preStm.setInt(3, (page - 1) * numOfProductsPerPage + 1);
            preStm.setInt(4, (page - 1) * numOfProductsPerPage + numOfProductsPerPage);
            rs = preStm.executeQuery();

            productsList = new ArrayList<>();
            while (rs.next()) {
                String productName = rs.getString("ProductName");
                String imgPath = rs.getString("ImgPath");
                String description = rs.getString("Description");
                int quantity = Integer.parseInt(rs.getString("Quantity"));
                double price = Double.parseDouble(rs.getString("Price"));
                String category = rs.getString("Category");
                String status = rs.getString("Status");
                Timestamp createTime = rs.getTimestamp("CreateTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
                productsList.add(product);
            }
        } finally {
            closeConnection();
        }

        return productsList;
    }

    public List<ProductDTO> searchDataByProductNameAndPriceLevelForAdminPage(String searchedProductName, double minValue, double maxValue, int page, int numOfProductsPerPage) throws Exception {
        List<ProductDTO> productsList = null;
        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status, ROW_NUMBER() over (order by CreateTime desc) as rowNum from Product where ProductName like ? and Price between ? and ?) as product where product.rowNum between ? and ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, "%" + searchedProductName + "%");
            preStm.setDouble(2, minValue);
            preStm.setDouble(3, maxValue);
            preStm.setInt(4, (page - 1) * numOfProductsPerPage + 1);
            preStm.setInt(5, (page - 1) * numOfProductsPerPage + numOfProductsPerPage);
            rs = preStm.executeQuery();

            productsList = new ArrayList<>();
            while (rs.next()) {
                String productName = rs.getString("ProductName");
                String imgPath = rs.getString("ImgPath");
                String description = rs.getString("Description");
                int quantity = Integer.parseInt(rs.getString("Quantity"));
                double price = Double.parseDouble(rs.getString("Price"));
                String category = rs.getString("Category");
                String status = rs.getString("Status");
                Timestamp createTime = rs.getTimestamp("CreateTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
                productsList.add(product);
            }
        } finally {
            closeConnection();
        }

        return productsList;
    }

    public int getSearchedProductsTotalByProductNameForAdminPage(String searchedProductName) throws Exception {
        int total = 0;

        try {
            String sql = "select count(*) from Product where ProductName like ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, "%" + searchedProductName + "%");
            rs = preStm.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } finally {
            closeConnection();
        }

        return total;
    }

    public int getSearchedProductsTotalByPriceLevelForAdminPage(double minValue, double maxValue) throws Exception {
        int total = 0;

        try {
            String sql = "select count(*) from Product where Price between ? and ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setDouble(1, minValue);
            preStm.setDouble(2, maxValue);
            rs = preStm.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } finally {
            closeConnection();
        }

        return total;
    }

    public int getSearchedProductsTotalByProductNameAndPriceLevelForAdminPage(String searchedProductName, double minValue, double maxValue) throws Exception {
        int total = 0;

        try {
            String sql = "select count(*) from Product where ProductName like ? and Price between ? and ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, "%" + searchedProductName + "%");
            preStm.setDouble(2, minValue);
            preStm.setDouble(3, maxValue);
            rs = preStm.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } finally {
            closeConnection();
        }

        return total;
    }

    public List<ProductDTO> getAllProductsForUserPage(int page, int numOfbBlogsPerPage) throws Exception {
        List<ProductDTO> productsList = null;

        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status, ROW_NUMBER() over (order by CreateTime desc) as rowNum from Product where Status = 'Active') as product where product.rowNum between ? and ?";
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
                Timestamp createTime = rs.getTimestamp("CreateTime");
                String status = rs.getString("Status");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
                productsList.add(product);
            }
        } finally {
            closeConnection();
        }

        return productsList;
    }

    public int getProductsTotalForUserPage() throws Exception {
        int total = 0;

        try {
            String sql = "select count(*) from Product where Status = 'Active'";
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

    public List<ProductDTO> searchDataByProductNameForUserPage(String searchedProductName, int page, int numOfProductsPerPage) throws Exception {
        List<ProductDTO> productsList = null;
        String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status, ROW_NUMBER() over (order by CreateTime desc) as rowNum from Product where ProductName like ? and Status = 'Active') as product where product.rowNum between ? and ?";
        try {
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, "%" + searchedProductName + "%");
            preStm.setInt(2, (page - 1) * numOfProductsPerPage + 1);
            preStm.setInt(3, (page - 1) * numOfProductsPerPage + numOfProductsPerPage);
            rs = preStm.executeQuery();

            productsList = new ArrayList<>();
            while (rs.next()) {
                String productName = rs.getString("ProductName");
                String imgPath = rs.getString("ImgPath");
                String description = rs.getString("Description");
                int quantity = Integer.parseInt(rs.getString("Quantity"));
                double price = Double.parseDouble(rs.getString("Price"));
                String category = rs.getString("Category");
                String status = rs.getString("Status");
                Timestamp createTime = rs.getTimestamp("CreateTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
                productsList.add(product);
            }
        } finally {
            closeConnection();
        }
        return productsList;
    }

    public List<ProductDTO> searchDataByPriceLevelForUserPage(double minValue, double maxValue, int page, int numOfProductsPerPage) throws Exception {
        List<ProductDTO> productsList = null;
        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status, ROW_NUMBER() over (order by CreateTime desc) as rowNum from Product where Price between ? and ? and Status = 'Active') as product where product.rowNum between ? and ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setDouble(1, minValue);
            preStm.setDouble(2, maxValue);
            preStm.setInt(3, (page - 1) * numOfProductsPerPage + 1);
            preStm.setInt(4, (page - 1) * numOfProductsPerPage + numOfProductsPerPage);
            rs = preStm.executeQuery();

            productsList = new ArrayList<>();
            while (rs.next()) {
                String productName = rs.getString("ProductName");
                String imgPath = rs.getString("ImgPath");
                String description = rs.getString("Description");
                int quantity = Integer.parseInt(rs.getString("Quantity"));
                double price = Double.parseDouble(rs.getString("Price"));
                String category = rs.getString("Category");
                String status = rs.getString("Status");
                Timestamp createTime = rs.getTimestamp("CreateTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
                productsList.add(product);
            }
        } finally {
            closeConnection();
        }

        return productsList;
    }

    public List<ProductDTO> searchDataByProductNameAndPriceLevelForUserPage(String searchedProductName, double minValue, double maxValue, int page, int numOfProductsPerPage) throws Exception {
        List<ProductDTO> productsList = null;
        try {
            String sql = "select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status from (select ProductName, ImgPath, Description, Quantity, Price, Category, CreateTime, Status, ROW_NUMBER() over (order by CreateTime desc) as rowNum from Product where ProductName like ? and Price between ? and ? and Status = 'Active') as product where product.rowNum between ? and ?";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, "%" + searchedProductName + "%");
            preStm.setDouble(2, minValue);
            preStm.setDouble(3, maxValue);
            preStm.setInt(4, (page - 1) * numOfProductsPerPage + 1);
            preStm.setInt(5, (page - 1) * numOfProductsPerPage + numOfProductsPerPage);
            rs = preStm.executeQuery();

            productsList = new ArrayList<>();
            while (rs.next()) {
                String productName = rs.getString("ProductName");
                String imgPath = rs.getString("ImgPath");
                String description = rs.getString("Description");
                int quantity = Integer.parseInt(rs.getString("Quantity"));
                double price = Double.parseDouble(rs.getString("Price"));
                String category = rs.getString("Category");
                String status = rs.getString("Status");
                Timestamp createTime = rs.getTimestamp("CreateTime");
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                ProductDTO product = new ProductDTO(productName, imgPath, description, quantity, price, category, sdf.format(createTime), status);
                productsList.add(product);
            }
        } finally {
            closeConnection();
        }

        return productsList;
    }

    public int getSearchedProductsTotalByProductNameForUserPage(String searchedProductName) throws Exception {
        int total = 0;

        try {
            String sql = "select count(*) from Product where ProductName like ? and Status = 'Active'";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, "%" + searchedProductName + "%");
            rs = preStm.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } finally {
            closeConnection();
        }

        return total;
    }

    public int getSearchedProductsTotalByPriceLevelForUserPage(double minValue, double maxValue) throws Exception {
        int total = 0;

        try {
            String sql = "select count(*) from Product where Price between ? and ? and Status = 'Active'";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setDouble(1, minValue);
            preStm.setDouble(2, maxValue);
            rs = preStm.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } finally {
            closeConnection();
        }

        return total;
    }

    public int getSearchedProductsTotalByProductNameAndPriceLevelForUserPage(String searchedProductName, double minValue, double maxValue) throws Exception {
        int total = 0;

        try {
            String sql = "select count(*) from Product where ProductName like ? and Price between ? and ? and Status = 'Active'";
            conn = MyConnection.getMyConnection();
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, "%" + searchedProductName + "%");
            preStm.setDouble(2, minValue);
            preStm.setDouble(3, maxValue);
            rs = preStm.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } finally {
            closeConnection();
        }

        return total;
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
                double productPriceTotal = product.getPrice() * product.getQuantity();
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
