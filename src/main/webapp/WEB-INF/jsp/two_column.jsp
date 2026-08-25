<%@include file="include.jsp"%>

<div class="two-col-wrapper">
	<div class="two-col-right">
		<c:if test="${not empty right}"><jsp:include page="${right}"/></c:if>
	</div>
</div>

<div class="two-col-left">
	<c:if test="${not empty left}"><jsp:include page="${left}"/></c:if>
</div>