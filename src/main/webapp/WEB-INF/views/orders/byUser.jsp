<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../includes/header.jsp"></jsp:include>
<title>User orders</title>
<div class="container">
    <h1 class="h2-responsive text-center my-5"><strong>UserID:${id} orders</strong></h1>
    <table id="dtBasicExample" class="mt-5 table table-striped table-bordered" cellspacing="0" width="70%">
        <thead>
        <tr>
            <th>OrderID</th>
            <th>Details</th>
            <th>Delete</th>
        </tr>
        </thead>
        <tbody>
        <jsp:useBean id="orders" scope="request" type="java.util.List"/>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>
                    <c:out value="${order.id}"/>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/orders/details?id=${order.id}"
                       type="button"  class="btn btn-info btn-sm">Details
                    </a>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/orders/delete?id=${order.id}"
                       type="button"  class="btn btn-info btn-sm">Delete
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="../includes/footer.jsp"></jsp:include>