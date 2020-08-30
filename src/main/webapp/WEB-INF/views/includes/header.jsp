<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.4.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.16.0/css/mdb.min.css" rel="stylesheet">
    <link href="data:image/png;base64,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"
          rel="icon" type="image/x-icon">
    <title></title>
</head>
<header class="cloudy-knoxville-gradient">
<nav class="navbar navbar-expand-lg navbar-dark peach-gradient">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">Home</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#basicExampleNav"
            aria-controls="basicExampleNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="basicExampleNav">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="${pageContext.request.contextPath}/index/inject-products"> Inject Products
                    <span class="sr-only">(current)</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/shoppingcart">Shopping Cart</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/orders/history">Orders</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/products/all">Products List</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">Admin</a>
                <div class="dropdown-menu dropdown-primary" aria-labelledby="navbarDropdownMenuLink">
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/products/add">Products manage</a>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/users/all">Users</a>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/orders/all">Orders</a>
                </div>
            </li>
        </ul>
        <ul class="navbar-nav ml-auto nav-flex-icons">
            <c:choose>
                <c:when test="${user_id > 0}">
                    <li class="nav-item">
                        <span class="navbar-text text-body mr-3">Welcome ${user_name}</span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-body" href="${pageContext.request.contextPath}/users/logout">Logout</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item">
                        <a class="nav-link text-body" href="${pageContext.request.contextPath}/users/login">Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-body" href="${pageContext.request.contextPath}/users/registration">Registration</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
</header>
</html>