<%@include file="../../../include.jsp"%>

<h1>Emergency Contact</h1>
<form:form commandName="emergencyContactForm" class="brform">
	<fieldset>
		<label for="name">Contact Full Name</label>
		<form:input path="name" id="name" />
		<form:errors path="name" cssClass="error" />
		<br />
		<label for="relationship">Relationship</label>
		<form:input path="relationship" id="relationship" />
		<form:errors path="relationship" cssClass="error" />
		<br />
		<label for="phone">Phone</label>
		<form:input path="phone" id="phone" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="phone" cssClass="error" />
		<br />
		<label for="mobile">Mobile</label>
		<form:input path="mobile" id="mobile" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="mobile" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="Save" />
	<input type="submit" name="next" value="Save & Next" />
</form:form>


<script type="text/javascript">
	$(document).ready(function() {
		$("input[name='name']").focus();
	});
</script>