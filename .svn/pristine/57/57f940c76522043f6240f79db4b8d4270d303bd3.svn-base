<%@include file="../../../include.jsp"%>

<SCRIPT type="text/javascript">
$(document).ready(function() {
	$("#fromDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
	$("#toDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
});
</SCRIPT>

<c:choose>
	<c:when test="${!empty employmentId}">
		<h1>Edit Employment Record</h1>
	</c:when>
	<c:otherwise>
		<h1>Add Employment Record</h1>
	</c:otherwise>
</c:choose>


<form:form commandName="employmentForm" action="${pageContext.request.contextPath}/secure/driver/mydata/employment_history" class="brform">
	<fieldset>
		<c:if test="${!empty employmentForm.uuid}">
		<form:hidden path="uuid" id="uuid" />
		</c:if>

		<label for="name">Company Name</label>
		<form:input path="name" id="name" />
		<form:errors path="name" cssClass="error" />
		<br />

		<label for="supervisor">Supervisor</label>
		<form:input path="supervisor" id="supervisor" />
		<form:errors path="supervisor" cssClass="error" />
		<br />

		<label for="address">Address</label>
		<form:input path="address" id="address" />
		<form:errors path="address" cssClass="error" />
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
		<label for="phone">Phone</label>
		<form:input path="phone" id="phone" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="phone" cssClass="error" />
		<br />

		<label for="position">Position Held</label>
		<form:input path="position" id="position" />
		<form:errors path="position" cssClass="error" />
		<br />

		<label for="fromDate">From</label>
		<form:input path="fromDate" id="fromDate" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="fromDate" cssClass="error" />
		<br />
		<label for="toDate">To</label>
		<form:input path="toDate" id="toDate" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="toDate" cssClass="error" />
		<br />

		<label for="leaving">Reason for Leaving</label>
		<form:textarea path="leaving" id="leaving" cols="50" rows="3" />
		<form:errors path="leaving" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="Save" />
	<button onclick="$(location).attr('href', '${pageContext.request.contextPath}/secure/driver/mydata/employment_history'); return false;">Cancel</button>
</form:form>
