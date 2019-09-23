<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="container">

    <fmt:message key="message.register.title" var="registerTitle"/>
<div class="text-center mt-5">${register ? registerTitle : ''}</div>
    <div class="row justify-content-center">
        <div class="col-md-4 col-sm-6">
    <form action="${register ? 'register' : 'login'}" method="post">
        <c:if test="${register}">
        <div class="form-group">
            <label for="name"><fmt:message key="message.name"/></label>
            <input id="name" class="form-control" name="name" type="text"
                   value="${registeredUser != null ? registeredUser.name : ''}" required>
            <small class="form-text text-danger collapse ${usernameError != null ? 'show' : ''}"><fmt:message key="${usernameError}"/></small>
        </div>
        </c:if>
        <div class="form-group">
            <label for="email"><fmt:message key="message.email"/></label>
            <input id="email" name="email" class="form-control" type="email" value="${registeredUser != null ? registeredUser.email : ''}"
                   placeholder="user@email.com" required>
            <small class="form-text text-danger collapse ${useremailError != null ? 'show' : ''}"><fmt:message key="${useremailError}"/></small>
            <small class="form-text text-danger collapse ${duplicateEmailError != null ? 'show' : ''}"><fmt:message key="${duplicateEmailError}">
                <fmt:param value="${duplicateEmail}"/></fmt:message></small>
            <small class="form-text text-danger collapse ${error != null ? 'show': ''}">
                <fmt:message key="message.emailnotfound.error">
                    <fmt:param value="${email}"/>
                </fmt:message>
            </small>
        </div>
        <div class="form-group">
            <label for="password"><fmt:message key="message.password"/></label>
            <input id="password" name="password" class="form-control" type="password" required>
            <small class="form-text text-danger collapse ${userpasswordError != null ? 'show' : ''}"><fmt:message key="${userpasswordError}"/></small>
            <%--<small class="form-text text-danger collapse ${wrongPasswordError != null ? 'show' : ''}"><fmt:message key="${}" </small>--%>
        </div>
        <c:if test="${register}">
        <div class="form-group">
            <label for="phone"><fmt:message key="message.phone"/></label>
            <input id="phone" name="phone" class="form-control" type="number" value="${registeredUser != null ? registeredUser.phone : ''}"
                   placeholder="0951234567">
            <small class="form-text text-danger collapse ${userphoneError != null ? 'show' : ''}"><fmt:message key="${userphoneError}"/></small>
        </div>
        </c:if>
        <fmt:message key="message.register" var="messageRegister"/>
            <fmt:message key="message.login" var="messageLogin"/>
            <input type="submit" class="btn btn-primary" value="${register ? messageRegister : messageLogin}">
</form>
        </div>
    </div>

</div>

</body>
</html>
