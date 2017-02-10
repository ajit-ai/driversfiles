<%@include file="../../../include.jsp"%>

<h1>Traffic Convictions & Forfeitures
<a class="add" href="${pageContext.request.contextPath}/secure/driver/mydata/traffics/new">New Traffic Record</a>
</h1>


<%-- List saved traffic conviction records --%>
<table class="data">
	<thead>
		<tr>
			<th>Date</th>
			<th>City</th>
			<th>State</th>
			<th>Charge</th>
			<th>Penalty</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>


<c:if test="${!empty trafficRecords}">
	<c:forEach items="${trafficRecords}" var="traffic">
		<tr>
			<td><fmt:formatDate value="${traffic.trafficDate}" pattern="MM/dd/yyyy"/></td>
			<td>${traffic.city}</td>
			<td>${traffic.state}</td>
			<td>${traffic.charge}</td>
			<td>${traffic.penalty}</td>
			<td><a href="${pageContext.request.contextPath}/secure/driver/mydata/traffics/${traffic.uuid}">Edit</a></td>
			<td><a class="confirmDelete" href="${pageContext.request.contextPath}/secure/driver/mydata/traffics/${traffic.uuid}/delete">Delete</a></td>
		</tr>
	</c:forEach>
</c:if>
<c:if test="${empty trafficRecords}">
		<tr><td colspan="7">No records</td></tr>
</c:if>

	</tbody>
</table>
<br />
