<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../includes/header.jsp"></jsp:include>
<title>Orders info</title>
<div class="container">
    <div class="row d-flex justify-content-center mt-5">
        <h1 class="h2-responsive text-center my-5"><strong>Order info</strong></h1>
        <table id="dtBasicExample" class="my-5 table table-striped table-bordered" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>ProductID</th>
                <th>Name</th>
                <th>Price</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>
                        <c:out value="${product.id}"/>
                    </td>
                    <td>
                        <c:out value="${product.name}"/>
                    </td>
                    <td>
                        <c:out value="${product.price}"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<jsp:include page="../includes/footer.jsp"></jsp:include>
