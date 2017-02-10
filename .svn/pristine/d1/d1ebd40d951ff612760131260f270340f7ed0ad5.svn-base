<%@include file="../../include.jsp"%>

<h1>
	My Drivers
</h1>

<form:form commandName="driverSearchForm" class="searchform" method="post">
	<fieldset>
		<label for="firstName">First Name</label>
		<form:input path="firstName" id="firstName" />
		<form:errors path="firstName" cssClass="error" />
		<label for="lastName">Last Name</label>
		<form:input path="lastName" id="lastName" />
		<form:errors path="lastName" cssClass="error" />
		<label for="email">Email</label>
		<form:input path="email" id="email" />
		<form:errors path="email" cssClass="error" />
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
			<th>First Name</th>
			<th>Last Name</th>
			<th>Email</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${!empty drivers}">
			<c:forEach items="${drivers}" var="driver" varStatus="status">
				<tr>
					<td>${driver.person.firstName}</td>
					<td>${driver.person.lastName}</td>
					<td>${driver.person.email}</td>
					<td><a href="${pageContext.request.contextPath}/secure/application/${driver.person.uuid}" target="_driver_app">Pull Application</a></td>
				</tr>
			</c:forEach>
		</c:if>
		<c:if test="${empty drivers}">
			<tr><td colspan="5">No records found</td></tr>
		</c:if>
	</tbody>
</table>
