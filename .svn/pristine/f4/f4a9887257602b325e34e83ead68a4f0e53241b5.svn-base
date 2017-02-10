<%@include file="../../include.jsp"%>

<h1>My Acount</h1>

<form:form commandName="myAccountForm" action="${pageContext.request.contextPath}/secure/common/account/save" class="brform">
	<form:hidden path="uuid" />
	<fieldset>
		<label for="firstName">First Name<t:required /></label>
		<form:input path="firstName" id="firstName" />
		<form:errors path="firstName" cssClass="error" />
		<br />

		<label for="lastName">Last Name<t:required /></label>
		<form:input path="lastName" id="lastName" />
		<form:errors path="lastName" cssClass="error" />
		<br />

		<label for="email">Email<t:required /></label>
		<form:input path="email" id="email" />
		<form:errors path="email" cssClass="error" />
		<br />
		
		<hr />
		<br />
		
		<security:authorize ifNotGranted="ROLE_PREVIOUS_ADMINISTRATOR">
			<label for="currentPassword">Current Password</label>
			<form:password path="currentPassword" id="currentPassword" />&nbsp;(Leave empty if not changing password)
			<form:errors path="currentPassword" cssClass="error" />
			<br />
		</security:authorize>

		<label for="newPassword">New Password</label>
		<form:password path="newPassword" id="newPassword" />&nbsp;(Leave empty if not changing password)
		<form:errors path="newPassword" cssClass="error" />
		<br />
		<label for="confirmPassword">Confirm Password</label>
		<form:password path="confirmPassword" id="confirmPassword" />
		<form:errors path="confirmPassword" cssClass="error" />
		<br />

	</fieldset>
	<input type="submit" value="Save" />
	<button onclick="$(location).attr('href', '${pageContext.request.contextPath}/secure/entry'); return false;">Cancel</button>
</form:form>

