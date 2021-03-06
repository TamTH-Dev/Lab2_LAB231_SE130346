<%-- 
    Document   : product-creating
    Created on : Feb 21, 2020, 10:13:15 PM
    Author     : hoang
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Creating</title>
        <link rel="stylesheet" href="./styles/all.css"/>
        <link rel="stylesheet" href="./styles/bootstrap.min.css" />
        <link rel="stylesheet" href="./styles/header.css" />
        <link rel="stylesheet" href="./styles/footer.css" />
    </head>
    <style>
        .container {
            margin-top: 50px;
            margin-bottom: 30px;
            padding: 0 150px 40px 150px;
            box-shadow: 0 1px 20px 5px rgba(0, 0, 0, 0.2);
        }

        .container form .form-group {
            margin-bottom: 30px;
            position: relative;
        }

        .container form .form-group .error {
            color: #f00;
            position: absolute;
            top: 70px;
            left: 0;
            font-size: 16px;
        }
    </style>
    <body>
        <header>
            <nav class="nav-app">
                <div class="logo">
                    <a href="
                       <c:if test="${sessionScope.ROLE ne 'Admin'}">index.jsp</c:if>
                       <c:if test="${sessionScope.ROLE eq 'Admin'}">admin.jsp</c:if> 
                           ">
                           <img src="./images/logo.png" alt="">
                       </a>
                    </div>
                    <ul class="nav-menu">
                        <li><a href="
                            <c:if test="${sessionScope.ROLE ne 'Admin'}">index.jsp</c:if>
                            <c:if test="${sessionScope.ROLE eq 'Admin'}">admin.jsp</c:if>
                                ">Home</a></li>
                    <c:if test="${sessionScope.ROLE eq 'User'}"><li><a href="article-posting.jsp">Blog Posting</a></li></c:if>
                        <c:if test="${sessionScope.ROLE ne 'User' && sessionScope.ROLE ne 'Admin'}">
                        <li><button><a class="header-btn" href="login.jsp">Sign In</a></li>
                        <li><button><a class="header-btn" href="register.jsp">Sign Up</a></li>
                                </c:if>
                                <c:if test="${sessionScope.ROLE eq 'User' || sessionScope.ROLE eq 'Admin'}">
                        <li><a href="#">Hello, ${sessionScope.NAME}!</a></li>
                        <li><button><a class="header-btn" href="Logout">Sign Out</a></button></li>
                                </c:if>
                </ul>
            </nav>
        </header>

        <div class="container">
            <h1 style="text-align: center; padding:20px 0;">Add Product</h1>
            <form action="ProductCreating" enctype="multipart/form-data" method="POST">
                <div class="form-group">
                    <label for="productName">Product Name</label>
                    <input type="text" name="productName" class="form-control" id="productName" value="${ProductInformation.productName}"/>
                    <span class="error" id="product-name-error">
                        <c:if test="${CreateError != null}">
                            ${CreateError}
                        </c:if>
                    </span>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea type="text" name="description" class="form-control" style="resize: none;" rows="3" id="description">${ProductInformation.description}</textarea>
                    <span class="error" style="top: 120px;" id="description-error"></span>
                </div>
                <div class="form-group">
                    <label for="quantity">Quantity</label>
                    <input type="text" name="quantity" class="form-control" id="quantity" value="${ProductInformation.quantity}" />
                    <span class="error" id="quantity-error"></span>
                </div>
                <div class="form-group">
                    <label for="price">Price</label>
                    <input type="text" name="price" class="form-control" id="price" value="${ProductInformation.price}" />
                    <span class="error" id="price-error"></span>
                </div>
                <div class="form-group">
                    <label for="category">Category</label>
                    <select name="category" class="form-control" id="category">
                        <option value="" 
                                <c:if test="${ProductInformation.category == null}">
                                    selected
                                </c:if>
                                >
                            Choose...
                        </option>
                        <option value="food"
                                <c:if test="${ProductInformation.category eq 'food'}">
                                    selected
                                </c:if>
                                >
                            Food
                        </option>
                        <option value="drink"
                                <c:if test="${ProductInformation.category eq 'drink'}">
                                    selected
                                </c:if>
                                >
                            Drink
                        </option>
                        <option value="icecream"
                                <c:if test="${ProductInformation.category eq 'icecream'}">
                                    selected
                                </c:if>
                                >
                            Ice Cream
                        </option>
                    </select>
                    <span class="error" id="category-error"></span>
                </div>
                <div class="form-group" style="height: 90px;">
                    <label for="image">Image</label>
                    <input type="file" name="image" class="form-control-file" id="image" />
                    <span class="error" style="top: 60px;" id="image-error"></span>
                            <c:if test="${DuplicateError != null}">
                        <div style="position: absolute; bottom: 0; left: 0; color: #f00;">${DuplicateError}</div> 
                    </c:if>
                </div>
                <button type="submit" class="btn btn-primary" id="add-btn">Add</button>
            </form>
        </div>

        <footer class="container-fluid main-footer">
            <div class="row">
                <div class="main-footer-contacts col-lg-4 col-md-12">
                    <h3>Contact Us</h3>
                    <ul class="contacts-list">
                        <li>
                            <p>1 (800) 686-6688</p>
                            <p>blog@gmail.com</p>
                        </li>
                        <li>
                            <p>40 Baria Sreet 133/2</p>
                            <p>NewYork City, US</p>
                        </li>
                        <li>Open hours: 8.00-22.00 Mon-Sat</li>
                    </ul>
                </div>
                <div class="main-footer-legal col-lg-4 col-md-12">
                    <h3>Legal</h3>
                    <ul class="legal-list">
                        <li>
                            <a href="#">Privacy Policy</a>
                        </li>
                        <li>
                            <a href="#">Terms And Conditions</a>
                        </li>
                    </ul>
                </div>
                <div class="main-footer-newsletter col-lg-4 col-md-12">
                    <h3>Our Newsletter</h3>
                    <p>Subscribe to our mailing list to get the updates to your email inbox.</p>
                    <form>
                        <input type="text" placeholder="Email">
                        <button type="button">SUBSCRIBE</button>
                    </form>
                    <ul class="links-list">
                        <li><i class="fab fa-facebook-f"></i></li>
                        <li><i class="fab fa-twitter"></i></li>
                        <li><i class="fab fa-google-plus-g"></i></li>
                        <li><i class="fab fa-instagram"></i></li>
                    </ul>
                </div>
            </div>
            <div class="footer-reserved">
                Copyright ©2019 All rights reserved
            </div>
        </footer>
        <script src="./scripts/all.js"></script>
        <script src="./scripts/product-creating-handling.js"></script>
    </body>
</html>
