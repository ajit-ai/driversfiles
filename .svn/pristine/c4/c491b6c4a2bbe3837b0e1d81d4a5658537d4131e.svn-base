<%@include file="../../../include.jsp"%>

<SCRIPT type="text/javascript">
$(document).ready(function() {
	$("#availableDate").datepicker({ dateFormat: 'mm/dd/yy', changeMonth: true, changeYear: true });
	
	// Watch driverSchool event
	$("#driverSchoolYesId").change(function() {
		enableDisableSchool()
	});
	$("#driverSchoolNoId").change(function() {
		enableDisableSchool()
	});
	
	// Check current value of driverSchool
	enableDisableSchool();
	
	// Watch eligibleEmployment event
	$("#eligibleEmploymentYesId").change(function() {
		enableDisableEligible()
	});
	$("#eligibleEmploymentNoId").change(function() {
		enableDisableEligible()
	});
	
	// Check current value of eligibleEmployment
	enableDisableEligible();
});

function enableDisableSchool() {
	
	var isChecked = $("#driverSchoolYesId").prop("checked");
	if (isChecked) {
		$("#driverSchoolName").prop("disabled", false);
	} else {
		$("#driverSchoolName").prop("disabled", true);
	}
}

function enableDisableEligible() {
	
	var isChecked = $("#eligibleEmploymentYesId").prop("checked");
	if (isChecked) {
		$("#notEligibleExplanation").prop("disabled", true);
	} else {
		$("#notEligibleExplanation").prop("disabled", false);
	}
}
</SCRIPT>


<h1>Driver Information</h1>
<form:form commandName="driverInformationForm"  class="brform">
	<fieldset>
		<label for="availableDate">When can you begin working?</label>
		<form:input path="availableDate" id="availableDate" cssStyle="width: 150px;"/>&nbsp;&nbsp;(MM/DD/YYYY)
		<form:errors path="availableDate" cssClass="error" />
		<br />

		<label for="felonyConviction">Have you ever been convicted of a felony?</label>
		<form:radiobutton path="felonyConviction" value="true"/>
			<spring:message code="label.Yes"/>
		<form:radiobutton path="felonyConviction" value="false"/>
			<spring:message code="label.No"/>
		<form:errors path="felonyConviction" cssClass="error" />
		<br />

		<label for="duiConviction">Have you ever been convicted of driving under the influence of alcohol or drugs?</label>
		<form:radiobutton path="duiConviction" value="true"/>
			<spring:message code="label.Yes"/>
		<form:radiobutton path="duiConviction" value="false"/>
			<spring:message code="label.No"/>
		<form:errors path="duiConviction" cssClass="error" />
		<br />

		<label for="licenseRevoked">Has your license ever been suspended or revoked?</label>
		<form:radiobutton path="licenseRevoked" value="true"/>
			<spring:message code="label.Yes"/>
		<form:radiobutton path="licenseRevoked" value="false"/>
			<spring:message code="label.No"/>
		<form:errors path="licenseRevoked" cssClass="error" />
		<br />

		<label for="controlledSubstance">Have you ever tested positive for controlled substances?</label>
		<form:radiobutton path="controlledSubstance" value="true"/>
			<spring:message code="label.Yes"/>
		<form:radiobutton path="controlledSubstance" value="false"/>
			<spring:message code="label.No"/>
		<form:errors path="controlledSubstance" cssClass="error" />
		<br />

		<label for="highestGradeCompleted">Highest Grade Completed</label>
		<form:select path="highestGradeCompleted" id="highestGradeCompleted">
			<form:option value="1">1</form:option>
			<form:option value="2">2</form:option>
			<form:option value="3">3</form:option>
			<form:option value="4">4</form:option>
			<form:option value="5">5</form:option>
			<form:option value="6">6</form:option>
			<form:option value="7">7</form:option>
			<form:option value="8">8</form:option>
			<form:option value="9">9</form:option>
			<form:option value="10">10</form:option>
			<form:option value="11">11</form:option>
			<form:option value="12">12</form:option>
			<form:option value="Some college / technical school">Some college / technical school</form:option>
			<form:option value="College graduate">College graduate</form:option>
			<form:option value="Graduate school / Advanced degree">Graduate school / Advanced degree</form:option>
		</form:select>
		<form:errors path="highestGradeCompleted" cssClass="error" />
		<br />

		<label for="driverSchool">Have you attended a driver or trade school?</label>
		<form:radiobutton path="driverSchool" value="true" id="driverSchoolYesId" />
			<spring:message code="label.Yes"/>
		<form:radiobutton path="driverSchool" value="false" id="driverSchoolNoId" />
			<spring:message code="label.No"/>
		<form:errors path="driverSchool" cssClass="error" />
		<br />

		<label for="driverSchoolName">Name of School</label>
		<form:input path="driverSchoolName" id="driverSchoolName" disabled="true" />
		<form:errors path="driverSchoolName" cssClass="error" />
		<br />

		<label for="eligibleEmployment">Are you legally eligible for employment in the US?</label>
		<form:radiobutton path="eligibleEmployment" value="true" id="eligibleEmploymentYesId" />
			<spring:message code="label.Yes"/>
		<form:radiobutton path="eligibleEmployment" value="false" id="eligibleEmploymentNoId" />
			<spring:message code="label.No"/>
		<form:errors path="eligibleEmployment" cssClass="error" />
		<br />
		
		<label for="notEligibleExplanation">If Not eligible, please explain</label>
		<form:input path="notEligibleExplanation" id="notEligibleExplanation" disabled="true" />
		<form:errors path="notEligibleExplanation" cssClass="error" />
		<br />

	</fieldset>
	<input type="submit" value="Save" />
	<input type="submit" name="next" value="Save & Next" />
</form:form>