<%@include file="../../include.jsp"%>

<h1>Content Nodes</h1>

<table class="data">
	<thead>
		<tr>
			<th>Name</th>
			<th>Description</th>
			<th>Last Modified By</th>
			<th>Last Modified Date</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${fn:length(nodes) == 0}">
			<tr><td colspan="5">No records found</td></tr>
		</c:if>
		<c:forEach items="${nodes}" var="node">
			<tr>
				<td><c:out value="${node.name}" /></td>
				<td><c:out value="${node.description}" /></td>
				<td><c:out value="${node.lastModifiedBy.firstName}" /><c:out value="${node.lastModifiedBy.lastName}" /></td>
				<td><fmt:formatDate value="${node.lastModifiedDate}" pattern="MM/dd/yyyy HH:mm:ss" /></td>
				<td>
					<a href="${pageContext.request.contextPath}/secure/admin/content/nodes/${node.name}">Edit</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>