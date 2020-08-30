<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../includes/header.jsp"></jsp:include>
<title>Product management</title>
<div class="container">
    <h1 class="h2-responsive text-center my-5"><strong>Add product</strong></h1>
    <form class="form-signin" method="post" action="${pageContext.request.contextPath}add">
        <label for="inputName" class="sr-only">Product name</label>
        <input type="text" name="name" id="inputName" class="form-control" placeholder="Product name" required autofocus>
        <label for="inputPrice" class="sr-only">Product price</label>
        <input type="text" name="price" id="inputPrice" class="form-control" placeholder="Product price" required autofocus>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Add product</button>
    </form>
    <h1 class="h2-responsive text-center my-5"><strong>List of all products</strong></h1>
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
                    <a href="${pageContext.request.contextPath}/products/delete?id=${product.getId()}"
                       type="button"  class="btn btn-info btn-sm">Delete
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="../includes/footer.jsp"></jsp:include>
