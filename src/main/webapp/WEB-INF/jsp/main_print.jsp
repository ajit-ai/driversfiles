<%@ include file="include.jsp" %>
<div id="wrapper">
	<div id="header">
		<div id="header-login">
		</div>
	</div>
	<div id="content">
		<c:if test="${not empty content}"><jsp:include page="${content}"/></c:if>
		<br class="clear" />
	</div>
	<div id="footer">
		<c:if test="${not empty footer}"><jsp:include page="${footer}"/></c:if>
	</div>
</div>

