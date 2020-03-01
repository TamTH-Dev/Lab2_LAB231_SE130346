<%-- 
    Document   : register
    Created on : Feb 20, 2020, 7:06:08 PM
    Author     : hoang
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link href="https://fonts.googleapis.com/css?family=Poppins:400,500,700|Roboto:400,500&display=swap"
              rel="stylesheet">
        <link rel="stylesheet" href="./styles/register.css">
    </head>
    <body>
        <div class="signup-form-container">
            <form id="register-form" action="Register" method="POST">
                <h3>Sign Up</h3>
                <div class="form-item">
                    <label for="email">Email</label>
                    <input id="email" type="text" name="email" value="${param.email}" />
                    <span class="error" id="email-error"><c:if test="${requestScope.DuplicateError != NULL}">${requestScope.DuplicateError} </c:if></span>
                    </div>
                    <div class="form-item">
                        <label for="name">User Name</label>
                        <input id="username" type="text" name="name" value="${param.name}" />
                    <span class="error" id="username-error"></span>
                </div>
                <div class="form-item">
                    <label for="password">Password</label>
                    <input id="password" type="password" name="password" />
                    <span class="error" id="password-error"></span>
                </div>
                <div class="form-item">
                    <label for="password-confirm">Confirm Password</label>
                    <input id="password-confirm" type="password" name="passwordConfirm" />
                    <span class="error" id="password-confirm-error"></span>
                </div>
                <button id="register-btn" type="submit" value="Register">Sign Up</button>
                <a class="home-back" href="index.jsp">Back to Home Page</a>
            </form>
        </div>

        <script src="./scripts/register-handling.js"></script>
    </body>
</html>

