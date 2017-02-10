<%@include file="../../../include.jsp"%>

<SCRIPT type="text/javascript">
$(document).ready(function() {
	$("#accidentDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
});
</SCRIPT>

<c:choose>
	<c:when test="${!empty accidentId}">
		<h1>Edit Accident Record</h1>
	</c:when>
	<c:otherwise>
		<h1>Add Accident Record</h1>
	</c:otherwise>
</c:choose>


<form:form commandName="accidentForm" action="${pageContext.request.contextPath}/secure/driver/mydata/accident_information" class="brform">
	<fieldset>
		<c:if test="${!empty accidentForm.uuid}">
		<form:hidden path="uuid" id="uuid" />
		</c:if>

		<label for="accidentDate">Accident Date</label>
		<form:input path="accidentDate" id="accidentDate" cssStyle="width: 150px;" />&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="accidentDate" cssClass="error" />
		<br />

		<label for="type">Type</label>
		<form:input path="type" id="type" />
		<form:errors path="type" cssClass="error" />
		<br />

		<label for="nature">Nature</label>
		<form:input path="nature" id="nature" />
		<form:errors path="nature" cssClass="error" />
		<br />

		<label for="atFault">Were you found at fault?</label>
		<form:radiobutton path="atFault" value="true"/>
			<spring:message code="label.Yes"/>
		<form:radiobutton path="atFault" value="false"/>
			<spring:message code="label.No"/>
		<form:errors path="atFault" cssClass="error" />
		<br />

		<label for="fatalities">Were there fatalities?</label>
		<form:radiobutton path="fatalities" value="true"/>
			<spring:message code="label.Yes"/>
		<form:radiobutton path="fatalities" value="false"/>
			<spring:message code="label.No"/>
		<form:errors path="fatalities" cssClass="error" />
		<br />

		<label for="injuries">Were there injuries?</label>
		<form:radiobutton path="injuries" value="true"/>
			<spring:message code="label.Yes"/>
		<form:radiobutton path="injuries" value="false"/>
			<spring:message code="label.No"/>
		<form:errors path="injuries" cssClass="error" />
		<br />

		<label for="damages">Amount of damages</label>&nbsp;&nbsp;(numbers only)
		<form:input path="damages" id="damages" />
		<form:errors path="damages" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="Save" />
	<button onclick="$(location).attr('href', '${pageContext.request.contextPath}/secure/driver/mydata/accident_information'); return false;">Cancel</button>
</form:form>
