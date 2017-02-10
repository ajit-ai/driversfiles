<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="util" uri="util" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Page Not Found (404)</title>
	<link type="text/css" href="${pageContext.request.contextPath}/resources/css/driversfiles.css" rel="stylesheet" />
	<link type="text/css" href="${pageContext.request.contextPath}/resources/js/jquery-ui/css/humanity/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-ui/js/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/driversfiles.js"></script>
</head>
<body>


<div id="wrapper">
	<div id="header">
		<div id="header-login">
		</div>
		<div id="header-menu">
			<ul>
				<li><a href="${pageContext.request.contextPath}/secure/entry" id="menu-home">Home</a></li>
			</ul>
		</div>
	</div>
	<div id="secondary-menu">
	</div>
	<div id="content">
		
		<h1>Unavailable</h1>
		<p>Oops! The page that you tried to access is unavailable. If this problem persists please report it to the webmaster.</p>
		
		<br class="clear" />
	</div>
	<div id="footer">
		Copyright &copy; 2011 - Drivers Files, Inc.
	</div>
</div>



</body>
</html>
