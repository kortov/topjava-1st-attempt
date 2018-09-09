<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<fmt:setBundle basename="messages.app"/>
<header><a href="${pageContext.request.contextPath}/"><fmt:message key="app.home"/></a>&nbsp;|&nbsp;<a
        href="meals"><fmt:message key="app.title"/></a>
    <header>
        <a href="${pageContext.request.contextPath}/"><spring:message code="app.home"/></a>&nbsp;|&nbsp;<a href="meals"><spring:message
            code="app.title"/></a>
    </header>