<%@include file="../../../include.jsp"%>


<script type="text/javascript">
$(document).ready(function() {
	$("#cdlExpireDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
	$("#physicalExpireDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
	$("#medicalExpireDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
});

function showUploadingDiv()
{
	var modalHtml = "<div style=\'text-align:center;\'><br /><br /><br /><h3>Files are being uploaded ...</h3><br /><p>This may take a minute.<br /><br /><br /><br /></p></div>";
	var theTitle = "Upload Files...";
	
	jQuery.fn.modalBox({
		directCall: {
			data : modalHtml
			}
	});
}
</script>


<h1>Documents</h1>

<%-- TODO We do not need a table display here as they can only have one of each document --%>
<table class="data">
	<thead>
		<tr>
			<th>Title</th>
			<th>Upload Date</th>
			<th>Expiration Date</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${!empty docs}">
			<c:forEach items="${docs}" var="doc">
				<c:set var="docTitle" value="" />
				<c:forEach items="${docTypes}" var="docType">
					<c:if test="${docType.name ==  doc.typeCode}">
						<c:set var="docTitle" value="${docType.title}" />
					</c:if>
				</c:forEach>
				<c:set var="expiredClass" value="success" />
				<c:if test="${!empty doc.expirationDate && now > doc.expirationDate}">
					<c:set var="expiredClass" value="error" />
				</c:if>

				<tr>
					<td>${docTitle}</td>
					<td><fmt:formatDate value="${doc.createdDate}" pattern="yyyy-MM-dd hh:mm" /></td>
					<td class="${expiredClass}">
						<fmt:formatDate value="${doc.expirationDate}" pattern="yyyy-MM-dd" />
					</td>
					<td><a href="${pageContext.request.contextPath}/secure/api/document/${doc.uuid}" target="_docdownload">Download</a></td>
				</tr>
			</c:forEach>
		</c:if>
		<c:if test="${empty docs}">
			<tr><td colspan="4">No uploads</td></tr>
		</c:if>
	</tbody>
</table>
<br />
<br />


<form:form commandName="documentUploadForm" class="brform" onsubmit="showUploadingDiv()" enctype="multipart/form-data" >
	<fieldset>
		<div class="error"><form:errors path="" cssClass="error" /></div><br /><%-- TODO Check this --%>
		
		<label for="cdlFile">CDL</label>
		<input type="file" name="cdlFile" id="cdlFile" maxlength="255" size="40" />
		<br />
		<label for="cdlExpireDate">Expiration Date</label>
		<input type="text" name="cdlExpireDate" id="cdlExpireDate" style="width:150px;" />&nbsp;&nbsp;MM/DD/YYYY
		<form:errors path="cdlExpireDate" cssClass="error" />
		<br /><br />
		
		<label for="physicalFile">Long Form Physical</label>
		<input type="file" name="physicalFile" id="physicalFile" maxlength="255" size="40" />
		<br />
		<label for="physicalExpireDate">Expiration Date</label>
		<input type="text" name="physicalExpireDate" id="physicalExpireDate" style="width:150px;" />&nbsp;&nbsp;MM/DD/YYYY
		<form:errors path="physicalExpireDate" cssClass="error" />
		<br /><br />
		
		<label for="medicalCardFile">Medical Card</label>
		<input type="file" name="medicalCardFile" id="medicalCardFile" maxlength="255" size="40" />
		<br />
		<label for="medicalExpireDate">Expiration Date</label>
		<input type="text" name="medicalExpireDate" id="medicalExpireDate" style="width:150px;" />&nbsp;&nbsp;MM/DD/YYYY
		<form:errors path="medicalExpireDate" cssClass="error" />
		<br /><br />

		<label for="ssCardFile">Social Security Card</label>
		<input type="file" name="ssCardFile" id="ssCardFile" maxlength="255" size="40" />
		<br />
		
		<br />
	</fieldset>
	<input type="submit" value="Save" />
	<input type="submit" name="next" value="Save & Next" />
</form:form>

