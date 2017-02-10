<%@include file="../../../include.jsp"%>

<SCRIPT type="text/javascript">
$(document).ready(function() {
	$("#trafficDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
});
</SCRIPT>

<c:choose>
	<c:when test="${!empty trafficId}">
		<h1>Edit Traffic Record</h1>
	</c:when>
	<c:otherwise>
		<h1>Add Traffic Record</h1>
	</c:otherwise>
</c:choose>


<form:form commandName="trafficForm" action="${pageContext.request.contextPath}/secure/driver/mydata/traffic_convictions" class="brform">
	<fieldset>
		<c:if test="${!empty trafficForm.uuid}">
		<form:hidden path="uuid" id="uuid" />
		</c:if>

		<label for="trafficDate">Date</label>
		<form:input path="trafficDate" id="trafficDate" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="trafficDate" cssClass="error" />
		<br />
		<label for="city">City</label>
		<form:input path="city" id="city" />
		<form:errors path="city" cssClass="error" />
		<br />
		<label for="state">State</label>
		<form:select path="state" items="${states}" itemLabel="name" itemValue="code" id="state" />
		<form:errors path="state" cssClass="error" />
		<br />

		<label for="charge">Charge</label>
		<form:input path="charge" id="charge" />
		<form:errors path="charge" cssClass="error" />
		<br />

		<label for="penalty">Penalty</label>
		<form:input path="penalty" id="penalty" />
		<form:errors path="penalty" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="Save" />
	<button onclick="$(location).attr('href', '${pageContext.request.contextPath}/secure/driver/mydata/traffic_convictions'); return false;">Cancel</button>
</form:form>
