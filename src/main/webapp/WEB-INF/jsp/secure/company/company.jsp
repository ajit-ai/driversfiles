<%@include file="../../include.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$("input[name='companyName']").focus();
	});
</script>

<h1>Company Information</h1>
<form:form commandName="myCompanyForm" cssClass="brform" action="${pageContext.request.contextPath}/secure/company/company/save" method="post" enctype="multipart/form-data">
	<fieldset>
		<label for="companyName">Company Name</label>
		<form:input path="companyName" id="companyName" />
		<form:errors path="companyName" cssClass="error" />
		<br />
		
		<label for="companyNumber">Company Number</label>
		<input id="companyNumber" readonly="readonly" value="${myCompanyForm.companyNumber}" />
		<br />
		
		<c:if test="${!empty myCompanyForm.iconUrl}">
			<%-- Show the icon --%>
			<label for="currentIcon">Current Icon</label>
			<img id="currentIcon" src="${pageContext.request.contextPath}${myCompanyForm.iconUrl}" title="Company Icon" />
			<br />
		</c:if>
		<spring:message code="icon.help" var="iconHelpText" />
		<label for="iconFile">${(!empty myCompanyForm.iconUrl)?'Replace':'Upload'} Icon
			<img src="${pageContext.request.contextPath}/resources/images/help_16x16.png" title="${iconHelpText}" />
		</label>
		<input type="file" name="iconFile" id="iconFile" maxlength="255" size="40" />
		<form:errors path="iconFile" cssClass="error" />
		<br />
		
		<label for="address1">Address</label>
		<form:input path="address1" id="address1" />
		<form:errors path="address1" cssClass="error" />
		<br />
		<label for="address2">&nbsp;</label>
		<form:input path="address2" id="address2" />
		<form:errors path="address2" cssClass="error" />
		<br />
		<label for="city">City</label>
		<form:input path="city" id="city" />
		<form:errors path="city" cssClass="error" />
		<br />
		<label for="state">State</label>
		<form:select path="state" items="${states}" itemLabel="name" itemValue="code" id="state" />
		<form:errors path="state" cssClass="error" />
		<br />
		<label for="postalCode">Postal Code</label>
		<form:input path="postalCode" id="postalCode" />
		<form:errors path="postalCode" cssClass="error" />
		<br />
		
		<label for="phone">Phone</label>
		<form:input path="phone" id="phone" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="phone" cssClass="error" />
		<br />
		<label for="fax">Fax</label>
		<form:input path="fax" id="fax" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="fax" cssClass="error" />
		<br />
		<label for="website">Website</label>
		<form:input path="website" id="website" />
		<form:errors path="website" cssClass="error" />
		<br />

	</fieldset>
	<input type="submit" value="Save" />
</form:form>

<br />

