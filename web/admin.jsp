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

        <c:if test="${param.searchedContent == null}">
            <c:if test="${requestScope.ProductsData == null}">
                <c:redirect url="DataLoading" />
            </c:if>
        </c:if>

        <header>
            <nav class="nav-app">
                <div class="logo">
                    <a href="index.jsp"><img src="./images/logo.png" alt=""></a>
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
                        <li><button><a class="header-btn" href="LogoutController">Sign Out</a></button></li>
                                </c:if>
                </ul>
            </nav>
        </header>

        <div class="container">
            <c:if test="${requestScope.ProductsData != null}">
                <c:if test="${requestScope.DeleteError != null}"><span style="color: #f00; font-size: 40px;">${requestScope.DeleteError}</span></c:if>
                <form class="blog-table" action="ProductsDeleting" method="POST">
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
                                        <c:url var="handleBlogDetail" value="MainController">
                                            <c:param name="action" value="getBlogDetail" />
                                            <c:param name="blogID" value="${blog.blogID}" />
                                        </c:url>
                                        <a href="${handleBlogDetail}">View</a>
                                    </td>
                                    <td style="text-align: center;">
                                        <input type="checkbox" name="selectedProducts" value="${product.productName}" <c:if test="${product.status.equals('Inactive')}">disabled="disabled"</c:if> />
                                        </td>
                                    </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <button class="delete-articles-btn" type="submit" name="action" value="deleteArticles">Delete Selected Items</button>
                </form>

                <div class="page-container">
                    <c:forEach var = "index" begin = "1" end = "${requestScope.TotalPage}">
                        <c:url value="DataLoading" var="handlePage">
                            <c:if test="${param.searchedContent == null && param.searchedArticle == null && paramValues.searchedStatus == null}">
                                <c:param value="loadData" name="action" />
                            </c:if>
                            <c:param value="${index}" name="pg" />
                        </c:url>
                        <a class="page-item <c:if test="${param.pg == index}">active</c:if>" href="${handlePage}">${index}</a>
                    </c:forEach>
                </div>

            </c:if>
            <c:if test="${requestScope.SearchError == null}">
                <c:if test="${requestScope.ProductsData.size() == 0}">
                    <h1 style="color: #f00; text-align: center; padding-bottom: 30px;">There isn't any data!</h1>
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
        <script>
            const articleFilter = document.getElementById('article-filter')
            const statusFilter = document.getElementById('status-filter')
            const articleFilterContainer = document.getElementById('article-filter-container')
            const statusFilterContainer = document.getElementById('status-filter-container')
            const article = document.getElementById('article')
            const st = document.getElementsByClassName('st')

            if (${param.articleFilter != null}) {
                articleFilterContainer.style.display = 'block'
            } else {
                articleFilterContainer.style.display = 'none'
            }
            if (${param.statusFilter != null}) {
                statusFilterContainer.style.display = 'block'
            } else {
                statusFilterContainer.style.display = 'none'
            }

            articleFilter.addEventListener('click', () => {
                if (articleFilter.checked) {
                    articleFilterContainer.style.display = 'block'
                } else {
                    articleFilterContainer.style.display = 'none'
                    article.value = ''
                }
            })
            statusFilter.addEventListener('click', () => {
                if (statusFilter.checked) {
                    statusFilterContainer.style.display = 'block'
                } else {
                    statusFilterContainer.style.display = 'none'
                    for (let i = 0; i < st.length; i++) {
                        st[i].checked = false
                    }
                }
            })
        </script>

    </body>
</html>
