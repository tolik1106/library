<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container">
    <h2 class="text-center"><fmt:message key="message.book.list"/></h2>
    <div>
        <form action="books/filter" method="get">
            <div class="form-group row">
                <div class="col-sm-3"></div>
                <div class="col-auto">
                    <label for="select" class="col-form-label"><fmt:message key="message.find"/></label>
                </div>
                <div class="col-auto">
                    <select class="form-control" id="select" onchange="changeSearchName(this);">
                        <option name="name" ${name != null ? 'selected' : ''}><fmt:message
                                key="message.select.name"/></option>
                        <option name="author" ${author != null ? 'selected' : ''}><fmt:message
                                key="message.select.author"/></option>
                        <option name="attribute" ${attribute != null ? 'selected' : ''}><fmt:message
                                key="message.select.attribute"/></option>
                    </select>
                </div>
                <div class="col-sm-3">
                    <input id="search" class="form-control" name="name" type="text"
                           value="${name != null ? name : author != null ? author : attribute != null ? attribute : ''}"
                           required>
                </div>
                <div class="col-sm-2">
                    <fmt:message key="message.search" var="search"/>
                    <input type="submit" class="btn btn-info" value="${search}">
                </div>
                <div class="col-sm-2"></div>
            </div>
        </form>
    </div>

    <div>
        <c:if test="${sessionScope.user.hasRole('ADMIN')}">
            <a class="btn btn-primary mb-3" href="admin/save"><fmt:message key="message.book.new"/></a>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${error != null}">
            <h2 style="text-align: center"><fmt:message key="message.notfound.error"/></h2>
        </c:when>
        <c:when test="${books == null || empty books}">
            <h2 style="text-align: center"><fmt:message key="message.empty.library"/></h2>
        </c:when>

        <c:otherwise>
            <div class="table-responsive-sm">
                <table class="table">
                    <thead>
                    <tr>
                        <th><fmt:message key="message.book.name"/></th>
                        <th><fmt:message key="message.book.authors"/></th>
                        <c:if test="${sessionScope.user.hasRole('ADMIN')}">
                            <th><fmt:message key="message.book.case"/></th>
                            <th><fmt:message key="message.book.shelf"/></th>
                        </c:if>
                        <th><fmt:message key="message.action"/></th>
                        <c:if test="${sessionScope.user.hasRole('ADMIN')}">
                            <th colspan="2"></th>
                        </c:if>
                    </tr>

                    </thead>
                    <tbody>
                    <c:forEach items="${books}" var="book">
                        <jsp:useBean id="book" type="com.zhitar.library.domain.Book"/>
                        <tr>
                            <td>${book.name}</td>

                            <td>
                                <c:forEach items="${book.authors}" var="author" varStatus="loop">
                                    ${author.name} ${!loop.last ? ',' : ''}
                                </c:forEach>
                            </td>
                            <c:if test="${sessionScope.user.hasRole('ADMIN')}">
                                <td>${book.bookcase}</td>
                                <td>${book.bookshelf}</td>
                            </c:if>
                            <td>
                                <c:choose>
                                    <c:when test="${book.ownedDate == null}">
                                        <a href="take/${book.id}"><strong><fmt:message
                                                key="message.book.take"/></strong></a>
                                    </c:when>
                                    <c:when test="${empty currentUserBooks}">
                                        <strong>
                                            <fmt:message key="message.book.unavailable"/>
                                        </strong>
                                    </c:when>
                                    <c:otherwise>
                                        <strong>
                                            <jsp:useBean id="now" class="java.util.Date"/>

                                            <fmt:parseNumber
                                                    value="${(now.time - book.ownedDate.time) / (1000 * 60 * 60 * 24)}"
                                                    var="diff" integerOnly="true"/>
                                            <fmt:message key="message.book.toreturn" var="bookToReturn">
                                                <fmt:param value="${30 - diff}"/>
                                            </fmt:message>

                                            <fmt:message key="message.book.unavailable" var="bookUnavailable"/>
                                            <c:out value="${fn:contains(currentUserBooks, book) ?
                                            bookToReturn : bookUnavailable}"/>
                                        </strong>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <c:if test="${sessionScope.user.hasRole('ADMIN')}">
                                <td><c:if test="${book.ownedDate == null}"><a href="admin/edit?id=${book.id}"><img
                                        src="img/edit.png"/></a></c:if></td>
                                <td><c:if test="${book.ownedDate == null}"><a href="admin/delete/${book.id}"><img
                                        src="img/delete.png"/></a></c:if></td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <c:if test="${count != null && count > 10}">
            <nav>
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                        <a class="page-link" href="books?page=${currentPage - 1}" tabindex="-1"><fmt:message key="message.page.previous"/></a>
                    </li>
                    <c:forEach var="page" begin="1" end="${1 + (count - 1) / 10}">
                        <li class="page-item ${(currentPage + 1) == page ? 'active' : ''}">
                            <a class="page-link" href="books?page=${page - 1}">${page}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${((currentPage + 1) > (count - 1) / 10) ? 'disabled': ''}">
                        <a class="page-link" href="books?page=${currentPage + 1}" tabindex="-1"><fmt:message key="message.page.next"/></a>
                    </li>
                </ul>
            </nav>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function changeSearchName(select) {
        var text = select.options[select.selectedIndex].getAttribute("name");
        document.getElementById("search").value = '';
        document.getElementById("search").name = text;
    }
</script>
<script>
    var select = document.getElementById("select");
    var text = select.options[select.selectedIndex].getAttribute("name");
    document.getElementById("search").name = text;
</script>
</body>
</html>
