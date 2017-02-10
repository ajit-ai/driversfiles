<%@include file="../../../include.jsp"%>

<h1>
	Residency History
	<a class="add" href="${pageContext.request.contextPath}/secure/driver/mydata/residences/new">New Residence</a>
</h1>

<p>List all addresses you have had in the past 3 years.</p>


<table class="data">
	<thead>
		<tr>
			<th>Address</th>
			<th>City</th>
			<th>State</th>
			<th>Postal Code</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
	
<c:if test="${!empty residences}">
	<c:forEach items="${residences}" var="residence">
		<tr>
			<td>${residence.address1}<br />&nbsp;&nbsp;&nbsp;${residence.address2}</td>
			<td>${residence.city}</td>
			<td>${residence.state}</td>
			<td>${residence.postalCode}</td>
			<td><a href="${pageContext.request.contextPath}/secure/driver/mydata/residences/${residence.uuid}">Edit</a></td>
			<td><a class="confirmDelete" href="${pageContext.request.contextPath}/secure/driver/mydata/residences/${residence.uuid}/delete">Delete</a></td>
		</tr>
	</c:forEach>
</c:if>
<c:if test="${empty residences}">
		<tr><td colspan="6">No records</td></tr>
</c:if>

	</tbody>
</table>
<br />
<form action="${pageContext.request.contextPath}/secure/driver/mydata/residency_history">
	<input type="submit" name="next" value="Next" />
</form>

