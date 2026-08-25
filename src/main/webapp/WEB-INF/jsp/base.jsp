<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" isELIgnored="false"%>
<%@ include file="include.jsp" %>

<!DOCTYPE html>

<html lang="en">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<title><spring:message code="title" /><c:if test="${!empty title}"> :: <spring:message code="${title}" /></c:if></title>
	<link type="text/css" href="${pageContext.request.contextPath}/resources/vendor/bootstrap.min.css" rel="stylesheet" />
	<link type="text/css" href="${pageContext.request.contextPath}/resources/css/driversfiles.css" rel="stylesheet" />
	<link type="text/css" href="${pageContext.request.contextPath}/resources/js/jquery-ui/css/humanity/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-ui/js/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/driversfiles.js"></script>
	<script type="text/javascript">
		var ACTIVE_MENU = '${activeMenu}';
		var ACTIVE_LEFT_MENU = '${activeLeftMenu}';
		var CSRF_TOKEN = '${_csrf != null ? _csrf.token : ""}';
		var CSRF_HEADER = '${_csrf != null ? _csrf.headerName : "X-CSRF-TOKEN"}';
		if (window.jQuery && CSRF_TOKEN) {
			jQuery.ajaxSetup({ beforeSend: function(xhr) { xhr.setRequestHeader(CSRF_HEADER, CSRF_TOKEN); } });
		}
	</script>
</head>
<body>
	<jsp:include page="${body}"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/vendor/bootstrap.bundle.min.js"></script>
</body>
</html>
