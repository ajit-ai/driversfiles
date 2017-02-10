<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" isELIgnored="false"%>
<%@ include file="include.jsp" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<tiles:useAttribute name="title" ignore="true" />
	<title><spring:message code="title" /><c:if test="${!empty title}"> :: <spring:message code="${title}" /></c:if></title>
	<link type="text/css" href="${pageContext.request.contextPath}/resources/css/driversfiles.css" rel="stylesheet" />
	<link type="text/css" href="${pageContext.request.contextPath}/resources/js/jquery-ui/css/humanity/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-ui/js/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/driversfiles.js"></script>
	<script type="text/javascript">
		<tiles:importAttribute name="active_menu" ignore="true" toName="activeMenu" />
		var ACTIVE_MENU = '${activeMenu}';
		<tiles:importAttribute name="active_left_menu" ignore="true" toName="activeLeftMenu" />
		var ACTIVE_LEFT_MENU = '${activeLeftMenu}';
	</script>
</head>
<body>
	<tiles:insertAttribute name="body" />
</body>
</html>
