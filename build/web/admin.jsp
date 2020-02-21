<%-- 
    Document   : admin
    Created on : Feb 20, 2020, 10:38:20 PM
    Author     : hoang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="ProductCreating" enctype="multipart/form-data" method="POST">
            <input type="text" name="productName">
            <input type="text" name="description">
            <input type="text" name="quantity">
            <input type="text" name="price">
            <select name="category">
                <option value="food">Food</option>
                <option value="drink">Drink</option>
                <option value="pudding">Pudding</option>
            </select>
            <input type="file" name="image" />
            <input type="submit" value="Submit" />
        </form>
    </body>
</html>
