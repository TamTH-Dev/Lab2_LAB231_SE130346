<%-- 
    Document   : cart-management
    Created on : Feb 24, 2020, 10:45:37 PM
    Author     : hoang
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="./styles/all.css"/>
        <link rel="stylesheet" href="./styles/bootstrap.min.css" />
        <link rel="stylesheet" href="./styles/header.css" />
        <link rel="stylesheet" href="./styles/footer.css" />
        <link rel="stylesheet" href="./styles/cart-management.css" />
    </head>
    <body>
        <header>
            <nav class="nav-app">
                <div class="logo">
                    <a href="
                       <c:if test="${sessionScope.ROLE ne 'Admin'}">
                           index.jsp
                       </c:if>
                       <c:if test="${sessionScope.ROLE eq 'Admin'}">
                           admin.jsp
                       </c:if> 
                       ">
                        <img src="./images/logo.png" alt="">
                    </a>
                </div>
                <ul class="nav-menu">
                    <li>
                        <a href="
                           <c:if test="${sessionScope.ROLE ne 'Admin'}">
                               index.jsp
                           </c:if>
                           <c:if test="${sessionScope.ROLE eq 'Admin'}">
                               admin.jsp
                           </c:if>
                           ">
                            Home
                        </a>
                    </li>
                    <c:if test="${sessionScope.ROLE eq 'User' || sessionScope.ROLE eq 'Admin'}">
                        <li>
                            <a href="#">Hello, ${sessionScope.NAME}!</a>
                        </li>
                    </c:if>
                    <c:if test="${sessionScope.ROLE ne 'Admin'}">
                        <li style="margin-right: 10px;">
                            <a class="cart-container" href="article-posting.jsp">
                                <i class="fas fa-shopping-cart cart"></i> 
                                <span>(${sessionScope.CART.getTotalProductsFromCart()})</span>
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${sessionScope.ROLE ne 'User' && sessionScope.ROLE ne 'Admin'}">
                        <li>
                            <button><a class="header-btn" href="login.jsp">Sign In</a>
                        </li>
                        <li>
                            <button><a class="header-btn" href="register.jsp">Sign Up</a>
                        </li>
                    </c:if>
                    <c:if test="${sessionScope.ROLE eq 'User' || sessionScope.ROLE eq 'Admin'}">
                        <li>
                            <button><a class="header-btn" href="Logout">Sign Out</a></button>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </header>

        <div class="container">
            <div class="cart-detail-container">
                <div class="cart-detail-container-header">
                    <div class="image-col col">Product's Image</div>
                    <div class="product-name-col col">Product's Name</div>
                    <div class="category-col col">Category</div>
                    <div class="quantity-col col">Quantity</div>
                    <div class="price-col col">Price</div>
                    <div class="total-price-col col">Total Price</div>
                    <div class="action-col col">Action</div>
                </div>
                <div>
                    <c:forEach items="${requestScope.Cart}" var="product">
                        <div class="product">
                            <div class="image-col col">
                                <img class="product-img" src="./uploads/${product.imgPath}" />
                            </div>
                            <div class="product-name-col col">${product.productName}</div>
                            <div class="category-col col">${product.category}</div>
                            <div class="quantity-col col">${product.quantity}</div>
                            <div class="price-col col">${product.price}</div>
                            <div class="total-price-col col"></div>
                            <div class="action-col col">
                                <button type="button">Delete</button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
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
        <script src="./scripts/cart-management-handling.js"></script>
    </body>
</html>