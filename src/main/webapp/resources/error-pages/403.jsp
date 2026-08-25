<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" isELIgnored="false"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<title>Access Denied (403) :: Drivers Files</title>
	<link type="text/css" href="$\{pageContext.request.contextPath\}/resources/vendor/bootstrap.min.css" rel="stylesheet" />
	<style>
		body { background: #f4f6f8; font-family: 'Segoe UI', system-ui, sans-serif; }
		.err-card { max-width: 560px; margin: 12vh auto; background: #fff; border-radius: 14px;
			box-shadow: 0 10px 30px rgba(0,0,0,.12); padding: 48px; text-align: center; }
		.err-code { font-size: 4rem; font-weight: 800; color: #f18700; line-height: 1; }
		h1 { font-size: 1.3rem; color: #2c3e50; margin-top: 12px; }
		p { color: #6c757d; }
	</style>
</head>
<body>
	<div class="err-card">
		<div class="err-code">403</div>
		<h1>Access Denied (403)</h1>
		<p>You do not have permission to access this resource.</p>
		<a class="btn btn-warning fw-semibold px-4 mt-2" href="$\{pageContext.request.contextPath\}/" style="background:#f18700;border-color:#f18700;color:#fff;">Back to Home</a>
	</div>
</body>
</html>