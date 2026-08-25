<%@include file="../../include.jsp"%>

<h1>
	User Management
	<a class="add" href="${pageContext.request.contextPath}/secure/admin/users/new">New User</a>
</h1>

<form:form commandName="userManagementForm" cssClass="brform" method="post">
	<fieldset>
		<div style="float: left">
			<label for="firstName" style="width: 100px; white-space: nowrap;">First Name</label>
			<form:input path="firstName" id="firstName" />
			<form:errors path="firstName" cssClass="error" />
			<br />
			<label for="lastName" style="width: 100px; white-space: nowrap;">Last Name</label>
			<form:input path="lastName" id="lastName" />
			<form:errors path="lastName" cssClass="error" />
			<br />
			<label for="type" style="width: 100px; white-space: nowrap;">User Type</label>
			<form:select path="type" id="type">
				<form:option value="" label="Select..." />
				<form:options items="${personTypes}" />
			</form:select>
			<form:errors path="type" cssClass="error" />
			<br />
		</div>
		<div style="float: left">
			<label for="email" style="width: 100px">Email Address</label>
			<form:input path="email" id="email" />
			<form:errors path="email" cssClass="error" />
			<br />
			<label for="companyName" style="width: 100px; white-space: nowrap;">Company Name</label>
			<form:input path="companyName" id="companyName" />
			<form:errors path="companyName" cssClass="error" />
			<br />
			<label for="companyNumber" style="width: 100px; white-space: nowrap;">Company Number</label>
			<form:input path="companyNumber" id="companyNumber" />
			<form:errors path="companyNumber" cssClass="error" />
			<br />
		</div>
	</fieldset>
	<div style="float: lefT">
		<input type="submit" value="Search" />
		<input type="button" id="reset" value="Clear" />
	</div>
	<div style="float: left">
		<label for="maxSize" style="width: 100px; white-space: nowrap;">Results</label>
		<form:select path="maxSize" id="maxSize">
			<form:option value="-1">All</form:option>
			<c:forEach begin="10" end="100" step="10" var="i">
				<form:option value="${i}"  label="${i}" />
			</c:forEach>
		</form:select>
		<form:errors path="type" cssClass="error" />
		<br />
	</div>
</form:form>
<br style="clear: both" />
<table class="data">
	<thead>
		<tr>
			<th>First Name</th>
			<th>Last Name</th>
			<th>Email Address</th>
			<th>Type</th>
			<th>Company Name</th>
			<th>Company Number</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${people}" var="p">
			<tr>
				<td><c:out value="${p.firstName}" /></td>
				<td><c:out value="${p.lastName}" /></td>
				<td><c:out value="${p.email}" /></td>
				<td>${p.type}</td>
				<td><c:out value="${p.company.name}" /></td>
				<td><c:out value="${p.company.companyNumber}" /></td>
				<td><a href="${pageContext.request.contextPath}/secure/admin/users/${p.id}">Edit</a></td>
				<td><a class="confirmDelete" href="${pageContext.request.contextPath}/secure/admin/users/${p.id}/delete">Delete</a></td>
			</tr>
		</c:forEach>
		<c:if test="${fn:length(people) == 0}">
			<tr>
				<td colspan="8">No records found</td>
			</tr>
		</c:if>
	</tbody>
</table>

<script type="text/javascript" language="javascript">
	$(document).ready(function() {
		$("input[name='firstName']").focus();
		$("#reset").click(function() {
			window.location.href = '${pageContext.request.contextPath}/secure/admin/users/reset';
		});
	});
</script>