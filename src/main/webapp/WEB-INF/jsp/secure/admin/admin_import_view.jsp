<%@include file="../../include.jsp"%>

<h1>View Data Import</h1>

<form class="brform">
	<fieldset>
		<label>Company</label>
		<div><c:out value="${imp.company.name}" /></div>
		<br />
		<label>Import Type</label>
		<div><c:out value="${imp.importType}" /></div>
		<br />
		<label>Uploaded By</label>
		<div><c:out value="${imp.createdBy.firstName}" /> <c:out value="${imp.createdBy.lastName}" /></div>
		<br />
		<label>Upload Date</label>
		<div><fmt:formatDate value="${imp.createdDate}" pattern="MM/dd/yyyy hh:mm:ss a" /></div>
		<br />
		<label>Status</label>
		<div id="status"></div>
		<br />
		<label>Start Time</label>
		<div id="startTime"></div>
		<br />
		<label>End Time</label>
		<div id="endTime"></div>
		<br />
		<label>Log</label>
		<textarea cols="100" rows="15" id="log"></textarea>
		<br />
	</fieldset>
	<input type="button" value="Back" id="back" />
	<input type="button" value="Delete" id="delete" />
</form>

<script type="text/javascript">
	function loadImportData() {
		$.getJSON("${pageContext.request.contextPath}/secure/admin/imports/${imp.id}.json", function(data) {
			if ($("#status").text() != 'Success' && $("#status").text() != 'Failure') {
				$("#log").text(data.log).scrollTop($("#log")[0].scrollHeight - $("#log").height());
			}
			if (data.success == null) {
				$("#status").removeClass().addClass("warning").text("In Progress");
			} else if (data.success) {
				$("#status").removeClass().addClass("success").text("Success");
			} else {
				$("#status").removeClass().addClass("error").text("Failure");
			}
			if (data.startTime == null) {
				$("#startTime").text("Not Started");
			} else {
				$("#startTime").text(data.startTime);
			}
			if (data.endTime != null) {
				$("#endTime").text(data.endTime);
			}
		});
	}
	$(document).ready(function() {
		$("#back").click(function() {
			window.location.href = '${pageContext.request.contextPath}/secure/admin/imports';
		});
		$("#delete").click(function() {
			if (confirmDelete()) {
				window.location.href = '${pageContext.request.contextPath}/secure/admin/imports/${imp.id}/delete';
			}
		});
		loadImportData();
		setInterval(loadImportData, (5 * 1000));
	});
</script>