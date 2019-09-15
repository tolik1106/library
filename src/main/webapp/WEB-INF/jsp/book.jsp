<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container">
    <fmt:message key="message.book.new" var="addNewBook"/>
    <fmt:message key="message.book.edit" var="editBook"/>
<h2 class="text-center">${book.id == null ? addNewBook : editBook}</h2>

<c:set var="save" value="admin/save"/>
<c:set var="edit" value="admin/edit"/>
<form id="book" name="book" action="${book.id == null ? save : edit}" method="post">
    <c:if test="${book.id != null}"><input type="hidden" name="id" value="${book.id}"></c:if>
    <div class="form-group row">
        <label for="name" class="col-sm-2 col-form-label"><fmt:message key="message.book.name"/></label>
        <div class="col-sm-6">
        <input id="name" name="name" type="text" class="form-control" value="${book.name}" size="50" maxlength="100" required>
            <small class="form-text text-danger collapse ${booknameError != null ? 'show' : ''}"><fmt:message key="${booknameError}"/></small>
        </div>
    </div>
    <div class="form-group row">
        <label for="bookcase" class="col-sm-2 col-form-label"><fmt:message key="message.book.case"/>:</label>
        <div class="col-sm-2">
        <input id="bookcase" name="bookcase" class="form-control" type="number" value="${book.bookcase}" required>
            <small class="form-text text-danger collapse ${bookbookcaseError != null ? 'show' : ''}"><fmt:message key="${bookbookcaseError}"/></small>

        </div>
        <label for="bookshelf" class="col-sm-2 col-form-label"><fmt:message key="message.book.shelf"/>:</label>
        <div class="col-sm-2">
        <input id="bookshelf" name="bookshelf" class="form-control" type="number" value="${book.bookshelf}" required>
            <small class="form-text text-danger collapse ${bookbookshelfError != null ? 'show' : ''}"><fmt:message key="${bookbookshelfError}"/></small>

        </div>
    </div>

    <div class="form-group">
        <label for="authors"><fmt:message key="message.book.authors.enter"/></label>
        <small class="form-text text-danger collapse ${authornameError != null ? 'show' : ''}"><fmt:message key="${authornameError}"/></small>
        <textarea id="authors" name="authors" class="form-control col-sm-8" form="book" rows="4" required><c:forEach items="${book.authors}" var="author" varStatus="loop"><c:out value="${author.name}${!loop.last ? ', ' : ''}"/></c:forEach></textarea>
    </div>

    <div class="form-group">
        <label for="attributes"><fmt:message key="message.book.attributes.enter"/></label>
        <small class="form-text text-danger collapse ${attributenameError != null ? 'show' : ''}"><fmt:message key="${attributenameError}"/></small>
        <textarea id="attributes" name="attributes" class="form-control col-sm-8" form="book" rows="4" cols="30" required><c:forEach items="${book.attributes}" var="attribute" varStatus="loop">${attribute.name}${!loop.last ? ', ' : ''}</c:forEach></textarea>
    </div>

    <fmt:message key="message.book.save" var="bookSave"/>
    <input type="submit" class="btn btn-success" value="${book.id == null ? bookSave : editBook}">
</form>
</div>
</body>
</html>
