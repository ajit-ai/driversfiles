<%@include file="../../include.jsp"%>

<h1>Application: Select Truck</h1>
<br />

<p>There are multiple active trucks. Please select the truck to display on the application.</p>
<br />

<form:form commandName="personTruckForm" action="${pageContext.request.contextPath}/secure/application/picktruck" class="brform">
	<form:hidden path="personUuid" />

	<fieldset>
		<label for="truckUuid">Truck</label>
		<form:select path="truckUuid" id="truckUuid">
			<c:forEach var="truck" items="${trucks}">
				<form:option value="${truck.uuid}">${truck.year} ${truck.make} ${truck.model}</form:option>
			</c:forEach>
		</form:select>
		<form:errors path="truckUuid" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="View Application" />
</form:form>
