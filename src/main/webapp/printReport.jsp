<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<title>Print Report Page</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f2f2f2;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
}

.container {
	background: white;
	padding: 40px;
	border-radius: 12px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	text-align: center;
}

h2 {
	margin-bottom: 30px;
	color: #333;
}

.print-btn {
	padding: 12px 24px;
	background-color: #007bff;
	border: none;
	border-radius: 6px;
	color: white;
	font-size: 16px;
	cursor: pointer;
	transition: background-color 0.3s ease;
}

.print-btn:hover {
	background-color: #0056b3;
}
</style>
</head>
<body>
	<div class="container">

		<h2>Generate and Print Report</h2>
		<button class="print-btn"
			onclick="window.open('UserServlet', '_blank')">Print Report</button>

	</div>
</body>
</html>
