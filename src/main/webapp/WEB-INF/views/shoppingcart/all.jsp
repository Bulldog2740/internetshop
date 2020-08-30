<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../includes/header.jsp"></jsp:include>
<title>Shopping cart</title>
<div class="container">
<h1 class="h2-responsive text-center my-5"><strong>Products in shopping cart</strong></h1>
<table id="dtBasicExample" class="mt-5 table table-striped table-bordered" cellspacing="0" width="100%">
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Delete</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>
                <c:out value="${product.getId()}"/>
            </td>
            <td>
                <c:out value="${product.getName()}"/>
            </td>
            <td>
                <c:out value="${product.getPrice()}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/shoppingcart/products/delete?id=${product.getId()}"
                   type="button"  class="btn btn-info btn-sm">Delete
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
    <button class="btn btn-lg btn-success btn-block" onclick="document.location='/orders/create'" type="submit">Order</button>
</div>
<jsp:include page="../includes/footer.jsp"></jsp:include>
