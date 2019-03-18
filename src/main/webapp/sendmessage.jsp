<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create an account ${pageContext.request}</title>
    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <div class="container">
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <div class="container w-50 p-3">
          <header class="d-flex bg-secondary">
              <h1>Send a message</h1>
              <a href="${contextPath}/welcome">
                  <div class="btn btn-lg btn-primary">Home</div>
              </a>
              <a onclick="document.forms['logoutForm'].submit()">
                  <div class="btn btn-lg btn-primary">
                      Logout
                  </div>
              </a>
          </header>
            <h2>Select a user to send a message to</h2>
            <form method="post" class="w-50" class="form-signin">
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}"/>
                <select name="receiverName" class="form-control">
                    <c:forEach items="${users}" var="user">
                        <option value="${user.username}">
                            <c:out value="${user.username}"/>
                        </option>
                    </c:forEach>
                </select>
                <h2>Fill in a message</h2>
                <textarea name="messageContent" class="form-control"></textarea>
                <button type="submit" class="btn btn-lg btn-primary">
                    Send Message
                </button>
            </form>
        </div>
    </c:if>
  </div>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
  <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
