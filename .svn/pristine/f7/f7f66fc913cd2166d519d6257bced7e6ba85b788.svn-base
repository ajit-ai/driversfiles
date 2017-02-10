<%@include file="../../../include.jsp"%>

<h1>Accident Information
<a class="add" href="${pageContext.request.contextPath}/secure/driver/mydata/accidents/new">Add an Accident</a>
</h1>

<%-- List saved accident records --%>
<table class="data">
	<thead>
		<tr>
			<th>Date</th>
			<th>Type</th>
			<th>Nature</th>
			<th>At Fault</th>
			<th>Fatalities</th>
			<th>Injuries</th>
			<th>Damages</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>


<c:if test="${!empty accidents}">
	<c:forEach items="${accidents}" var="accident">
		<tr>
			<td><fmt:formatDate value="${accident.accidentDate}" pattern="MM/dd/yyyy"/></td>
			<td>${accident.type}</td>
			<td>${accident.nature}</td>
			<td>${accident.atFault}</td>
			<td>${accident.fatalities}</td>
			<td>${accident.injuries}</td>
			<td>${accident.damages}</td>
			<td><a href="${pageContext.request.contextPath}/secure/driver/mydata/accidents/${accident.uuid}">Edit</a></td>
			<td><a class="confirmDelete" href="${pageContext.request.contextPath}/secure/driver/mydata/accidents/${accident.uuid}/delete">Delete</a></td>
		</tr>
	</c:forEach>
</c:if>
<c:if test="${empty accidents}">
		<tr><td colspan="9">No records</td></tr>
</c:if>

	</tbody>
</table>
<br />
<form action="${pageContext.request.contextPath}/secure/driver/mydata/employment_history">
	<input type="submit" name="next" value="Next" />
</form>

