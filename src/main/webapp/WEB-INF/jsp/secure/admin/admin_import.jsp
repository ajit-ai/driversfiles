<%@ include file="../../include.jsp" %>

<h1>Data Import</h1>
<form:form commandName="importForm" cssClass="brform" enctype="multipart/form-data">
	<fieldset>
		<label>Import Type</label>
		<form:select path="importType" items="${importTypes}" />
		<form:errors path="importType" cssClass="error" />
		<br />
		<label>Company</label>
		<form:select path="companyId" items="${companies}" itemLabel="name" itemValue="id" />
		<form:errors path="companyId" cssClass="error" />
		<br />
		<label for="file">Import File</label>
		<input type="file" name="file" id="file" />
		<form:errors path="file" cssClass="error" />
		<br />
		<label for="overwrite">Overwrite Existing Data</label>
		<form:checkbox path="overwrite" id="overwrite" />
		<form:errors path="overwrite" cssClass="error" />
		<br />
	</fieldset>
	<input type="submit" name="submit" value="Import" />
</form:form>