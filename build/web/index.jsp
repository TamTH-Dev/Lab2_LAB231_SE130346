<%-- 
    Document   : index
    Created on : Feb 20, 2020, 5:58:13 PM
    Author     : hoang
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <link rel="stylesheet" href="./styles/index.css" />
    </head>
    <body>
        <c:if test="${param.searchedProductName == null && param.searchedPriceLevel == null}">
            <c:if test="${requestScope.ProductsData == null}">
                <c:url value="DataLoading" var="loadData"></c:url>
                <c:redirect url="${loadData}" />
            </c:if>
        </c:if>

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
                            <button><a class="header-btn" onclick="signOut();" href="#">Sign Out</a></button>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </header>

        <div class="container">
            <form action="ProductNameAndPriceLevelSearching" method="POST" class="form-inline search-form">
                <div class="form-group mr-3">
                    <label for="productName" class="sr-only">Product Name</label>
                    <input id="productName" name="searchedProductName" value="${param.searchedProductName}" type="text" class="form-control" placeholder="Product Name" />
                </div>

                <div class="form-group mx-sm-3">
                    <div class="form-check form-check-inline mr-3">
                        <input class="form-check-input price-level" type="radio" name="searchedPriceLevel" id="level-1" value="level-1" 
                               <c:if test="${param.searchedPriceLevel.equals('level-1')}">
                                   checked="checked"
                               </c:if>
                               >
                        <label class="form-check-label" for="level-1">0$ - 20$</label>
                    </div>
                    <div class="form-check form-check-inline mr-3">
                        <input class="form-check-input price-level" type="radio" name="searchedPriceLevel" id="level-2" value="level-2"
                               <c:if test="${param.searchedPriceLevel.equals('level-2')}">
                                   checked="checked"
                               </c:if>
                               >
                        <label class="form-check-label" for="level-2">20$ - 50$</label>
                    </div>
                    <div class="form-check form-check-inline mr-3">
                        <input class="form-check-input price-level" type="radio" name="searchedPriceLevel" id="level-3" value="level-3"
                               <c:if test="${param.searchedPriceLevel.equals('level-3')}">
                                   checked="checked"
                               </c:if>
                               >
                        <label class="form-check-label" for="level-3"> Greater than 50$</label>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary search-btn" name="action" value="search" id="search-btn">
                    Search
                </button>
                <span id="search-error" style="display: none; color: #f00; position: absolute; top: 42px;">Please Fill Out Or Select One Price Level To Search</span>
                <a class="view-all-btn" href="DataLoading">View All</a>
            </form>

            <c:if test="${requestScope.ProductsData.size() != 0}">
                <div class="products-container">
                    <c:forEach items="${requestScope.ProductsData}" var="product">
                        <div class="card">
                            <img src="./uploads/${product.imgPath}" class="card-img-top" alt="...">
                            <div class="card-body">
                                <h5 class="card-title">
                                    <c:url value="ProductDetailLoading" var="loadProductDetail" >
                                        <c:param name="productName" value="${product.productName}" />
                                    </c:url>
                                    <a href="${loadProductDetail}" class="product-title">
                                        ${product.productName}
                                    </a>
                                </h5>
                                <p class="card-text">${product.description}</p>
                                <c:url value="CartAdding" var="addToCart">
                                    <c:param name="productName" value="${product.productName}" />
                                    <c:param name="price" value="${product.price}" />
                                    <c:param name="quantity" value="1" />
                                    <c:param name="category" value="${product.category}" />
                                    <c:param name="imgPath" value="${product.imgPath}" />
                                </c:url>
                                <a href="${addToCart}" class="btn btn-primary">Add to Cart</a>
                                <span class="card-price">${product.price} $</span>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="page-container">
                    <c:forEach var = "index" begin = "1" end = "${requestScope.TotalPage}">
                        <c:if test="${param.searchedProductName == null && param.searchedPriceLevel == null}">
                            <c:url value="DataLoading" var="handlePage">
                                <c:param value="${index}" name="pg" />
                            </c:url>
                        </c:if>
                        <c:if test="${param.searchedProductName != null || param.searchedPriceLevel != null}">
                            <c:url value="ProductNameAndPriceLevelSearching" var="handlePage">
                                <c:param value="${index}" name="pg" />
                                <c:if test="${param.searchedProductName != null}">
                                    <c:param name="searchedProductName" value="${param.searchedProductName}" /> 
                                </c:if>
                                <c:if test="${param.searchedPriceLevel != null}">
                                    <c:param name="searchedPriceLevel" value="${param.searchedPriceLevel}" /> 
                                </c:if>
                            </c:url>
                        </c:if>
                        <a class="page-item <c:if test="${param.pg == index}">active</c:if>" href="${handlePage}">${index}</a>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${requestScope.ProductsData.size() == 0}">
                <h1 style="color: #f00; text-align: center;">There isn't any data!</h1>
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
        <script src="./scripts/user-page-handling.js"></script>
        <script src="https://apis.google.com/js/platform.js?onload=onLoad" async defer></script>
    </body>
</html>
