<%@ include file="../../../include.jsp" %>

<script type="text/javascript">
$(document).ready(function() {
	window.print();
});

</script>


<h1>My Application</h1>
<br />

<p>This is an invitation to view the application of <br />
<Strong>${driver.person.firstName} ${driver.person.lastName}</Strong>
</p>

<p>To view this application please type the following address<br />
into a web browser and use the code given below. Please be aware that<br />
this invitation has an expiration date.</p>

Address:<br />
<a href="${appUrl}" target="_blank">${appUrl}</a><br />
<br />

Code: ${fn:toUpperCase(driver.accessCode)}<br />
Expires on: 
	<c:if test="${!empty expireDate}">
		<fmt:formatDate value="${expireDate}" pattern="MMM dd, yyyy" />
	</c:if>
	<c:if test="${empty expireDate}">
		<font class="error">Expired</font>
	</c:if>
	<br />
<br />
<br />

