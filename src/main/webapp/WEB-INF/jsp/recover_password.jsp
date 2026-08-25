<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="util" uri="util" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<form:form  action="${pageContext.request.contextPath}/password/recover.htm" method="post" commandName="recoverPasswordForm">
	<table width ="100%">
		<tr align="center">
			<td>
				<form:errors path="*" cssClass="error" />
			</td>
		</tr>
	</table>
	<table align="center" cellpadding="4" cellspacing="0">
		<tr>
			<td align="right"><label for="username">New Password*</label></td>
			<td width="30"><form:input type="password" id="newPassword" path ="newPassword"/></td>
		</tr>
		<tr><td colspan="3">&nbsp;</td></tr>
		<tr>
			<td align="right"><label for="username">Re-Type New Password*</label></td>
			<td width="30"><form:input type="password" path ="confirmPassword"/></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
					Items with a * are required,
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="submit" />
			</td>
		</tr>
	</table>
</form:form>
<script type="text/javascript"  language="javascript">
	$(document).ready(function() {
		$("#newPassword").focus();
	});
</script>
