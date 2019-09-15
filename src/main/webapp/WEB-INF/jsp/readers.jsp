<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container">
    <div class="text-center h2"><fmt:message key="message.readers"/></div>
<c:choose>
    <c:when test="${users == null}">
        <h2><fmt:message key="message.readers.empty"/></h2>
    </c:when>
            <c:otherwise>
                <ul>
                    <c:forEach items="${users}" var="user">
                        <jsp:useBean id="user" type="com.zhitar.library.domain.User"/>
                        <li class="font-weight-bold">${user.name}</li>
                        <ol>
                            <c:forEach items="${user.books}" var="book">
                                <jsp:useBean id="book" type="com.zhitar.library.domain.Book"/>
                                <li>${book.name}&emsp;
                                    <form style="display: inline-block;" method="post" name="returnBook" action="admin/readers">
                                    <input type="hidden" name="bookId" value="${book.id}">
                                    <input type="hidden" name="userId" value="${user.id}">
                                        <fmt:message key="message.book.return" var="returnMessage"/>
                                    <input type="submit" class="btn btn-outline-success btn-sm" value="${returnMessage}">
                                </form>
                            </li>
                            </c:forEach>
                        </ol>
                    </c:forEach>
                </ul>
            </c:otherwise>
</c:choose>
</div>
</body>
</html>
