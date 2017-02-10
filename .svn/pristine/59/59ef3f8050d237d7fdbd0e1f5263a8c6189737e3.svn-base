<%@include file="../../../include.jsp"%>

<h1>
	My Trucks
	<a class="add" href="${pageContext.request.contextPath}/secure/common/trucks/new">New Truck</a>
</h1>

<form:form commandName="truckSearchForm" class="searchform" method="post">
	<fieldset>
		<label for="vin">VIN</label>
		<form:input path="vin" id="vin" />
		<form:errors path="vin" cssClass="error" />
		<label for="year">Year</label>
		<form:input path="year" id="year" />
		<form:errors path="year" cssClass="error" />
		<br />
		<label for="make">Make</label>
		<form:input path="make" id="make" />
		<form:errors path="make" cssClass="error" />
		<label for="model">Model</label>
		<form:input path="model" id="model" />
		<form:errors path="model" cssClass="error" />
		<br />
		<label for="active">Active</label>
		<form:checkbox path="active" id="active" />
		<form:errors path="active" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="Search" />
	<input type="submit" name="clear" value="Clear" />
	<br />
</form:form>
<br style="clear: both" />
<table class="data">
	<thead>
		<tr>
			<th>VIN</th>
			<th>Year</th>
			<th>Make</th>
			<th>Model</th>
			<th>License</th>
			<th>Active</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${!empty trucks}">
			<c:forEach items="${trucks}" var="truck" varStatus="status">
				<tr>
					<td>${truck.vin}</td>
					<td>${truck.year}</td>
					<td>${truck.make}</td>
					<td>${truck.model}</td>
					<td>${truck.license}</td>
					<td>${(!empty truck.active && truck.active)?'Yes':'No'}</td>
					<td><a href="${pageContext.request.contextPath}/secure/common/trucks/${truck.uuid}">Edit</a></td>
					<td><a href="${pageContext.request.contextPath}/secure/common/trucks/${truck.uuid}/delete">Delete</a></td>
				</tr>
			</c:forEach>
		</c:if>
		<c:if test="${empty trucks}">
			<tr><td colspan="8">No records found</td></tr>
		</c:if>
	</tbody>
</table>