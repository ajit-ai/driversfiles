<%@include file="include.jsp"%>

<h1>Sign Up</h1>
<div>
	${util:getContent("SIGNUP")}
</div>
<form:form commandName="signUpForm" cssClass="brform">
	<fieldset>
		<label for="firstName">First Name<t:required /></label>
		<form:input path="firstName" id="firstName" />
		<form:errors path="firstName" cssClass="error" />
		<br />
		<label for="middleName">Middle Name</label>
		<form:input path="middleName" id="middleName" />
		<form:errors path="middleName" cssClass="error" />
		<br />
		<label for="lastName">Last Name<t:required /></label>
		<form:input path="lastName" id="lastName" />
		<form:errors path="lastName" cssClass="error" />
		<br />
		<label for="email1">Email Address<t:required /></label>
		<form:input path="email1" id="email1" />
		<form:errors path="email1" cssClass="error" />
		<br />
		<label for="email2">Re-Type Email Address<t:required /></label>
		<form:input path="email2" id="email2" />
		<form:errors path="email2" cssClass="error" />
		<br />
		<label for="password1">Password<t:required /></label>
		<form:password path="password1" id="password1" />
		<form:errors path="password2" cssClass="error" />
		<br />
		<label for="password2">Re-Type Password<t:required /></label>
		<form:password path="password2" id="password2" />
		<form:errors path="password2" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="Sign Up" />
	<br />
</form:form>