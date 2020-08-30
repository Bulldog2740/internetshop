<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<jsp:include page="../includes/backgroundVideo.jsp"/>
<title>Registration</title>
<body class="text-center">
<form class="form-signin" method="post" action="${pageContext.request.contextPath}registration">
    <h1 class="h3 mb-3 font-weight-normal" style="color: #efefef">Sign up</h1>
    <label for="Name" class="sr-only">Name</label>
    <input type="text" name="name" id="Name" class="form-control" placeholder="Name" required>
    <label for="inputEmail" class="sr-only">Email address</label>
    <input type="text" name="login" id="inputEmail" class="form-control" placeholder="Login" required autofocus>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" name="pass" id="inputPassword" class="form-control" placeholder="Password" required>
    <label for="inputRepPassword" class="sr-only">Repeat password</label>
    <input type="password" name="rep-pass" id="inputRepPassword" class="form-control" placeholder="Repeat password" required>
    <div class="checkbox mb-3">
        <p style="color: #ff0000">${message}</p>
        <p><a href="${pageContext.request.contextPath}login" style="color: #f0d60c">Have account? Login here</a></p>
    </div>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign up</button>
    <p class="mt-5 mb-3" style="color: #efefef">&copy; satellite-shop 2020</p>
</form>
</body>
</html>
