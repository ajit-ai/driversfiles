<%@include file="../../../include.jsp"%>

<h1>Employment History
<a class="add" href="${pageContext.request.contextPath}/secure/driver/mydata/employments/new">New Employment</a>
</h1>

<%-- List saved employment records --%>
<table class="data">
	<thead>
		<tr>
			<th>From</th>
			<th>To</th>
			<th>Company Name</th>
			<th>Address</th>
			<th>City</th>
			<th>State</th>
			<th>Postal Code</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>


<c:if test="${!empty employmentHistory}">
	<c:forEach items="${employmentHistory}" var="employ">
		<tr>
			<td><fmt:formatDate value="${employ.fromDate}" pattern="MM/dd/yyyy"/></td>
			<td><fmt:formatDate value="${employ.toDate}" pattern="MM/dd/yyyy"/></td>
			<td>${employ.name}</td>
			<td>${employ.address}</td>
			<td>${employ.city}</td>
			<td>${employ.state}</td>
			<td>${employ.postalCode}</td>
			<td><a href="${pageContext.request.contextPath}/secure/driver/mydata/employments/${employ.uuid}">Edit</a></td>
			<td><a class="confirmDelete" href="${pageContext.request.contextPath}/secure/driver/mydata/employments/${employ.uuid}/delete">Delete</a></td>
		</tr>
	</c:forEach>
</c:if>
<c:if test="${empty employmentHistory}">
		<tr><td colspan="9">No records</td></tr>
</c:if>

	</tbody>
</table>
<br />
<form action="${pageContext.request.contextPath}/secure/driver/mydata/traffic_convictions">
	<input type="submit" name="next" value="Next" />
</form>

