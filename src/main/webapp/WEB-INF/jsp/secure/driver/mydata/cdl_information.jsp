<%@include file="../../../include.jsp"%>


<h1>CDL Information
<a class="add" href="${pageContext.request.contextPath}/secure/driver/mydata/licenses/new">New CDL</a>
</h1>

<p>Add all licenses held in the past 3 years.</p>

<%-- List saved license records --%>
<table class="data">
	<thead>
		<tr>
			<th>State</th>
			<th>Number</th>
			<th>Type</th>
			<th>Exp Date</th>
			<th>Current</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>


<c:if test="${!empty licenses}">
	<c:forEach items="${licenses}" var="license">
		<tr>
			<td>${license.state}</td>
			<td>${license.number}</td>
			<td>${license.type.name}</td>
			<td><fmt:formatDate value="${license.expiration}" pattern="MM/dd/yyyy"/></td>
			<td>${(license.current)?'Yes':'&nbsp;'}</td>
			<td><a href="${pageContext.request.contextPath}/secure/driver/mydata/licenses/${license.uuid}">Edit</a></td>
			<td><a class="confirmDelete" href="${pageContext.request.contextPath}/secure/driver/mydata/licenses/${license.uuid}/delete">Delete</a></td>
		</tr>
	</c:forEach>
</c:if>
<c:if test="${empty licenses}">
		<tr><td colspan="6">No records</td></tr>
</c:if>

	</tbody>
</table>
<br />
<form action="${pageContext.request.contextPath}/secure/driver/mydata/accident_information">
	<input type="submit" name="next" value="Next" />
</form>


