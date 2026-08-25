<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="util" uri="util" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<h1>Sign In</h1>
<form action="${pageContext.request.contextPath}/auth" method="post" class="brform">
	\t<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	<c:if test="${param.error != null}">
		<div class="error"><spring:message code="login.failure" /></div><br />
	</c:if>
	<c:if test="${param.logout != null}">
		<div class="success"><spring:message code="login.success" /></div><br />
	</c:if>
	<fieldset>
		<label for="username">Email</label>
		<input type="text" name="j_username" id="username" />
		<br />
		<label for="password">Password</label>
		<input type="password" name="j_password" id="password" />
		<br />
	</fieldset>
	<input type="submit" value="Sign In" />
	<br />
	<br />
	<div><a href="${pageContext.request.contextPath}/signup">Not a member? Sign Up Now!</a>.</div>
	<br />
	<div><a href="${pageContext.request.contextPath}/password/reset" class="b0rk">Forgot your password?</a></div>
</form>

<script language="javascript" type="text/javascript">
	// Make username field selected on page load
	$(document).ready(function() {
		$("input[name=j_username]").focus();
	});
</script>
