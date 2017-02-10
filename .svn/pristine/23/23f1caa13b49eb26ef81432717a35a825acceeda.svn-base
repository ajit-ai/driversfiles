<%@include file="../../include.jsp"%>

<h1>
	Edit User
</h1>

<form:form commandName="personForm" class="brform">
	<fieldset>
		<label for="firstName"><t:required />First Name</label>
		<form:input path="firstName" id="firstName" />
		<br />

		<label for="lastName"><t:required />Last Name</label>
		<form:input path="lastName" id="lastName" />
		<form:errors path="lastName" cssClass="error" />
		<br />

		<label for="email"><t:required />Email</label>
		<form:input path="email" id="email" />
		<form:errors path="email" cssClass="error" />
		<br />

		<label for="password1">Password</label>
		<form:password path="password1" id="password1" />
		<form:errors path="password1" cssClass="error" />
		<br />

		<label for="password2">Re-type Password</label>
		<form:password path="password2" id="password2" />
		<form:errors path="password2" cssClass="error" />
		<br />

		<label for="type"><t:required />Type</label>
		<form:select path="type" items="${personTypes}" id="type" />
		<form:errors path="type" cssClass="error" />
		<br />

		<div id="company">
			<label for="companyName"><t:required />Company Name</label>
			<form:input path="companyName" id="companyName" />
			<form:errors path="companyName" cssClass="error" />
			<br />

			<label for="companyNumber"><t:required />Company Number</label>
			<form:input path="companyNumber" id="companyNumber" />
			<form:errors path="companyNumber" cssClass="error" />
			<br />
		</div>
	</fieldset>
	<input type="submit" name="submit" value="Submit" />
	<button onclick="$(location).attr('href', '${pageContext.request.contextPath}/secure/admin/users'); return false;">Cancel</button>
</form:form>

<script type="text/javascript" language="javascript">
	function toggleCompany() {
		if ($("#type").val() == 'COMPANY') {
			$("#company").show();
		} else {
			$("#company").hide();
		}
	}
	$(document).ready(function() {
		$("#type").click(function() {
			toggleCompany();
		});
		toggleCompany();
	});
	$("input[name='firstName']").focus();
</script>