<%@include file="../../../include.jsp"%>

<script type="text/javascript">
$(document).ready(function() {
	$("#expiration").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
});
</script>

<h1>CDL Information</h1>


<form:form commandName="cdlInformationForm" action="${pageContext.request.contextPath}/secure/driver/mydata/cdl_information" class="brform" >
	<fieldset>
		<c:if test="${!empty cdlInformationForm.uuid}">
		<form:hidden path="uuid" id="uuid" />
		</c:if>

		<label for="state">State</label>
		<form:select path="state" items="${states}" itemLabel="name" itemValue="code" id="state" />
		<form:errors path="state" cssClass="error" />
		<br />

		<label for="number">Number</label>
		<form:input path="number" id="number" />
		<form:errors path="number" cssClass="error" />
		<br />

		<label for="type">Type</label>
		<form:select path="type" items="${licensetypes}" itemLabel="name" id="type" />
		<form:errors path="type" cssClass="error" />
		<br />

		<label for="expiration">Expiration Date</label>
		<form:input path="expiration" id="expiration" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="expiration" cssClass="error" />
		<br />

		<label for="current">This is my current CDL</label>
		<form:checkbox path="current" id="current" />
		<br />
	</fieldset>
	<input type="submit" value="Save" />
	<button onclick="$(location).attr('href', '${pageContext.request.contextPath}/secure/driver/mydata/cdl_information'); return false;">Cancel</button>
</form:form>

<br />

