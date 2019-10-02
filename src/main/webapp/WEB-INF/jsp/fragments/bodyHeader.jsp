<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="messages"/>

<nav class="navbar navbar-light navbar-expand-md justify-content-between" style="background-color: #e1eaed;">
    <fmt:message key="message.login" var="loginMessage"/>
    <c:set var="login" value="<a href='login'>${loginMessage}</a>"/>
    <fmt:message key="message.logout" var="logoutMessage"/>
    <c:set var="logout" value="<a href='logout'>${logoutMessage}</a>"/>
    <fmt:message key="message.book.list" var="bookListMessage"/>
    <c:set var="books" value="<a class='nav-link' href='books?page=0'>${bookListMessage}</a>"/>

    <span class="navbar-brand">
        <img src="img/library.png" class="d-inline-block align-top" alt="">
                <fmt:message key="message.title"/>
    </span>

    <div class="collapse navbar-collapse">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link text-secondary" href=""><fmt:message key="message.home"/></a>
            </li>
            <c:if test="${user != null}">
                <li class="nav-item">
                    ${books}
                </li>
            </c:if>
            <c:if test="${sessionScope.user.hasRole('ADMIN')}">
                <li class="nav-item">
                    <a href="admin/readers" class="nav-link"><fmt:message key="message.readers"/></a>
                </li>
            </c:if>
        </ul>
    </div>

    <span class="form-inline">
        <span class="navbar-brand mb-0 h1"><c:if test="${user != null}">${user.name}</c:if></span>
            <span class="nav-item mb-0 mr-4">${user == null ? login : logout}</span>
            <c:if test="${user == null}"><span class="nav-item mb-0"><a href="register"><fmt:message key="message.register"/></a></c:if>
    </span>

        <span class="form-inline dropdown">
                    <a class="dropdown-toggle nav-link my-1 ml-2" data-toggle="dropdown">${pageContext.response.locale.language}</a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=en">English</a>
                        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=ru">Русский</a>
                        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=uk">Українська</a>
                    </div>
                </span>
</nav>
<div>
    <hr/>
</div>
