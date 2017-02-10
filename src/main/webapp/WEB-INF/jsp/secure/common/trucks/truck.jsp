<%@include file="../../../include.jsp"%>

<script type="text/javascript">
$(document).ready(function() {
	$("#registration").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
	$("#annualInspection").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
	$("#bobtailInsurance").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
	$("#quarterlyMaintenance").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
	$("#physicalDamageInsurance").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
});
</script>

<c:choose>
	<c:when test="${!empty truckUuid}">
		<h1>Edit Truck</h1>
	</c:when>
	<c:otherwise>
		<h1>Add Truck</h1>
	</c:otherwise>
</c:choose>


<form:form commandName="truckForm" action="${pageContext.request.contextPath}/secure/common/trucks/save" class="brform" enctype="multipart/form-data" >
	<fieldset>
		<c:if test="${!empty truckForm.uuid}">
			<form:hidden path="uuid" id="uuid" />
		</c:if>

		<label for="vin">VIN<t:required /></label>
		<form:input path="vin" id="vin" />
		<form:errors path="vin" cssClass="error" />
		<br />

		<label for="year">Year<t:required /></label>
		<form:input path="year" id="year" />
		<form:errors path="year" cssClass="error" />
		<br />

		<label for="make">Make<t:required /></label>
		<form:input path="make" id="make" />
		<form:errors path="make" cssClass="error" />
		<br />

		<label for="model">Model<t:required /></label>
		<form:input path="model" id="model" />
		<form:errors path="model" cssClass="error" />
		<br />
		
		<c:if test="${!empty companyType}">
		<label for="truckNumber">Truck Number<t:required /></label>
		<form:input path="truckNumber" id="truckNumber" />
		<form:errors path="truckNumber" cssClass="error" />
		<br />
		</c:if>
		
		<label for="license">License</label>
		<form:input path="license" id="license" />
		<form:errors path="license" cssClass="error" />
		<br />

		<label for="licenseState">License State</label>
		<form:select path="licenseState" items="${states}" itemLabel="name" itemValue="code" id="licenseState" />
		<form:errors path="licenseState" cssClass="error" />
		<br />

		<label for="registration">Registration</label>
		<form:input path="registration" id="registration" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="registration" cssClass="error" />
		<br />

		<label for="annualInspection">Annual Inspection</label>
		<form:input path="annualInspection" id="annualInspection" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="annualInspection" cssClass="error" />
		<br />

		<label for="bobtailInsurance">Bobtail Insurance</label>
		<form:input path="bobtailInsurance" id="bobtailInsurance" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="bobtailInsurance" cssClass="error" />
		<br />

		<label for="ifta">IFTA</label>
		<form:checkbox path="ifta" id="ifta" />
		<form:errors path="ifta" cssClass="error" />
		<br />

		<label for="quarterlyMaintenance">Quarterly Maintenance</label>
		<form:input path="quarterlyMaintenance" id="quarterlyMaintenance" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="quarterlyMaintenance" cssClass="error" />
		<br />

		<label for="physicalDamageInsurance">Physical Damage Insurance</label>
		<form:input path="physicalDamageInsurance" id="physicalDamageInsurance" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="physicalDamageInsurance" cssClass="error" />
		<br />

		<label for="lessorNumber">Lessor Number</label>
		<form:input path="lessorNumber" id="lessorNumber" />
		<form:errors path="lessorNumber" cssClass="error" />
		<br />

		<label for="lessorName">Lessor Name</label>
		<form:input path="lessorName" id="lessorName" />
		<form:errors path="lessorName" cssClass="error" />
		<br />

		<label for="lessorAddress1">Lessor Address 1</label>
		<form:input path="lessorAddress1" id="lessorAddress1" />
		<form:errors path="lessorAddress1" cssClass="error" />
		<br />

		<label for="lessorAddress2">Lessor Address 2</label>
		<form:input path="lessorAddress2" id="lessorAddress2" />
		<form:errors path="lessorAddress2" cssClass="error" />
		<br />

		<label for="lessorCity">Lessor City</label>
		<form:input path="lessorCity" id="lessorCity" />
		<form:errors path="lessorCity" cssClass="error" />
		<br />

		<label for="lessorState">Lessor State</label>
		<form:select path="lessorState" items="${states}" itemLabel="name" itemValue="code" id="lessorState" />
		<form:errors path="lessorState" cssClass="error" />
		<br />

		<label for="lessorPostalCode">Lessor Postal Code</label>
		<form:input path="lessorPostalCode" id="lessorPostalCode" cssStyle="width: 150px;" />
		<form:errors path="lessorPostalCode" cssClass="error" />
		<br />

		<label for="lessorPhone">Lessor Phone</label>
		<form:input path="lessorPhone" id="lessorPhone" cssStyle="width: 150px;" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="lessorPhone" cssClass="error" />
		<br />

		<label for="lessorMobile">Lessor Mobile</label>
		<form:input path="lessorMobile" id="lessorMobile" cssStyle="width: 150px;" />&nbsp;(XXX-XXX-XXXX)
		<form:errors path="lessorMobile" cssClass="error" />
		<br />

		<label for="lessorGovId">Lessor Gov. Id</label>
		<form:input path="lessorGovId" id="lessorGovId" />
		<form:errors path="lessorGovId" cssClass="error" />
		<br />

		<label for="active">Active</label>
		<form:checkbox path="active" id="active" />
		<form:errors path="active" cssClass="error" />
		<br />
	</fieldset>
	
	<fieldset><legend>Truck Documents</legend>
		
		<c:if test="${!empty docs}">
			<br />
			<table class="data">
				<thead>
					<tr>
						<th>Type</th>
						<th>&nbsp;</th>
						<th>&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${!empty docs}">
						<c:forEach items="${docs}" var="doc">
							<tr>
								<td>${doc.type.title}</td>
								<td><a href="${pageContext.request.contextPath}${doc.url}" target="_docdownload" type="${doc.mimetype}" >Download</a></td>
								<td><a href="${pageContext.request.contextPath}/secure/common/trucks/${truckForm.uuid}/deletedoc/${doc.type.name}">Delete</a></td>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${empty docs}">
						<tr><td colspan="2">No uploads</td></tr>
					</c:if>
				</tbody>
			</table>
			<br />
			<br />
		</c:if>


		<label for="registrationFile">Registration</label>
		<input type="file" name="registrationFile" id="registrationFile" maxlength="255" size="40" />
		<form:errors path="registrationFile" cssClass="error" />
		<br /><br />
		
		<label for="annualInspFile">Annual Inspection</label>
		<input type="file" name="annualInspFile" id="annualInspFile" maxlength="255" size="40" />
		<form:errors path="annualInspFile" cssClass="error" />
		<br /><br />
		
		<label for="bobtailFile">Bobtail</label>
		<input type="file" name="bobtailFile" id="bobtailFile" maxlength="255" size="40" />
		<form:errors path="bobtailFile" cssClass="error" />
		<br /><br />

		<label for="quarterlyMaintFile">Quarterly Maintenance</label>
		<input type="file" name="quarterlyMaintFile" id="quarterlyMaintFile" maxlength="255" size="40" />
		<form:errors path="quarterlyMaintFile" cssClass="error" />
		<br /><br />
		
		<label for="physicalDamageInsFile">Physical Damage Insurance</label>
		<input type="file" name="physicalDamageInsFile" id="physicalDamageInsFile" maxlength="255" size="40" />
		<form:errors path="physicalDamageInsFile" cssClass="error" />
		<br /><br />
	</fieldset>
	
	
	<input type="submit" value="Save" />
	<button onclick="$(location).attr('href', '${pageContext.request.contextPath}/secure/common/trucks'); return false;">Cancel</button>
</form:form>

