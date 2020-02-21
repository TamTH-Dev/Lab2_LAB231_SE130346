<%-- 
    Document   : login
    Created on : Feb 20, 2020, 6:59:38 PM
    Author     : hoang
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Blogs World</title>
        <link href="https://fonts.googleapis.com/css?family=Poppins:400,500,700|Roboto:400,500&display=swap"
              rel="stylesheet" />
        <link rel="stylesheet" href="./styles/all.css" />
        <link rel="stylesheet" href="./styles/login.css" />
    </head>
    <body>
        <div class="login-page">
            <form class="login-form" id="login-form" method="POST" action="Login">
                <h3 class="form-title">LOGIN</h3>
                <div class="form-input">
                    <div>Email</div>
                    <span class="icon"><i style="color: #666;" class="fas fa-user"></i></span>
                    <input type="text" id="email" name="email" class="input-field" value="${param.email}" />
                    <span class="error" id="email-error"><c:if test="${requestScope.InvalidAccount != NULL}">${requestScope.InvalidAccount}</c:if></span>
                </div>
                <div class="form-input">
                    <div>Password</div>
                    <span class="icon"><i style="color: #666;" class="fas fa-key"></i></span>
                    <input type="password" id="password" name="password" class="input-field" />
                    <span class="error" id="password-error"></span>
                </div>
                <div class="new-account-creation">
                    <span>If you're a new one, <a href="register.jsp">Create new account!</a></span>
                </div>
                <button type="submit" name="action" value="Login" id="login-btn">Login</button>
                <a class="home-back" href="index.jsp">Back to Home Page</a>
            </form>
        </div>

        <script src="./scripts/all.js"></script>
        <script src="./scripts/login-handling.js"></script>
    </body>
</html>
