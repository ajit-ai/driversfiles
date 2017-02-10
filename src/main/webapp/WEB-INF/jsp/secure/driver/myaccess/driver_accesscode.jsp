<%@ include file="../../../include.jsp" %>


<script type="text/javascript">
$(document).ready(function() {
	$("#btnExpire").click(function() {
		window.location.href = '${pageContext.request.contextPath}/secure/driver/accesscode/${fn:toUpperCase(driver.accessCode)}/expire';
	});
	$("#bntPrint").click(function() {
		window.open('${pageContext.request.contextPath}/secure/driver/accesscode/print');
		//window.location.href = '${pageContext.request.contextPath}/secure/driver/accesscode/print';
	});
});

</script>

<h1>My Access Code</h1>
<br />

<p>Your current access code is:</p>

Code: ${fn:toUpperCase(driver.accessCode)}<br />
Expires on: 
	<c:if test="${!empty expireDate}">
		<fmt:formatDate value="${expireDate}" pattern="MMM dd, yyyy" />
	</c:if>
	<c:if test="${empty expireDate}">
		<font class="error">Expired</font>
	</c:if>
	<br />
<button id="btnExpire">Expire Now</button>
&nbsp;
<button id="bntPrint" onclick="doPrintCode()">Print Code</button>
<br />
<br />

<p>You may allow non-driverfiles members access to <br />
your driver application by giving them your <br />
generated access code and having them go to:</p>

<p>
<a href="${appUrl}" target="_blank">${appUrl}</a><br />
</p>

<br />
<a id="abtnView" href="${pageContext.request.contextPath}/secure/application/${driver.person.uuid}" target="_myapp">View My Application</a>

<script>
$(function() {
	$("#abtnView").button();
});
</script>

