function confirmDelete() {
	return confirm("Are you sure you want to delete this?");
}

$(document).ready(function() {
	$("table.data tbody tr:even").addClass("even-row");
	$("table.data tbody tr:odd").addClass("odd-row");
	$("table.data tbody tr:even").hover(
		function() {
			$(this).removeClass("even-row").addClass("highlight");
		},
		function() {
			$(this).removeClass("highlight").addClass("even-row");
		});
	$("table.data tbody tr:odd").hover(
		function() {
			$(this).removeClass("odd-row").addClass("highlight");
		},
		function() {
			$(this).removeClass("highlight").addClass("odd-row");
		});

	$(".b0rk").click(function() {
		alert("The requested resource is in the process of being developed.");
		return false;
	});
	$(".confirmDelete").click(function() {
		return confirmDelete();
	});

	$("#menu-" + ACTIVE_MENU).addClass('active');
	$("#left-menu-" + ACTIVE_LEFT_MENU).addClass('active');

	$.ajaxSetup({
		cache: false
	});
});