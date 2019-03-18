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
              <h1>Your messages</h1>
              <a href="${contextPath}/welcome">
                  <div class="btn btn-lg btn-primary">Home</div>
              </a>
              <a onclick="document.forms['logoutForm'].submit()">
                  <div class="btn btn-lg btn-primary">
                      Logout
                  </div>
              </a>
          </header>
            <c:choose>
                <c:when test="${messages.size() > 0}">
                    <c:forEach items="${messages}" var="message">
                        <div id="messages-container">
                            <div class="sender">
                                <c:out value="${message.sender.username}"/>
                            </div>
                            <div class="message">
                                <c:out value="${message.content}"/>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <h3>You do not have any messages currently</h3>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
  </div>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
  <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
