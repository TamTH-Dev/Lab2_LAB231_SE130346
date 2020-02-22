<%-- 
    Document   : admin
    Created on : Feb 20, 2020, 10:38:20 PM
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
        <link rel="stylesheet" href="./styles/admin.css" />
    </head>
    <body>
        <c:if test="${param.searchedProductName == null && param.searchedPriceLevel == null}">
            <c:if test="${requestScope.ProductsData == null}">
                <c:redirect url="DataLoading" />
            </c:if>
        </c:if>

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
            <form action="ProductNameAndPriceLevelSearching" method="POST" class="form-inline search-form" id="search-form">
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
                <form class="product-table" action="ProductsDeleting" method="POST">
                    <table class="table table-striped" style=" box-shadow: 0 5px 15px 2px rgba(0, 0, 0, 0.2);">
                        <thead style="background: #131627; color: #fff;">
                            <tr>
                                <th scope="col">No.</th>
                                <th scope="col">Product Name</th>
                                <th scope="col">Quantity</th>
                                <th scope="col">Price</th>
                                <th scope="col">Category</th>
                                <th scope="col">Status</th>
                                <th scope="col">Detail</th>
                                <th scope="col">Select</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach items="${requestScope.ProductsData}" var="product" varStatus="counter" >
                                <tr>
                                    <th scope="row">${counter.count}</th>
                                    <td>${product.productName}</td>
                                    <td>${product.quantity}</td>
                                    <td>${product.price}</td>
                                    <td>${product.category}</td>
                                    <td>${product.status}</td>
                                    <td>
                                        <a href="ProductDetailLoading?productName=${product.productName}">View</a>
                                    </td>
                                    <td style="text-align: center;">
                                        <input type="checkbox" name="selectedProducts" value="${product.productName}" <c:if test="${product.status.equals('Inactive')}">disabled="disabled"</c:if> />
                                        </td>
                                    </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <button class="products-deleting-btn" type="submit">
                        Delete Selected Items
                        <c:if test="${requestScope.DeleteError != null}">
                            <span style="width: 220px; color: #f00; font-size: 14px; position: absolute; top: 30px; right:50%; transform: translateX(50%);">${requestScope.DeleteError}
                            </span>
                        </c:if>
                    </button>
                    <a class="product-adding-btn" href="product-creating.jsp">Add Product</a>
                </form>

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
            <c:if test="${requestScope.SearchError == null}">
                <c:if test="${requestScope.ProductsData.size() == 0}">
                    <h1 style="color: #f00; text-align: center;">There isn't any data!</h1>
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
        <script src="./scripts/search-handling.js"></script>
    </body>
</html>
