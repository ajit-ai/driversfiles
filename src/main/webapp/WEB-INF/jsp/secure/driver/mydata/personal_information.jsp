<%@include file="../../../include.jsp"%>

<SCRIPT type="text/javascript">
$(document).ready(function() {
	$("#dob").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
});
</SCRIPT>


<h1>Personal Information</h1>
<form:form commandName="personalInformationForm" cssClass="brform">
	<fieldset>
		<label for="firstName">First Name</label>
		<form:input path="firstName" id="firstName" />
		<form:errors path="firstName" cssClass="error" />
		<br />
		<label for="middleName">Middle Name</label>
		<form:input path="middleName" id="middleName" />
		<form:errors path="middleName" cssClass="error" />
		<br />
		<label for="lastName">Last Name</label>
		<form:input path="lastName" id="lastName" />
		<form:errors path="lastName" cssClass="error" />
		<br />
		<label for="email">Email</label>
		<form:input path="email" id="email" />
		<form:errors path="email" cssClass="error" />
		<br />
		<label for="dob">Date Of Birth</label>
		<form:input path="dob" id="dob" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="dob" cssClass="error" />
		<br />
		<label for="ssn">Social Security Number</label>
		<form:input path="ssn" id="ssn" />
		<form:errors path="ssn" cssClass="error" />
		<br />
		<label for="phone">Phone</label>
		<form:input path="phone" id="phone" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="phone" cssClass="error" />
		<br />
		<label for="mobile">Mobile</label>
		<form:input path="mobile" id="mobile" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="mobile" cssClass="error" />
		<br />
		<label for="fax">Fax</label>
		<form:input path="fax" id="fax" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="fax" cssClass="error" />
		<br />
		<label for="address1">Address</label>
		<form:input path="address1" id="address1" />
		<form:errors path="address1" cssClass="error" />
		<br />
		<label for="address2">&nbsp;</label>
		<form:input path="address2" id="address2" />
		<form:errors path="address2" cssClass="error" />
		<br />
		<label for="city">City</label>
		<form:input path="city" id="city" />
		<form:errors path="city" cssClass="error" />
		<br />
		<label for="state">State</label>
		<form:select path="state" items="${states}" itemLabel="name" itemValue="code" id="state" />
		<form:errors path="state" cssClass="error" />
		<br />
		<label for="postalCode">Postal Code</label>
		<form:input path="postalCode" id="postalCode" />
		<form:errors path="postalCode" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="Save" />
	<input type="submit" name="next" value="Save & Next" />
</form:form>

<script type="text/javascript" language="javascript">
	$(document).ready(function() {
		$("input[name='firstName']").focus();
	});
</script>