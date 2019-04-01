<%--
  Created by IntelliJ IDEA.
  User: mcoppieters
  Date: 31/03/19
  Time: 13:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header id="login-bar" class="bg-secondary">
    <h2>Welcome ${pageContext.request.userPrincipal.name}</h2>
    <div>
        <a onclick="document.forms['logoutForm'].submit()">
            <div class="btn btn-lg btn-primary">
                Home
            </div>
        </a>
        <a onclick="document.forms['logoutForm'].submit()">
            <div class="btn btn-lg btn-primary">
                Logout
            </div>
        </a>
    </div>
</header>
