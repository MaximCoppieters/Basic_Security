<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create an account ${pageContext.request}</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/main.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <div class="container app">
            <div class="row app-one" id="page">
                <jsp:include page="header.jsp" />
                <div class="col-sm-4 side">
                    <div class="side-one">
                        <!-- Heading -->
                        <div class="row heading">
                            <div class="col-sm-3 col-xs-3 heading-avatar">
                                <div class="heading-avatar-icon">
                                    <img src="img/man-2-512.png">
                                </div>
                            </div>
                            <div class="col-sm-3 col-xs-3 heading-avatar">
                                ${activeUser}
                            </div>
                            <div class="col-sm-1 col-xs-1  heading-dot  pull-right">
                                <i class="fa fa-ellipsis-v fa-2x  pull-right" aria-hidden="true"></i>
                            </div>
                            <div class="col-sm-2 col-xs-2 heading-compose  pull-right">
                                <i class="fa fa-comments fa-2x  pull-right" aria-hidden="true"></i>
                            </div>
                        </div>
                        <!-- Heading End -->
                        <div class="sidebar-main">
                            <c:forEach items="${users}" var="user">
                                <div class="row sideBar-body">
                                    <form onclick="changeCorrespondent(this)">
                                        <input name="correspondent-name" value="${user.username}" hidden>
                                        <div class="col-sm-3 col-xs-3 sideBar-avatar">
                                            <div class="avatar-icon">
                                                <img src="img/man-2-512.png">
                                            </div>
                                        </div>
                                        <div class="col-sm-9 col-xs-9 sideBar-main">
                                            <div class="row">
                                                <div class="col-sm-8 col-xs-8 sideBar-name">
                                                    <span class="name-meta">${user.username} </span>
                                                </div>
                                                <div class="col-sm-4 col-xs-4 pull-right sideBar-time">
                                                    <span class="time-meta pull-right">${user.getLastOnline()}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </c:forEach>
                        </div>
                        <!-- Sidebar End -->
                    </div>
                    <!-- Sidebar End -->
                </div>


                <!-- New Message Sidebar End -->

                <!-- Conversation Start -->
                <div class="col-sm-8 conversation">
                    <!-- Heading -->
                    <c:if test="${not empty correspondent}">
                    <div class="row heading">
                        <div class="col-sm-2 col-md-1 col-xs-3 heading-avatar">
                            <div class="heading-avatar-icon">
                                <img src="img/man-2-512.png">
                            </div>
                        </div>
                        <div class="col-sm-8 col-xs-7 heading-name">
                            <a class="heading-name-meta">${correspondent.username}
                            </a>
                            <span>Online</span>
                        </div>
                        <div class="col-sm-1 col-xs-1  heading-dot pull-right">
                            <i class="fa fa-ellipsis-v fa-2x  pull-right" aria-hidden="true"></i>
                        </div>
                    </div>
                    <!-- Heading End -->

                    <!-- Message Box -->
                    <div class="row message" id="conversation">
                        <c:forEach items="${messages}" var="message">
                            <div class="row message-body">
                                <div class=${message.receiver.username.equals(activeUser) ? 'col-sm-12 message-main-receiver' : 'col-sm-12 message-main-sender'}">
                                    <div class="${message.receiver.username.equals(activeUser) ? 'receiver' : 'sender'}">
                                        <div class="message-text">
                                                ${message.content}
                                        </div>
                                        <c:if test="${not empty message.appendix}">
                                            <a href="${message.getPublicAppendixFilePath()}">
                                                ${message.appendixFileName}
                                            </a>
                                        </c:if>
                                        <span class="message-time pull-right"> ${message.getDaySent()} </span>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
            <div class="row chat-box">
                <form method="post" id="reply-message" enctype="multipart/form-data">
                    <input type="hidden"
                           name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                    <div id="reply-row">
                        <input id="message-input" name="messageContent" class="form-control">
                        <input name="receiverName" hidden>
                        <input type="file" name="appendix" multiple>
                        <button type="submit" class="btn btn-primary">
                            Send
                        </button>
                    </div>
                </form>
            </div>
                </div>
                    </c:if>
            </div>
    </c:if>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<script src="${contextPath}/resources/js/scripts.js"></script>
</body>
</html>
