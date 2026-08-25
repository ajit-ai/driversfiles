<%@ include file="include.jsp" %>
<div id="wrapper">
	<div id="header">
		<div id="header-login">
			<security:authorize access="isAuthenticated()">
				Welcome
				<c:out value="${pageContext.request.userPrincipal.principal.person.firstName}" />
				<c:out value="${pageContext.request.userPrincipal.principal.person.lastName}" />
				- <a href="${pageContext.request.contextPath}/secure/common/accounts/${pageContext.request.userPrincipal.principal.person.uuid}">My Account</a>
				- <a href="${pageContext.request.contextPath}/logout">Sign Out</a>
			</security:authorize>
			<security:authorize access="hasRole('ROLE_ADMIN')">
				<a href="#" onclick="return doUserSearch();"><img src="${pageContext.request.contextPath}/resources/images/search_24x24.png" style="vertical-align: middle; margin-left: 20px;" /></a>
				<form id="switchUserFormId" action="${pageContext.request.contextPath}/secure/admin/enter">
					<input type="text" id="adminUserNameId" name="j_username" style="width: 150px; vertical-align: middle;" />
				</form>
				<button style="vertical-align: middle" onclick="validateUserSwitch()">Enter</button>
			</security:authorize>
			<security:authorize access="hasRole('ROLE_PREVIOUS_ADMINISTRATOR')">
				<form action="${pageContext.request.contextPath}/secure/exit">
					<input type="submit" value="Exit User" style="margin-left: 20px;"/>
				</form>
			</security:authorize>
		</div>
		<div id="header-menu">
			<c:if test="${not empty menu}"><jsp:include page="${menu}"/></c:if>
		</div>
	</div>
	<div id="secondary-menu">
		<c:if test="${not empty secondary_menu}"><jsp:include page="${secondary_menu}"/></c:if>
	</div>
	<div id="content">
		<c:if test="${param.message == 'success'}">
			<div id="success"><div>Update successful</div></div>
		</c:if>
		<c:if test="${not empty content}"><jsp:include page="${content}"/></c:if>
		<br class="clear" />
	</div>
	<div id="footer">
		<c:if test="${not empty footer}"><jsp:include page="${footer}"/></c:if>
	</div>
</div>


<security:authorize access="hasRole('ROLE_ADMIN')">
<script type="text/javascript">

$(document).ready(function() {
	$.getJSON("${pageContext.request.contextPath}/secure/api/persontypes.json",
			function(data){
				var inc = 0;
				var strData = new String(data);
				var items = strData.split(",");
				var typeSelect = document.getElementById("searchUserType");
				for (inc=0; inc < items.length; inc++) {
					if (items[inc] != 'ADMIN') {
						$("#searchUserType").append($('<option>', { value : items[inc] }).text(items[inc]));
					}
				}
			});
});

function validateUserSwitch() {
	
	var frm = document.getElementById("switchUserFormId");
	var username = document.getElementById("adminUserNameId").value;
	var url = "${pageContext.request.contextPath}/secure/api/user/validateswitch.json";
	var params = "username=" + username;
	
	$.post( url, { username: username } , 
		function( rdata ) {
			if ("true" == new String(rdata)) {
				frm.submit();
			} else {
				alert("That is not a valid assumable username.");
				return false;
			}
		});

	return false;
}

function doUserSearch() {

	// Make Enter key submit search in all browsers
	$('input[name^="searchUser"]').keypress(function(event) {
		if (event.which == 13) {
			doAjaxUserSearch();
		}
	});

	// Launch the dialog
	$("#userSearchDlg").dialog({
		title: "User Search",
		minWidth: 550,
		minHeight: 450
	});

	return false;
}

function closeUserSearch() {
	$("#userSearchDlg").dialog('close');
}

function doAjaxUserSearch() {
	// Get the variables together
	var firstName = document.getElementById("searchUserFirstName").value;
	var lastName = document.getElementById("searchUserLastName").value;
	var email = document.getElementById("searchUserEmail").value;
	var type = document.getElementById("searchUserType").value;
	var companyName = document.getElementById("searchUserCompanyName").value;
	var companyNumber = document.getElementById("searchUserCompanyNumber").value;
	var userList = document.getElementById("userList");
	var items = new Array();
	var item = "";
	
	// Call the ajax function
	var url = "${pageContext.request.contextPath}/secure/api/user/search.json";
	$.post( url, { firstName: firstName, lastName: lastName, email: email, type: type, companyName: companyName, companyNumber: companyNumber } , 
		function( rdata ) {
			strData = new String(rdata);
			userList.options.length = 0;

			strData = strData.replace(/[\[][\[]/, "[");
			strData = strData.replace(/[\]][\]]/, "]");
			strData = strData.replace(/[\"]/g, "");
			items = strData.split("],[");
			var i = 0;
			for (i=0; i < items.length; i++) {
				item = items[i];
				item = item.replace(/[\]\[]/g, "");
				parts = item.split(",", 2);
				if (parts.length == 2) {
					$("#userList").append($('<option>', { value : parts[0] }).text(parts[1]));  
				}
			}
		}, "html");

	return false;
}

function doSelectUser() {
	
	var userList = document.getElementById("userList");
	var email = userList.options[userList.selectedIndex].value;
	var field = document.getElementById("adminUserNameId");
	$("#userSearchDlg").dialog("close");
	field.value = email;
	field.select();
}

</script>

<%-- This div is a hidden popup dialog --%>
<div id="userSearchDlg" style="display:none; padding: 20px;">

	<table class="data">
	<tr>
		<td>First Name</td>
		<td><input type="text" id="searchUserFirstName" name="searchUserFirstName" /></td>
		<td>Last Name</td>
		<td><input type="text" id="searchUserLastName" name="searchUserLastName" /></td>
	</tr>
	<tr>
		<td>Email</td>
		<td><input type="text" id="searchUserEmail" name="searchUserEmail" /></td>
		<td>User Type</td>
		<td>
			<select id="searchUserType" name="type">
			</select>
		</td>
	</tr>
	<tr>
		<td>Company Name</td>
		<td><input type="text" id="searchUserCompanyName" name="searchUserCompanyName" /></td>
		<td>Company Number</td>
		<td><input type="text" id="searchUserCompanyNumber" name="searchUserCompanyNumber" /></td>
	</tr>
	<tr>
		<td>
			<button onclick='doAjaxUserSearch()'>Search</button>
		</td>
	</tr>
	</table>
	
	<br />
	<hr />
	
	<table>
	<tr>
		<td>Results:</td>
	</tr>
	<tr>
		<td>
			<select id="userList" size="6" style="width: 470px" ondblclick="doSelectUser()" ></select>
		</td>
	</tr>
	<tr>
		<td>
			<button id="selUserBtn" onclick="doSelectUser()">Ok</button>&nbsp;&nbsp;
			<button id="cancelBtn" onclick="closeUserSearch()">Cancel</button>
		</td>
	</tr>
	</table>	
</div>
</security:authorize>