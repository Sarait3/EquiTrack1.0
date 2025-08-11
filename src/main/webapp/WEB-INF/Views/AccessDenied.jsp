<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%-- Access Denied page: shown when a user lacks permission for a route. --%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Access Denied</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<!-- Header with back navigation -->
	<div class="header">
		<div class="header-content">
			<a href="ListView" class="back-btn">&larr; Back to List</a>
			<h1>Access Denied</h1>
		</div>
	</div>

	<!-- Main content -->
	<div class="container">
		<div class="empty-state">
			<h3 style="color: red;">You do not have permission to view this
				page.</h3>
		</div>
	</div>
</body>
</html>
