<%@ include file="include.jsp" %>


<h1>Application Access</h1>
<br />


<form:form commandName="accessInformationForm" action="${pageContext.request.contextPath}/application" class="brform">
	<fieldset>
		<label for="name">Name</label>
		<form:input path="name" id="name" />
		<form:errors path="name" cssClass="error" />
		<br />

		<label for="email">Email</label>
		<form:input path="email" id="email" />
		<form:errors path="email" cssClass="error" />
		<br />

		<label for="company">Company</label>
		<form:input path="company" id="company" />
		<form:errors path="company" cssClass="error" />
		<br />

		<label for="code">Code</label>
		<form:input path="code" id="code" />
		<form:errors path="code" cssClass="error" />
		<br />
		
		<c:if test="${!empty trucks}">
			<label for="truckUuid">Truck</label>
			<form:select path="truckUuid" id="truckUuid">
				<c:forEach var="truck" items="${trucks}">
					<form:option value="${truck.uuid}">${truck.year} ${truck.make} ${truck.model}</form:option>
				</c:forEach>
			</form:select>
			<form:errors path="truckUuid" cssClass="error" />
			<br />
		</c:if>
	</fieldset>
	<input type="submit" value="View Application" />
</form:form>
