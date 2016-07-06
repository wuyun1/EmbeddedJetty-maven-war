<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<html>
<body>
<h2>Hello World!</h2>

<h1> 
	<%= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss  SSS").format(new Date()) %>
</h1>
</body>
</html>
