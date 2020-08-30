<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../includes/header.jsp"></jsp:include>
<title>History</title>
<div class="container">
    <div class="row d-flex justify-content-center mt-5">
        <h1 class="h2-responsive text-center my-5"><strong>Orders history</strong></h1>
        <table id="dtBasicExample" class="my-5 table table-striped table-bordered" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>OrderID</th>
                <th>Details</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>
                        <c:out value="${order.id}"/>
                    </td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}details">
                            <button name="id" class="btn btn-sm peach-gradient btn-block" value="${order.id}"
                                    type="submit">
                                Details
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<jsp:include page="../includes/footer.jsp"></jsp:include>
