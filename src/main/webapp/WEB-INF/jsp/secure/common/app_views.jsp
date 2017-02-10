<%@include file="../../include.jsp"%>

<h1>Application Access Records</h1>

<SCRIPT type="text/javascript">
$(document).ready(function() {
	$("#startDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
	$("#endDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
});
</SCRIPT>

<%-- Search --%>
<form:form commandName="appViewSearchForm" class="searchform" method="post">
	<fieldset>
	
<c:if test="${!isDriver}" >
		<label for="driverEmail">Driver Email</label>
		<form:input path="driverEmail" id="driverEmail" />
		<form:errors path="driverEmail" cssClass="error" />
		<br />
</c:if>		
		<label for="viewerEmail">Viewer Email</label>
		<form:input path="viewerEmail" id="viewerEmail" />
		<form:errors path="viewerEmail" cssClass="error" />
		<label for="viewerCompany">Viewer Company</label>
		<form:input path="viewerCompany" id="viewerCompany" />
		<form:errors path="viewerCompany" cssClass="error" />
		<br />
		
		<label for="startDate">From Date</label>
		<form:input path="startDate" id="startDate" />
		<form:errors path="startDate" cssClass="error" />
		<label for="endDate">To Date</label>
		<form:input path="endDate" id="endDate" />
		<form:errors path="endDate" cssClass="error" />
		<br />
		
		<label for="maxRecords">Max Records</label>
		<form:select path="maxRecords" id="maxRecords" >
			<c:forEach begin="10" end="50" step="10" var="i">
				<form:option value="${i}"  label="${i}" />
			</c:forEach>
		</form:select>
		<form:errors path="maxRecords" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="Search" />
	<input type="submit" name="clear" value="Clear" />
	<br />
</form:form>
<br style="clear: both" />



<%-- List Application Views --%>
<table class="data">
	<thead>
		<tr>
<c:if test="${!isDriver}" >
			<th>Driver</th>
</c:if>		
			<th>Name</th>
			<th>Email</th>
			<th>Company</th>
			<th>Date</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${!empty appViews}">
			<c:forEach items="${appViews}" var="appView" varStatus="status">
				<tr>
<c:if test="${!isDriver}" >
					<td>${appView.driver.person.email}</td>
</c:if>		
					<td>${appView.name}</td>
					<td>${appView.email}</td>
					<td>${appView.company}</td>
					<td><fmt:formatDate value="${appView.createdDate}" pattern="MM-dd-yyyy" /></td>
				</tr>
			</c:forEach>
		</c:if>
		<c:if test="${empty appViews}">
			<tr><td colspan="8">No records found</td></tr>
		</c:if>
	</tbody>
</table>
