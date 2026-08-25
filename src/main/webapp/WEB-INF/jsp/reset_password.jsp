<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="util" uri="util" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<form:form  action="${pageContext.request.contextPath}/password/reset.htm" method="post" commandName="passwordResetForm">
	<table width ="100%">
		<tr align="center">
			<td>
				<form:errors path="*" cssClass="error" />
			</td>
		</tr>
	</table>
	<table align="center" cellpadding="4" cellspacing="0">
		<c:if test="${param.error == 'invalidToken'}">
			<tr>
				<td>&nbsp;</td>
				<td colspan="2" class="error">
					<spring:message code="resetPasswordinvalidToken" />
				</td>
			</tr>
			<tr><td colspan="3">&nbsp;</td></tr>
		</c:if>
		<tr>
			<td align="right"><label for="username">Email<t:required/></label></td>
			<td width="225"><form:input type="text" name="j_username" id="username" path ="email"/></td>
		</tr>
		<tr><td colspan="3">&nbsp;</td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">Type the characters you see in the picture below.</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<img src="${pageContext.request.contextPath}/captcha/image.htm" width="300" height="100" />
			</td>
		</tr>

		<tr>
			<td align="right">Word Verification<t:required/></td>
			<td colspan="2">
				<form:input name="captcha" type="text" path="captcha"/>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
					Letters are not case-sensitive.
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
					Items with a <t:required/> are required,
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
		$("#username").focus();
	});
</script>
