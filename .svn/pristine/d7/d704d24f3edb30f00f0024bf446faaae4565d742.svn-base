<%@include file="../../include.jsp"%>

<script type="text/javascript">

var baseUrl = "${pageContext.request.contextPath}/secure/admin/imports/";

function updateProgress()
{
	// Get all of the lines in the table
	$("tr").each(function(index) {
		
		var elemId = $(this).attr("id");
		if (jQuery.isNumeric(elemId)) {
			
			//alert("Found a line to work on!  ID:" + elemId);
			var statusTr = $(this).children("td:nth-child(5)");
			var starttimeTr = $(this).children("td:nth-child(6)");
			var endtimeTr = $(this).children("td:nth-child(7)");
			
			// If status, starttime, or or endtime empty then update
			if (statusTr.text() == null || statusTr.text().length == 0 ||
					starttimeTr.text() == null || starttimeTr.text().length == 0 ||
					endtimeTr.text() == null || endtimeTr.text().length == 0) {
				
				// Call ajax to get values
				var url = baseUrl + elemId + ".json";
				$.getJSON(url, function(data) {
					
					// Update the row
					if (data.success == null) {
						statusTr.removeClass().addClass("warning").text("In Progress");
					} else if (data.success) {
						statusTr.removeClass().addClass("success").text("Success");
					} else {
						statusTr.removeClass().addClass("error").text("Failure");
					}
					if (data.startTime == null) {
						starttimeTr.text("Not Started");
					} else {
						starttimeTr.text(data.startTime);
					}
					if (data.endTime != null) {
						endtimeTr.text(data.endTime);
					} else {
						endtimeTr.text("");
					}
				});
			}
		}
	});
	
}

var tid = setInterval(updateProgress, 3000);
function abortTimer()
{
	// to be called when you want to stop the timer
	clearInterval(tid);
}

</script>


<h1>
	Data Imports
	<a class="add" href="${pageContext.request.contextPath}/secure/admin/imports/new">New Import</a>
</h1>

<table class="data">
	<thead>
		<tr>
			<th>Company</th>
			<th>Import Type</th>
			<th>Uploaded By</th>
			<th>Upload Date</th>
			<th>Status</th>
			<th>Start Time</th>
			<th>End Time</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${empty imports}">
			<tr><td colspan="9">No records found</td></tr>
		</c:if>
		<c:forEach items="${imports}" var="imp">
			<tr id="${imp.id}">
				<td><c:out value="${imp.company.name}" /></td>
				<td><c:out value="${imp.importType}" /></td>
				<td><c:out value="${imp.createdBy.firstName} ${imp.createdBy.lastName}" /></td>
				<td><fmt:formatDate pattern="MM/dd/yyyy hh:mm:ss a" value="${imp.createdDate}" /></td>
				<c:choose>
					<c:when test="${imp.success == null}">
						<td class="warning">In Progress</td>
					</c:when>
					<c:when test="${imp.success}">
						<td class="success">Success</td>
					</c:when>
					<c:otherwise>
						<td class="error">Failure</td>
					</c:otherwise>
				</c:choose>
				<td><fmt:formatDate pattern="hh:mm:ss a" value="${imp.startTime}" /></td>
				<td><fmt:formatDate pattern="hh:mm:ss a" value="${imp.endTime}" /></td>
				<td><a href="${pageContext.request.contextPath}/secure/admin/imports/${imp.id}.csv" target="_blank">View Data</a></td>
				<td><a href="${pageContext.request.contextPath}/secure/admin/imports/${imp.id}">View Log</a></td>
				<td><a href="${pageContext.request.contextPath}/secure/admin/imports/${imp.id}/delete" class="confirmDelete">Delete</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>