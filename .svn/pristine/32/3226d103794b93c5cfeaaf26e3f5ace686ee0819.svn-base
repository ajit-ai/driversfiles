<%@include file="../../../include.jsp"%>

<c:choose>
	<c:when test="${!empty employmentId}">
		<h1>Edit Residence</h1>
	</c:when>
	<c:otherwise>
		<h1>Add Residence</h1>
	</c:otherwise>
</c:choose>


<form:form commandName="residenceForm" action="${pageContext.request.contextPath}/secure/driver/mydata/residency_history" class="brform">
	<fieldset>
		<c:if test="${!empty residenceForm.uuid}">
		<form:hidden path="uuid" id="uuid" />
		</c:if>

		<label for="address1">Address 1</label>
		<form:input path="address1" id="address1" />
		<form:errors path="address1" cssClass="error" />
		<br />

		<label for="address2">Address 2</label>
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
	<button onclick="$(location).attr('href', '${pageContext.request.contextPath}/secure/driver/mydata/residency_history'); return false;">Cancel</button>
</form:form>
