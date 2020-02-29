<%-- 
    Document   : error
    Created on : Jan 19, 2020, 3:06:15 PM
    Author     : hoang
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>ERROR PAGE</h1>
        <c:if test="${ERROR != null}">
            <h1 style="color: #f00; text-align: center;">${requestScope.ERROR}</h1>
        </c:if>
    </body>
</html>
