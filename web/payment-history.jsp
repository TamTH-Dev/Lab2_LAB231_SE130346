<%-- 
    Document   : payment-history
    Created on : Feb 25, 2020, 9:55:16 PM
    Author     : hoang
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <meta name="google-signin-client_id" content="1091231205111-1go7q8tg3q4h7vgs7lkp530hs31lr3dd.apps.googleusercontent.com">
        <title>JSP Page</title>
        <link rel="stylesheet" href="./styles/all.css"/>
        <link rel="stylesheet" href="./styles/bootstrap.min.css" />
        <link rel="stylesheet" href="./styles/header.css" />
        <link rel="stylesheet" href="./styles/footer.css" />
        <link rel="stylesheet" href="./styles/payment-history.css" />
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
                    <c:if test="${sessionScope.ROLE ne 'Admin'}">
                        <li style="margin-right: 10px;">
                            <a class="cart-container" href="CartDataLoading">
                                <i class="fas fa-shopping-cart cart"></i> 
                                <span>(${sessionScope.CART.getTotalProductsFromCart()})</span>
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${sessionScope.ROLE eq 'User'}">
                        <li style="width: 140px;">
                            <a class="cart-container" style="width:140px;" href="UserPaymentHistory">
                                Payment History
                            </a>
                        </li>
                    </c:if>
                    <c:if test="${sessionScope.ROLE eq 'User' || sessionScope.ROLE eq 'Admin'}">
                        <li>
                            <a href="#">Hello, ${sessionScope.NAME}!</a>
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
                            <button><a class="header-btn" href="#" onclick="signOut();">Sign Out</a></button>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </header>

        <div class="container">
            <form action="ProductNameAndShoppingTimeSearching" method="POST" class="form-inline search-form">
                <div class="form-group mr-3">
                    <label for="productName" class="sr-only">Product Name</label>
                    <input id="productName" name="searchedProductName" value="${param.searchedProductName}" type="text" class="form-control" placeholder="Product Name" />
                </div>

                <div class="form-group mr-3">
                    <label for="searchedStartingShoppingTime" class="sr-only">Shopping Time</label>
                    <input id="searchedStartingShoppingTime" name="searchedStartingShoppingTime" value="${param.searchedStartingShoppingTime}" type="text" class="form-control" placeholder="From Time" />
                </div>

                <div class="form-group mr-3">
                    <label for="searchedEndingShoppingTime" class="sr-only">Shopping Time</label>
                    <input id="searchedEndingShoppingTime" name="searchedEndingShoppingTime" value="${param.searchedEndingShoppingTime}" type="text" class="form-control" placeholder="To Time" />
                </div>

                <button type="submit" class="btn btn-primary search-btn" id="search-btn">
                    Search
                </button>
                <span id="fillout-search-error" style="display: none; color: #f00; position: absolute; top: 42px;">Please Fill Out At Least One Form To Search</span>
                <span id="valid-search-error" style="display: none; color: #f00; position: absolute; top: 42px;">Searched Time Should Be In Format (dd-MM-yyyy)</span>
                <a class="view-all-btn" href="UserPaymentHistory">View All</a>
            </form>

            <c:if test="${requestScope.PaymentHistory.size() != 0}">
                <c:forEach items="${requestScope.PaymentHistory}" var="paymentHistoryItem">
                    <div class="payment-history-container">
                        <div class="payment-history-item">
                            <span class="buytime">
                                Buy Time:  <fmt:formatDate type="date" value="${paymentHistoryItem.buyTime}" pattern="dd-MM-yyyy" />
                            </span>
                            <span class="payment-method">Payment Method:  ${paymentHistoryItem.paymentMethod}</span>
                            <span class="bill-price-total">Bill Total:  ${paymentHistoryItem.billPriceTotal} $</span>
                            <span class="collapse-history-btn">
                                <span><i class="fas fa-plus"></i></span>
                            </span>
                        </div>
                        <div class="payment-history-detail-container">
                            <c:forEach items="${requestScope.PaymentHistoryDetail}" var="paymentHistoryDetailItem">
                                <c:if test="${paymentHistoryDetailItem.saleID == paymentHistoryItem.saleID}">
                                    <ul class="payment-history-detail-item">
                                        <li class="product-name">
                                            ${paymentHistoryDetailItem.productName}
                                        </li>
                                        <li class="quantity">
                                            ${paymentHistoryDetailItem.quantity}
                                        </li>
                                        <li class="product-price-total">
                                            ${paymentHistoryDetailItem.productPriceTotal}
                                        </li>
                                    </ul>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${param.searchedProductName != null || (param.searchedStartingShoppingTime != null && param.searchedEndingShoppingTime != null)}">
                <c:if test="${requestScope.PaymentHistory.size() == 0}">
                    <div style="text-align: center; font-size: 28px; color: #f00;">Can't not find appropriate result!</div>
                </c:if>
            </c:if>
            <c:if test="${param.searchedProductName == null && (param.searchedStartingShoppingTime == null && param.searchedEndingShoppingTime == null)}">
                <c:if test="${requestScope.PaymentHistory.size() == 0}">
                    <div style="text-align: center; font-size: 28px; color: #f00;">Your payment history is empty!</div>
                </c:if>
            </c:if>
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
        <script src="./scripts/moment.js"></script>
        <script src="./scripts/payment-history-handling.js"></script>
        <script src="https://apis.google.com/js/platform.js?onload=onLoad" async defer></script>
    </body>
</html>
