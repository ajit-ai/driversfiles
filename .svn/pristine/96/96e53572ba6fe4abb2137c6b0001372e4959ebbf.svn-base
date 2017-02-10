<%@include file="../../include.jsp"%>

<div style="width: 100%;">
	<div id="application">
		
		<c:set var="companyName" value="DRIVER'S FILES" />
		<c:if test="${!empty company}">
			<c:set var="companyName" value="${fn:toUpperCase(company.name)}" />
			<table style="margin-top: 50px;padding: 5px;" cellpadding="10px" >
				<tr>
					<td width="33%" valign="middle" align="right">
						${company.address1}<br />
						<c:if test="${!empty company.address2}">
						${company.address2}<br />
						</c:if>
						${company.city}, ${company.state }<br />
						${company.postalCode}<br />
					</td>
					<td>
						<c:if test="${!empty companyIconUrl}">
							<img id="currentIcon" src="${pageContext.request.contextPath}${companyIconUrl}" title="Company Icon" />
						</c:if>
					</td>
					<td width="33%">
						Ph ${company.phone}<br />
						Fax ${company.fax}<br />
					</td>
				</tr>
				<tr>
					<td colspan="3" style="height: 20px;">&nbsp;</td>
				</tr>
			</table>
		</c:if>
	
		<div class="header1" style="text-align: center;">DRIVER APPLICATION FOR EMPLOYMENT</div>
		
		<div id="discriminationDisclaimer" class="box" style="margin-right: 50px; margin-left: 50px; border: 1px solid black;">
			We consider applicants for all positions without regard to RACE, COLOR, RELIGION, SEX, NATIONAL ORIGIN, AGE, MARITAL
			STATUS, VETERAN STATUS, DISABILITY, or any other legally protected status.
		</div>
		
		<table style="margin-top: 50px;">
			<tr>
				<td class="underline" width="60%"></td>
				<td></td>
				<td class="underline" width="30%"></td>
			</tr>
			<tr>
				<td align="center">Signature of Applicant</td>
				<td></td>
				<td align="center">Date</td>
			</tr>
		</table>
		
		<table class="topMargin1">
			<tr>
				<td>Name</td>
				<td class="underline paddedData" width="60%">${person.firstName} ${person.lastName}</td>
				<td style="padding-right: 15px;"></td>
				<td>Phone</td>
				<td class="underline paddedData" width="30%">${driver.phone}</td>
			</tr>
		</table>
		
		<table class="topMargin1">
			<tr>
				<td class="nobr">Date of Birth</td>
				<td class="underline paddedData" width="50%">
					<fmt:formatDate value="${driver.dob}" pattern="MM-dd-yyyy" var="varDob" />
					${varDob}
				</td>
				<td style="padding-right: 50px;"></td>
				<td class="nobr">Social Security Number</td>
				<td class="underline paddedData" width="50%">${driver.ssn}</td>
			</tr>
		</table>
		
		<table class="topMargin1">
			<tr>
				<td class="nobr">Current Address</td>
				<td class="underline" width="30%" align="center">${driver.address1}</td>
				<td class="underline" width="30%" align="center">${driver.city}</td>
				<td class="underline" width="20%" align="center">${driver.state}</td>
				<td class="underline" width="20%" align="center">${driver.postalCode}</td>
			</tr>
			<tr>
				<td></td>
				<td align="center">Street</td>
				<td align="center">City</td>
				<td align="center">State</td>
				<td align="center">Zip</td>
			</tr>
		</table>
		
		<table class="topMargin1">
			<tr>
				<td class="nobr">Previous 3 years</td>
				<td align="center" class="underline" width="30%">${(!empty residences)? residences[0].address1:''} ${(!empty residences)? residences[0].address2:''}</td>
				<td align="center" class="underline" width="30%">${(!empty residences)? residences[0].city:''}</td>
				<td align="center" class="underline" width="20%">${(!empty residences)? residences[0].state:''}</td>
				<td align="center" class="underline" width="20%">${(!empty residences)? residences[0].postalCode:''}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td align="center">Street</td>
				<td align="center">City</td>
				<td align="center">State</td>
				<td align="center">Zip</td>
			</tr>
		</table>
		
		<c:choose>
			<c:when test="${!empty residences && fn:length(residences) > 1}">
				<c:forEach items="${residences}" var="residence" begin="1">
					<table style="margin-top: 30px;">
						<tr>
							<td align="center" class="underline" width="30%">${residence.address1} ${residence.address2}</td>
							<td align="center" class="underline" width="30%">${residence.city}</td>
							<td align="center" class="underline" width="20%">${residence.state}</td>
							<td align="center" class="underline" width="20%">${residence.postalCode}</td>
						</tr>
						<tr>
							<td align="center">Street</td>
							<td align="center">City</td>
							<td align="center">State</td>
							<td align="center">Zip</td>
						</tr>
					</table>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<table style="margin-top: 30px;">
					<tr>
						<td class="underline" style="padding-right: 108px;"></td>
						<td class="underline" width="30%"></td>
						<td class="underline" width="30%"></td>
						<td class="underline" width="20%"></td>
						<td class="underline" width="20%"></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td align="center">Street</td>
						<td align="center">City</td>
						<td align="center">State</td>
						<td align="center">Zip</td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose>
		
		<table class="topMargin1">
			<tr>
				<td class="nobr">WHEN CAN YOU BEGIN WORKING?</td>
				<td align="center" class="underline" width="50%">
					<c:if test="${!empty driver.availableDate}">
						<fmt:formatDate value="${driver.availableDate}" pattern="MM-dd-yyyy"/>
					</c:if>
				</td>
				<td style="padding-right: 15px;"></td>
				<td class="nobr">POSITION APPLYING FOR:</td>
				<td align="center" class="underline" width="50%"></td>
			</tr>
		</table>
		
		<table class="topMargin1">
			<tr>
				<td colspan="3">ARE YOU LEGALLY ELIGIBLE FOR EMPLOYMENT IN THE U.S.? - 
					(${(!empty driver.eligibleEmployment && driver.eligibleEmployment)?'X':' '}) YES 
					(${(!empty driver.eligibleEmployment && !driver.eligibleEmployment)?'X':' '}) NO;
				</td>
			</tr>
			<tr>
				<td style="padding-right: 222px;"></td>
				<td class="nobr">IF NO EXPLAIN:</td>
				<td class="underline" width="100%">
					<c:if test="${!empty driver.eligibleEmployment && !driver.eligibleEmployment}">
						${driver.notEligibleExplanation}
					</c:if>
				</td>
			</tr>
		</table>
		
		<table class="topMargin1">
			<tr>
				<td colspan="3">HAVE YOU EVER BEEN EMPLOYED WITH US? - 
					( ) YES 
					( ) NO;
				</td>
			</tr>
			<tr>
				<td style="padding-right: 222px;"></td>
				<td class="nobr">IF YES, PROVIDE MONTH AND YEAR:</td>
				<td class="underline" width="100%"></td>
			</tr>
		</table>
		
		<table class="topMargin1">
			<tr>
				<td colspan="2">HAVE YOU EVER BEEN CONVICTED OF A FELONY? - 
					(${(!empty driver.felonyConviction && driver.felonyConviction)?'X':' '}) YES 
					(${(!empty driver.felonyConviction && !driver.felonyConviction)?'X':' '}) NO; IF YES, EXPLAIN AND GIVE
				</td>
			</tr>
			<tr>
				<td>DETAILS</td>
				<td class="underline" width="100%">${driver.felonyConvictionExplanation}</td>
			</tr>
		</table>
		
		<div class="topMargin1">HAVE YOU EVER TESTED POSITIVE FOR CONTROLLED SUBSTANCE(S)? - 
			(${(!empty driver.controlledSubstance && driver.controlledSubstance)?'X':' '}) YES 
			(${(!empty driver.controlledSubstance && !driver.controlledSubstance)?'X':' '}) NO
		</div>
		
		<table class="topMargin1">
			<tr>
				<td class="nobr">IN CASE OF AN EMERGENCY NOTIFY:</td>
				<td align="center" class="underline" width="30%">${driver.contactName}</td>
				<td align="center" class="underline" width="30%">${driver.contactRelationship}</td>
				<td align="center" class="underline" width="40%">${driver.contactPhone}</td>
			</tr>
			<tr>
				<td></td>
				<td align="center">Name</td>
				<td align="center">Relation</td>
				<td align="center">Phone Number</td>
			</tr>
		</table>
		
		<div class="header1 topMargin1" style="text-align: center;">REQUIRED DOCUMENTS FOR DRIVER APPLICATIONS</div>
		<div class="topMargin1" style="margin-left: 25px;">
			<div>1.  COPY OF CDL
				<c:if test="${!empty cdlDoc}">
					:<a href="${pageContext.request.contextPath}/secure/api/document/${cdlDoc.uuid}" target="_cdlDoc">&nbsp;click here</a>
				</c:if>
			</div>
			<div>2.  COPY OF SOCIAL SECURITY CARD
				<c:if test="${!empty medCardDoc}">
					:<a href="${pageContext.request.contextPath}/secure/api/document/${medCardDoc.uuid}" target="_medCardDoc">&nbsp;click here</a>
				</c:if>
			</div>
			<div>3.  COPY OF LONG FORM PHYSICAL
				<c:if test="${!empty physicalDoc}">
					:<a href="${pageContext.request.contextPath}/secure/api/document/${physicalDoc.uuid}" target="_physicalDoc">&nbsp;click here</a>
				</c:if>
			</div>
			<div>4.  COPY OF MEDICAL CARD
				<c:if test="${!empty ssCardDoc}">
					:<a href="${pageContext.request.contextPath}/secure/api/document/${ssCardDoc.uuid}" target="_ssCardDoc">&nbsp;click here</a>
				</c:if>
			</div>
		</div>
		<div class="topMargin1" style="font-size: smaller; font-style: italic;">If additional space is required, the applicant can use a blank sheet of paper and attach it to this application.</div>
		
		<div class="header1 topMargin1">LIST ALL DRIVER LICENSES HELD IN THE PAST THREE (3) YEARS</div>
		<table class="borders" border="1">
			<tr>
				<td align="center" valign="bottom" style="height: 35px;" width="15%">STATE</td>
				<td align="center" valign="bottom" width="35%">LICENSE NUMBER</td>
				<td align="center" valign="bottom" width="15%">TYPE</td>
				<td align="center" valign="bottom" width="35%">EXPIRATION DATE</td>
			</tr>
			<c:choose>
				<c:when test="${!empty licenses}">
					<c:forEach items="${licenses}" var="license">
						<tr>
							<td align="center">${license.state}</td>
							<td align="center">${license.number}</td>
							<td align="center">${license.type.name}</td>
							<td align="center">
								<fmt:formatDate value="${license.expiration}" var="licExpDate" pattern="MM-dd-yyyy" />
								${licExpDate}
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td style="height: 25px;" colspan="4">  No Records</td></tr>
					<tr><td style="height: 25px;"></td><td></td><td></td><td></td></tr>
				</c:otherwise>
			</c:choose>
		</table>

		<table class="topMargin1">
			<tr>
				<td colspan="3">Has your license ever been suspended or revoked? - 
					(${(!empty driver.licenseRevoked && driver.licenseRevoked)?'X':' '}) YES 
					(${(!empty driver.licenseRevoked && !driver.licenseRevoked)?'X':' '}) NO;
				</td>
			</tr>
			<tr>
				<td style="padding-right: 175px;"></td>
				<td class="nobr">If yes, explain,</td>
				<td class="underline" width="100%">${driver.licenseRevokedExplanation}</td>
			</tr>
		</table>

		<table class="topMargin1">
			<tr>
				<td colspan="3">Have you ever been convicted of driving under the influence of alcohol or drugs? - 
					(${(!empty driver.duiConviction && driver.duiConviction)?'X':' '}) YES 
					(${(!empty driver.duiConviction && !driver.duiConviction)?'X':' '}) NO;
				</td>
			</tr>
			<tr>
				<td style="padding-right: 175px;"></td>
				<td class="nobr">If yes, When?</td>
				<td class="underline" width="100%">
					<fmt:formatDate value="${driver.duiConvictionDate}" var="duiConvictDate" pattern="MM-dd-yyyy" />
					${duiConvictDate}
				</td>
			</tr>
		</table>
		
		<div class="header1 topMargin1">ACCIDENT RECORD</div>
		<div>
			LIST ALL ACCIDENTS YOU HAVE BEEN INVOLVED IN WHILE OPERATING A TRUCK, CAR, MOTORCYCLE, OR OTHER MOTORIZED VEHICLE.
			INCLUDE ALL ACCIDENTS WHETHER AT FAULT OR NOT AT FAULT IN THE PAST 10 YEARS (IF NONE, WRITE NONE) AND ANY PROPERTY
			DAMAGE.
		</div>
		<table class="borders topMargin1" border="1">
			<tr>
				<td align="center" valign="bottom" style="height: 35px;" width="13%">DATE</td>
				<td align="center" valign="bottom" width="12%">TYPE</td>
				<td align="center" valign="bottom" width="25%">NATURE OF ACCIDENT (head-on, rear end, upset, etc.)</td>
				<td align="center" valign="bottom" width="10%">WERE YOU AT FAULT</td>
				<td align="center" valign="bottom" width="15%">FATALITIES</td>
				<td align="center" valign="bottom" width="10%">INJURIES</td>
				<td align="center" valign="bottom" width="15%">AMOUNT OF PROPERTY DAMAGE</td>
			</tr>
			<c:choose>
				<c:when test="${!empty accidents}">
					<c:forEach items="${accidents}" var="accident">
						<tr>
							<td style="height: 25px;"><fmt:formatDate value="${accident.accidentDate}" pattern="MM-dd-yyyy" /></td>
							<td>${accident.type}</td>
							<td>${accident.nature}</td>
							<td>${(accident.atFault)?'Yes':'No'}</td>
							<td>${(accident.fatalities)?'Yes':'No'}</td>
							<td>${(accident.injuries)?'Yes':'No'}</td>
							<td>
								<fmt:formatNumber value="${accident.damages}" type="currency" var="damagesAmt" />
								${damagesAmt}
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td style="height: 25px;" colspan="7">No Records</td></tr>
				</c:otherwise>
			</c:choose>
		</table>
		
		<div class="header1 topMargin1">TRAFFIC CONVICTIONS AND FORFEITURES</div>
		<div>
			LIST ALL TRAFFIC CONVICTIONS, FIRFEITURES OR SUSPENSIONS OF A LICENSE IN A MOTOR VEHICLE (OTHER THAN PARKING) FOR THE
			PAST 10 YEARS (IF NONE, WRITE NONE)
		</div>
		<table class="borders topMargin1" border="1">
			<tr>
				<td align="center" valign="bottom" style="height: 35px;" width="13%">DATE</td>
				<td align="center" valign="bottom" width="27%">LOCATION</td>
				<td align="center" valign="bottom" width="40%">CHARGE</td>
				<td align="center" valign="bottom" width="20%">PENALTY</td>
			</tr>
			<c:choose>
				<c:when test="${!empty traffics}">
					<c:forEach items="${traffics}" var="traffic">
						<tr>
							<td style="height: 25px;"><fmt:formatDate value="${traffic.trafficDate}" pattern="MM-dd-yyyy" /></td>
							<td>${traffic.city}, ${traffic.state}</td>
							<td>${traffic.charge}</td>
							<td>${traffic.penalty}</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="4" style="height: 25px;">No Records</td></tr>
				</c:otherwise>
			</c:choose>
		</table>
		
		<div class="topMargin1">
		<c:if test="${!empty driver.highestGradeCompleted}">
			Highest grade completed: ${driver.highestGradeCompleted}
		</c:if>
		<c:if test="${empty driver.highestGradeCompleted}">
			Circle highest grade completed: 1 2 3 4 5 6 7 8 9 10 11 12 College: 1 2 3 4
		</c:if>
		</div>
	
		<table class="topMargin1">
			<tr>
				<td colspan="3">Driver School / Trade School? - 
					(${(!empty driver.driverSchool && driver.driverSchool)?'X':' '}) YES 
					(${(!empty driver.driverSchool && !driver.driverSchool)?'X':' '}) NO;
				</td>
			</tr>
			<tr>
				<td style="padding-right: 175px;"></td>
				<td class="nobr">If yes, Name of School?</td>
				<td class="underline" width="100%" align="center">${driver.driverSchoolName}</td>
			</tr>
		</table>
		
		
		<table class="topMargin1">
			<tr>
				<td class="nobr">Last school attended</td>
				<td class="underline" width="40%"></td>
				<td class="underline" width="60%"></td>
			</tr>
			<tr>
				<td></td>
				<td align="center">Name</td>
				<td align="center">Address</td>
			</tr>
		</table>
		
		<div class="topMargin1" style="font-size: smaller; font-style: italic;">If additional space is required, the applicant can use a blank sheet of paper and attach it to this application.</div>
		
		<div class="header1 topMargin1" style="text-align: center;">EMPLOYMENT RECORD</div>
		<div class="topMargin1">
			Begin with your present or most recent job and work backward in order, LISTING YOUR EMPLOYERS FOR THE LAST 10 YEARS. Include
			all full- and part-time employment. All time must be accounted for including military service, school, self-employment, and
			periods or unemployment.  WE MUST HAVE TELEPHONE NUMBERS FOR ALL EMPLOYERS. If additional space is required, the applicant
			can use a blank sheet of paper and attach it to this application.
		</div>
		
		<c:choose>
			<c:when test="${!empty employments}">
				<c:forEach items="${employments}" var="employ">
					<table class="topMargin1">
						<tr>
							<td class="nobr" width="20">Name:</td>
							<td class="underline" width="60%">${employ.name}</td>
							<td class="nobr">Supervisor:</td>
							<td class="underline" width="100%">${employ.supervisor}</td>
						</tr>
					</table>
					<table class="topMargin2">
						<tr>
							<td class="nobr" width="20">Address:</td>
							<td class="underline" width="60%">${employ.address}, ${employ.city}, ${employ.state}</td>
							<td class="nobr">Zip</td>
							<td class="underline" width="20%" align="center">${employ.postalCode}</td>
							<td class="nobr">Phone</td>
							<td class="underline" width="100%">${employ.phone}</td>
						</tr>
					</table>
					<table class="topMargin2">
						<tr>
							<td class="nobr" width="20">Position Held:</td>
							<td class="underline" width="50%">${employ.position}</td>
							<td class="nobr">From:</td>
							<td class="underline" width="20%">
								<fmt:formatDate value="${employ.fromDate}" var="employFromDt" pattern="MM-dd-yyyy"/>
								${employFromDt}
							</td>
							<td class="nobr">To:</td>
							<td class="underline" width="100%">
								<fmt:formatDate value="${employ.toDate}" var="employToDt" pattern="MM-dd-yyyy"/>
								${employToDt}
							</td>
						</tr>
					</table>
					<table class="topMargin2">
						<tr>
							<td class="nobr">Reason for Leaving:</td>
							<td class="underline" width="100%">${employ.leaving}</td>
						</tr>
					</table>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<table class="topMargin1">
					<tr>
						<td class="nobr" width="20">No Employment Records</td>
					</tr>
				</table>
				<br />
			</c:otherwise>
		</c:choose>
		
		<div class="header1 topMargin1">EMPLOYMENT RECORD Continued</div>
		<div class="topMargin1">
			I hereby certify that all questions answered are true to the best of my knowledge.  I authorize <span class="companyName">${companyName}
			</span> (hereinafter "the Company") to contact my former employers, references furnished, and all other sources that they see
			fit in order to verify the facts and information furnished. I have included in my application documents verifying my
			citizenship status or ability to legally work in the United States as required by the Immigration Reform Act and Control Act
			of 1986 and any other applicable laws and regulations.<br />
			I understand that a pre-employment physical, including drug screening and breath/alcohol tests, will be used by the Company
			to determine my ability to perform the position for which I am applying.  I understand that the completion of this or any
			other application does not assure me a position with the Company or obligate the Company in any way.  This application for
			employment shall be considered active for a period of time not to exceed forty-five (45) days.  Any applicant wishing to be
			considered for employment beyond this time period should inquire as to whether or not applications are being accepted at
			that time.<br />
			I hereby understand and acknowledge that unless otherwise defined by applicable law, any employment relationship with this
			organization is of an "at will" nature, which means that the employee may resign at any time and the employer may discharge
			employee at any time with our without cause.  It is further understood that this "at will" employment relationship may not be
			changed unless such change is specifically acknowledged by an authorized executive of the Company executed in writing.<br />
			I further understand that this application is not, nor is it intended to be, a contract of employment and any employment
			relationship established between the applicant and the Company may be terminated at the will of either the applicant or the
			company.<br />
			Should any employment relationship occur, I understand that I am required to abide by all rules and regulations of the
			Company.  I understand that any misleading, incorrect, or omitted statements may render this application null and void and,
			if employed, would be cause for immediate termination of employment with the Company.<br />
			I certify that this application was completed by me, and that all entries on it and information in it are true and complete
			to the best of my knowledge.  I also understand and agree that any conduct which would have been reason for my discharge may
			be used against me by the Company even if it is acquired after my employment ceases.  I agree to submit a urine sample and/or
			specimen testing including breath/alcohol tests for the purpose of drug/controlled substance screening for pre-employment
			medical qualifications and thereafter as warranted by <span class="companyName">${companyName}'S</span>
			policy and federal regulatory agencies.
		</div>
		
		<table style="margin-top: 40px;">
			<tr>
				<td style="padding-left: 160px;">X</td>
				<td class="underline" width="50%"></td>
				<td style="padding-right: 25px;"></td>
				<td class="underline" width="25%"></td>
			</tr>
			<tr>
				<td></td>
				<td align="center">Signature of Applicant</td>
				<td></td>
				<td align="center">Date</td>
			</tr>
		</table>
		
		<div style="text-align: center; font-weight: bold; font-size: x-large; margin-top: 35px;">RELEASE OF INFORMATION</div>
		<div class="topMargin1">NOTE: THIS DOCUMENT MUST BE RETURNED WITH YOUR COMPLETED AND SIGNED APLICATION</div>
		
		<div style="margin-top: 50px;">
		I hereby acknowledge that <span class="companyName">${companyName}</span>
		may request the following information from any prior employer or any of their respective agents and employee's as required by
		49 CFR &sect;382.413:
		</div>
		
		<ol>
			<li>Any positive result from a controlled substance or alcohol test and the date of such test;<br />
				and</li><br />
			<li>Any refusals to take a controlled substance test or alcohol test and the date of refusal.</li>
		</ol>
		
		<div class="topMargin1">
			I understand that my refusal to sign this release will disqualify me from obtaining a commercial driving position with
			<span class="companyName">${companyName}</span>
		</div>
		
		<div class="topMargin1">
			I hereby authorize and consent to <span class="companyName">${companyName}</span> obtaining any and all
			information that may be required regarding my driving experience, personnel record, and/or character without recourse.
			I understand that if qualified, any misrepresentation or false statement on my driving application revealed at a later
			date shall be considered sufficient cause for disqualification or termination. I also understand that this release in no
			way assures that applicant will be qualified as a commercial driver with <span class="companyName">${companyName}</span>.
		</div>
		
		<div class="topMargin1">
			I hereby knowingly and voluntarily release all persons and entities from any and all claims or liabilities for release
			information described in this form to those identified in the preceding paragraphs.
		</div>
		
		<div class="topMargin1">
			I certify that I have read, understood and agree to all the provisions of this form.
		</div>
		
		<table class="topMargin1">
			<tr>
				<td style="padding-right: 100px; padding-top: 40px;"></td>
				<td valign="bottom" class="nobr">Signature:</td>
				<td class="underline" colspan="4" width="100%"></td>
			</tr>
			<tr>
				<td style="padding-top: 40px;"></td>
				<td valign="bottom" class="nobr">Print Name:</td>
				<td class="underline" colspan="4" valign="bottom">${person.firstName} ${person.lastName}</td>
			</tr>
			<tr>
				<td style="padding-top: 40px;"></td>
				<td valign="bottom" class="nobr">Date:</td>
				<td class="underline" width="30%" valign="bottom"><fmt:formatDate value="${today}" pattern="MM-dd-yyyy" /></td>
				<td style="padding-right: 50px;"></td>
				<td valign="bottom">SS#:</td>
				<td class="underline" width="100%" valign="bottom">${driver.ssn}</td>
			</tr>
		</table>
		
		<c:if test="${!empty activeTruck}" >
			<c:set var="lessorName" value="${activeTruck.lessorName}" />
			<c:set var="lessorAddress1" value="${activeTruck.lessorAddress1}" />
			<c:set var="lessorAddress2" value="${activeTruck.lessorAddress2}" />
			<c:set var="lessorCity" value="${activeTruck.lessorCity}" />
			<c:set var="lessorState" value="${activeTruck.lessorState}" />
			<c:set var="lessorPostalCode" value="${activeTruck.lessorPostalCode}" />
			<c:set var="lessorPhone" value="${activeTruck.lessorPhone}" />
			<c:set var="lessorGovId" value="${activeTruck.lessorGovId}" />
		</c:if>
		
		
		<div class="header1" style="margin-top: 60px; text-align: center;">TRUCK APPLICATION CONTRACT OPERATING AGREEMENT</div>
		
		<div class="topMargin1" style="line-height: 2;">
			This agreement made this <span class="underline" style="padding-right: 50px;"> &nbsp;<fmt:formatDate value="${today}" pattern="dd" /></span>
			 day of <span class="underline" style="padding-right: 150px;"> &nbsp;<fmt:formatDate value="${today}" pattern="MMMM, yyyy" /></span>
			 between <span class="companyName">${companyName}
			</span> of <span style="text-decoration: underline;">Jacksonville, Florida</span>, known as CARRIER and
			<span class="underline" style="padding-right: ${(empty lessorName)?'200':'10'}px;"><br />&nbsp;${lessorName}</span> of 
			<span class="underline" style="padding-right: ${(empty lessorName)?'200':'10'}px;">${lessorCity}, ${lessorState}</span>,
			known as CONTRACTOR.
		</div>
		
		<div class="topMargin1">
			Whereas, the CARRIER, a for-hire motor carrier, operating under authority issued by the Interstate Commerce Commission
			wishes to obtain transportation with equipment it does not own through an agreement with CONTRACTOR.
		</div>
		
		<div class="topMargin1">
			Whereas, the CARRIER and CONTRACTOR desires to enter into an agreement to carry out the foregoing.
		</div>
		
		<div class="topMargin1">
			Now, therefore, in consideration of the mutual covenants and agreements contained herein, the parties mutually agree as follows:
		</div>
		
		<ol>
			<li>The CONTRACTOR agrees to use all necessary labor to transport, load, and unload on behalf of such other carriers as
			CARRIER may designate through authorized "trip lease" of interchange agreements, such commodities available so that the
			CONTRACTOR shall be able to keep under the terms of this agreement, although this shall not be construed as an
			agreement by the CARRIER to furnish any specific number of loads or pounds of freight for transportation by the
			CONTRACTOR any particular place.</li><br />
			<li>CONTRACTOR to receive <span class="underline" style="padding-right: 50px;"></span>% or amount agreed on by agent
			and owner / operator of the gross revenue on all loads with the following exceptions.</li><br />
			<li>CONTRACTOR shall be liable for the first <span class="underline">$2,500.00</span> of all cargo and liability claims
			that <span class="companyName">${companyName}</span> is deemed liable for.  If an accident claim
			arises out of the driver's negligence the full responsibility of the claim will be the contractor's responsibility.
			CONTRACTOR shall be liable for all fines; overweight, and traffic and all other violations.  CONTRACTOR shall carry
			workman's compensation and provide verification of such to CARRIER.</li><br />
			<li>CONTRACTOR shall be personally responsible for paying federal highway use tax and federal income and state
			income taxes as well as social security taxes.  CARRIER shall provide to CONTRACTOR by mail or in person those documents
			showing full and proper performance of the terms of this agreement on each trip.  The required documents shall include
			delivery receipts, bill of ladings, logs, vehicle condition reports and other such evidence of proper delivery.</li><br />
			<li>The CONTRACTOR agrees to provide check-in calls to CARRIER every day between 7:30 a.m. and 9:00 a.m. and again
			between 3:30 p.m. and 5:00 p.m. Monday through Saturday at the CARRIER'S expense.</li><br />
		</ol>
		
		<table class="topMargin1">
			<tr>
				<td class="nobr" style="font-weight: bold;">Carrier's initials</td>
				<td class="underline" style="padding-right: 100px;"></td>
				<td width="100%"></td>
				<td class="nobr" style="font-weight: bold;">Contractor's initials</td>
				<td class="underline" style="padding-right: 100px;"></td>
			</tr>
		</table>
		
		<ol start="6" style="margin-top: 40px;">
			<li>In the event of an accident, the CONTRACTOR will notify CARRIER immediately and have the trailer returned to a specified
			location as designated by the CARRIER at the CONTRACTOR'S expense.</li><br />
			<li>If, for any reason, an attorney has to be consulted during this contract, the CONTRACTOR agrees to pay any and all
			attorney's fees.</li><br />
			<li>(a) CONTRACTOR and CARRIER agree that any action at law or in equity with regard to this Agreement or with regard to
			any rights, claims, payments, duties, or liabilities thereunder, or regarding the interpretation or construction of any
			terms of this Agreement, shall be governed by the laws of the State of Utah, and any dispute hereunder shall be brought
			in a court of competent jurisdiction in Wasatch County, Utah.<br /><br />
			(b) In the event that CONTRACTOR files an action against CARRIER in any court other than in Wasatch County, Utah, 
			CONTRACTOR agrees to reimburse CARRIER, upon demand, for CARRIER's attorney fees and expenses which it incurs in seeking
			transfer of such action to Wasatch County, Utah, regardless of which party prevails in the action.</li>
			<br />
			<li>The CONTRACTOR will furnish his or her base plate.</li><br />
			<li>The CONTRACTOR agrees to pay for physical damage and Bobtail insurance.</li><br />
			<li>The CONTRACTOR agrees to pay all road and fuel tax at the end of each quarter of the year on a prorated basis for
			the fleet he or she operates in.</li><br />
			<li>The CONTRACTOR agrees to pay for oil, filters, truck maintenance and repair, tires, and provide a copy of all receipts
			to Safety Department.</li><br />
			<li>If a default occurs before the end of this contract the final settlement and security deposit will not be paid for
			ninety (90) days after termination of the contract.  This will allow for any bills to be received by this office.</li><br />
			<li>CONTRACTOR agrees and acknowledges by his or her signature herein that he or she operates for
			<span class="companyName">${companyName}</span> as a contractor and not an employee.</li><br />
			<li><span class="companyName">${companyName}</span> will make available to all lessors advances
			sufficient to complete all loads not to exceed 10% of the lessor load revenue.</li><br />
			<li>CONTRACTOR agrees and acknowledges that in the event of termination of this contract he or she will turn into 
			carrier the door placards, fuel cards, delivery receipts, seals, manifests, company manual, and driver's logs.  Failure
			to do so will result in a deduction of $250.00 from the final settlement.  No final settlement will be prepared until
			all of the above items have been provided.</li><br />
			<li>If default occurs within the ninety (90) day probationary period, an early termination fee of $250.00 will be
			deducted from teh final settlement for costs incurred during approval.</li><br />
		</ol>
		
		<table class="topMargin1">
			<tr>
				<td class="nobr" style="font-weight: bold;">Carrier's initials</td>
				<td class="underline" style="padding-right: 100px;"></td>
				<td width="100%"></td>
				<td class="nobr" style="font-weight: bold;">Contractor's initials</td>
				<td class="underline" style="padding-right: 100px;"></td>
			</tr>
		</table>
		
		<ol start="18" style="margin-top: 40px;">
			<li>A sum of $2,500.00 will be held in a security account for the duration of the lease.  $50.00 per week will be
			deducted from the CONTRACTOR'S settlement until the balance is paid in full.</li><br />
			<li>The owner-operator or driver, periodically throughout this lease agreement, must attend the company provided safety
			seminars and training courses.  There will be a seven-day notice given to each terminal prior to the course.</li><br />
			<li>The owner-operator is required to give <span class="companyName">${companyName}</span> seven
			(7) days notice of termination to the corporate office, prior to termination; failure to do so will result in a
			$500.00 fine.</li><br />
		</ol>
		
		<div class="topMargin1" style="line-height: 2;">
			WITNESS WHEREOF, the parties hereto have executed this agreement this 
			
			<span class="underline" style="padding-right: 150px;"> &nbsp;<fmt:formatDate value="${today}" pattern="dd" /></span>
			 day of <span class="underline" style="padding-right: 200px;"> &nbsp;<fmt:formatDate value="${today}" pattern="MMMM, yyyy" /></span>

			
			and same shall be considered upon both parties and shall remain in full force and effect unless and until canceled
			according to the terms of this agreement.
		</div>
		
		<table class="topMargin1" border="0">
			<tr>
				<td valign="top" width="50%">
					<table>
						<tr>
							<td colspan="2" valign="top" align="center" valign="top"><span class="underline" style="font-weight: bold;">CONTRACTOR</span></td>
						</tr>
						<tr>
							<td class="nobr topPad1" align="right">Name:</td>
							<td class="underline" width="100%" valign="bottom">${lessorName}</td>
						</tr>
						<tr>
							<td class="nobr topPad2" align="right">Address:</td>
							<td class="underline" width="100%" valign="bottom">${lessorAddress1}</td>
						</tr>
						<tr>
							<td class="nobr topPad2">&nbsp;</td>
							<td class="underline" width="100%" valign="bottom">${lessorAddress2}</td>
						</tr>
						<tr>
							<td class="nobr topPad2">&nbsp;</td>
							<td class="underline" width="100%" valign="bottom">${lessorCity}, ${lessorState} ${lessorPostalCode}</td>
						</tr>
						<tr>
							<td class="nobr topPad2">&nbsp;</td>
							<td width="100%"></td>
						</tr>
						<tr>
							<td class="nobr topPad2" align="right">Phone #:</td>
							<td class="underline" width="100%" valign="bottom">${lessorPhone}</td>
						</tr>
						<tr>
							<td class="nobr topPad1" align="right">By:</td>
							<td class="underline" width="100%"></td>
						</tr>
						<tr>
							<td></td>
							<td class="hint" width="100%" align="center">(Contractor's Signature)</td>
						</tr>
					</table>
				</td>
				<td valign="top" width="50%">
					<table>
						<tr>
							<td></td><td align="center"><span class="underline" style="font-weight: bold;">CARRIER</span></td>
						</tr>
						<tr>
							<td></td><td align="center" class="topPad1"><span class="underline"  style="font-weight: bold;">${companyName}</span></td>
						</tr>
						<tr>
							<td></td><td align="center" class="topPad2"><span class="underline">P.O. Box 100</span></td>
						</tr>
						<tr>
							<td></td><td align="center" class="topPad2"><span class="underline">Heber City, Utah 84032</span></td>
						</tr>
						<tr>
							<td></td><td align="center" class="topPad2"><span class="underline">(800) 373-1029</span></td>
						</tr>
						<tr>
							<td></td><td align="center" class="topPad2"><span class="underline">(435) 657-2960 Fax</span></td>
						</tr>
						<tr>
							<td class="nobr topPad2" align="right">F.I.D. #:</td>
							<td class="underline" width="100%" valign="bottom">${lessorGovId}</td>
						</tr>
						<tr>
							<td class="nobr topPad1" align="right">By:</td>
							<td class="underline" width="100%"></td>
						</tr>
						<tr>
							<td></td>
							<td class="hint" width="100%" align="center">(Carrier Representative's Signature)</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
		<c:if test="${!empty activeTruck}" >
			<c:set var="unitNum" value="" />	<!-- TODO: Don't know some values -->
			<c:set var="truckYear" value="${activeTruck.year}" />
			<c:set var="truckMake" value="${activeTruck.make}" />
			<c:set var="truckModel" value="${activeTruck.model}" />
			<c:set var="truckSerial" value="${activeTruck.vin}" />
			<c:set var="truckLicenseState" value="${activeTruck.licenseState}" />
			<c:set var="truckLicense" value="${activeTruck.license}" />
		</c:if>
		
		<div class="header1 topMargin1">TRACTOR INFORMATION:</div>
		<table class="topMargin1">
			<tr>
				<td class="nobr">UNIT NO.</td><td class="underline" width="25%" align="center">${unitNum}</td>
				<td class="nobr">YEAR</td><td class="underline" width="15%" align="center">${truckYear}</td>
				<td class="nobr">MAKE</td><td class="underline" width="30%" align="center">${truckMake}</td>
				<td class="nobr">MODEL</td><td class="underline" width="30%" align="center">${truckModel}</td>
			</tr>
		</table>
		<table class="topMargin1">
			<tr>
				<td class="nobr">SERIAL #</td>
				<td class="underline" width="50%" align="center">${truckSerial}</td>
				
				<td class="nobr">PLATE #</td>
				<td class="underline" width="25%" align="center">${truckLicenseState}</td>
				<td class="underline" width="25%" align="center">${truckLicense}</td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				
				<td></td>
				<td class="hint" align="center">(State)</td>
				<td class="hint" align="center">(Plate #)</td>
			</tr>
		</table>
		
		<div class="topMargin1">
			Signatures by both parties in <span style="font-weight: bold;">this section will void</span> previous agreements and
			release equipment back to the contractor.
		</div>
		
		<table class="topMargin1">
			<tr>
				<td class="underline" width="45%"></td>
				<td width="10%"></td>
				<td class="underline" width="45%"></td>
			</tr>
			<tr>
				<td class="hint" align="center">Contractor's Signature</td>
				<td></td>
				<td class="hint" align="center">Carrier Representative's Signature</td>
			</tr>
			<tr>
				<td class="underline" style="padding-top: 30px;">&nbsp;</td>
				<td></td>
				<td class="underline"></td>
			</tr>
			<tr>
				<td class="hint" align="center">Date</td>
				<td></td>
				<td class="hint" align="center">Date</td>
			</tr>
		</table>

		<div class="header1" style="margin-top: 60px; text-align: center;">ITEMS TO KEEP IN TRUCK AT ALL TIMES</div>
		<ol>
			<li>COPY OF YOUR LEASE</li><br />
			<li>COPY OF YOUR LONG FORM PHYSICAL AND MEDICAL CARD</li><br />
			<li>LAST SEVEN DAYS OF YOUR LOGS- USING OUR BOOKS THIS SHOULD NOT BE A PROBLEM.</li><br />
			<li>COPY OF THE INSURANCE PAPERS</li><br />
			<li>ACCIDENT PACK AND POST ACCIDENT INSTRUCTION SHEET</li><br />
			<li>CAMERA FOR DOCUMENTING ACCIDENTS AND CARGO CLAIMS</li><br />
			<li>CHAIN OF CUSTODY FORM FOR CONTROLLED SUBSTANCE TESTING</li><br />
			<li>DRIVERS MANUAL</li><br />
			<li>COPY OF YOUR TRUCK INSPECTION</li><br />
			<li>YEAR 2000 EMERGENCY RESPONSE GUIDEBOOK</li><br />
			<li>COPY OF THE COMPANY'S AUTHORITY</li><br />
			<li>IF DRIVER IS HAZMAT CERTIFIED, A COPY OF THE HAZARDOUS MATERIALS COMPLIANCE POCKETBOOK</li><br />
		</ol>

		<div class="header1 topMargin1" style="text-align: center;">REQUIRED DOCUMENTS FOR TRUCK APPLICATIONS</div>
		<ol>
			<li>ANNUAL VEHICLE INSPECTION</li><br />
			<li>VEHICLE CONTRACT</li><br />
			<li>W-9</li><br />
			<li>REGISTRATION CARD</li><br />
			<li>PROOF OF BOBTAIL INSURANCE</li><br />
			<li>PROOF OF PHYSICAL DAMAGE INSURANCE (If you have coverage)</li><br />
			<li>PROOF OF OCCUPATIONAL ACCIDENT INSURANCE</li><br />
		</ol>

		<div class="header1 topMargin1" style="text-align: center;">Insurance Certification</div>
		<div class="topMargin1" style="line-height: 2;">
			I, <span class="underline" style="padding-right: 250px;"></span> certify that I want to purchase the following Company
			Insurance:
		</div>
		
		<table class="topMargin1" style="border-collapse: separate; border-spacing: 15px 1px;">
			<tr>
				<td width="46%"></td>
				<td width="10%" align="center">Yes</td>
				<td width="10%" align="center">NO</td>
				<td width="17%" align="center">Quoted Price per Month</td>
				<td width="17%" align="center">Owner Initials</td>
			</tr>
			<tr>
				<td align="right">Bobtail</td>
				<td class="border1"></td>
				<td class="border1"></td>
				<td class="border1">$30.00</td>
				<td class="border1"></td>
			</tr>
			<tr>
				<td align="right">Physical Damage</td>
				<td class="border1"></td>
				<td class="border1"></td>
				<td class="border1">$</td>
				<td class="border1"></td>
			</tr>
			<tr>
				<td align="right">Occupational Accident</td>
				<td class="border1"></td>
				<td class="border1"></td>
				<td class="border1">$109.00</td>
				<td class="border1"></td>
			</tr>
		</table>

		<div class="topMargin1">
			I am aware that Bobtail Insurance is $30.00 per month and Physical damage is determined upon the year and make of my truck.
			The payment for insurance will be deducted from my first settlement check and on the 1<sup>st</sup> week of every month.
		</div>
		<div style="text-align: right; font-weight: bold; font-style: italic;">Contractor's initials<span class="underline" style="padding-right: 100px;"></span></div>

		<div class="topMargin1">
			I am aware Occupational Accident Insurance is $109.00 per month.  The payment for insurance will be deducted from my
			first settlement check and reoccur every 28 days.
		</div>
		<div style="text-align: right; font-weight: bold; font-style: italic;">Contractor's initials<span class="underline" style="padding-right: 100px;"></span></div>

		<div class="topMargin1">
			I am also aware that two months of insurance will be deducted from my first settlement check the first as a deposit and
			the second as the current month's premium.
		</div>
		<div style="text-align: right; font-weight: bold; font-style: italic;">Contractor's initials<span class="underline" style="padding-right: 100px;"></span></div>

		<div class="topMargin1">
			Please fill in the information below if you would like Company Purchased Physical Damage Insurance.
			<span style="font-style: italic;">*Insurance cannot be quoted without the Vehicle Value</span>.
		</div>
		
		<table class="topMargin1">
			<tr>
				<td align="right" style="padding-left: 45px;"><span class="nobr">Lein holder</span> Name:</td>
				<td class="underline" width="100%" colspan="5"></td>
			</tr>
			<tr>
				<td class="topPad2" align="right">Address:</td>
				<td class="underline" colspan="5"></td>				
			</tr>
			<tr>
				<td class="topPad2" align="right">City:</td>
				<td class="underline" width="50%"></td>
				<td>State:</td>
				<td class="underline" width="25%"></td>
				<td>Zip:</td>
				<td class="underline" width="25%"></td>
			</tr>
			<tr>
				<td class="topPad2" align="right">Phone:</td>
				<td class="underline" width="50%"></td>
				<td><span class="nobr">Vehicle Value:</span></td>
				<td class="underline" width="50%" colspan="3">$</td>
			</tr>
		</table>

		<table class="topMargin1">
			<tr>
				<td class="nobr">Agent Signature</td>
				<td class="underline" width="70%"></td>
				<td class="nobr">Date</td>
				<td class="underline" width="30%"></td>
			</tr>
			<tr>
				<td></td>
				<td class="topPad2" align="center">OR</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td class="nobr topPad2">Owner Signature</td>
				<td class="underline" width="70%"></td>
				<td class="nobr">Date</td>
				<td class="underline" width="30%"></td>
			</tr>
		</table>

		<table class="topMargin1">
			<tr>
				<td class="nobr">Occupational Accident Information:</td>
				<td class="underline" width="100%"></td>
			</tr>
			<tr>
				<td class="nobr hint topPad2" align="right">If provided by Owner of Truck</td>
				<td class="underline"></td>
			</tr>
			<tr>
				<td class="nobr hint topPad2" align="right">&nbsp;</td>
				<td class="underline"></td>
			</tr>
		</table>

		<div class="topMargin1">
			Upon cancellation of my policy, I <span class="underline" style="padding-right: 300px;"></span> will notify the Safety
			Department in writing of the cancellation of my policy.
		</div>

		<div class="topMargin1">
			Upon cancellation of Physical Damage, Bobtail or Occupational Accident Insurance coverage will be terminated immediately
			and payments cannot be refunded.
		</div>
		<div style="text-align: right; font-weight: bold; font-style: italic;">Contractor's initials<span class="underline" style="padding-right: 100px;"></span></div>
	</div>
</div>