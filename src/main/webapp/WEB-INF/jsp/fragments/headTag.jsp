<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}" />
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages" scope="session"/>
<head>
    <meta http-equiv="Content-Type" content="text/html: charset=UTF-8">

    <title><fmt:message key="message.title"/></title>
    <base href="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}/">
    <link rel="stylesheet" href="webjars/bootstrap/4.3.1/css/bootstrap.min.css">

    <script type="text/javascript" src="webjars/jquery/3.0.0/jquery.min.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/4.3.1/js/bootstrap.min.js" defer></script>
</head>
