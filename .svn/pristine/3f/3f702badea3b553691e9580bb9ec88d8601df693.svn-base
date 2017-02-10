<%@include file="../../include.jsp"%>

<h1>Edit Content Node</h1>

<form:form commandName="contentForm" cssClass="brform">
	<fieldset>
		<label>Content Node Name</label>
		<div><c:out value="${node.name}" /></div>
		<br />

		<label>Description</label>
		<div><c:out value="${node.description}" /></div>
		<br />

		<label for="content-node">Content</label>
		<form:textarea path="content" id="content-node" cols="100" rows="20" />
		<form:errors path="content" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" value="Save" />
	<input type="button" value="Cancel" id="cancel" />
	<br />
</form:form>

<script type="text/javascript">
	$(document).ready(function() {
		$("#cancel").click(function() {
			$(location).attr('href', '${pageContext.request.contextPath}/secure/admin/content/nodes');
		});
		$("#content-node").focus();
	});
</script>
