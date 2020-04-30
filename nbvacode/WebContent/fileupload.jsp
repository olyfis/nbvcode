<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style><%@include file="includes/css/header.css"%></style>
<style><%@include file="includes/css/menu.css"%></style>
 <link rel="stylesheet" href="includes/css/calendar.css" />
    <script type="text/javascript" src="includes/scripts/pureJSCalendar.js"></script>
</head>
<body>
<h5><b>Note: <font color="red">The filename must not contain any spaces.</font></b></h5>
	<form action="fileupload" method="post" enctype="multipart/form-data" >
		<table border="1" class="tablesorter">
			<tr bgcolor="#5DADE2" style="font-family: sans-serif; color: white;">
				<th class="a">Select Template File</th>
				<th class="a">Action</th>
			</tr>
			<tr>
				<td bgcolor="#AEB6BF"><input type="file" name="file" size="50" /></td>
				<td bgcolor="#AEB6BF"><input type="submit" value="Process File" /></td>
			</tr>
			
			  <td width="20" valign="bottom"> <b>Enter Contract Number:</b> </td> 
  <td width="20" valign="bottom">  
     <%   out.println("<input name=\"startDate\" id=\"date2\" type=\"text\" value=\"Click for Calendar\" onclick=\"pureJSCalendar.open('yyyy-MM-dd', 20, 30, 7, '2017-1-1', '2025-12-31', 'date2', 20)\"   />" );
     %>
    <!--  <CENTER>  <input name="id" type="text"  value="101-0009442-019" /> </CENTER>  -->
      <CENTER>  <input name="id" type="text"   /> </CENTER>
     
  </td>
		</table>
		<input type="hidden" name="hiddenField" value="dummy">
	</form>
	<BR>
</body>
</html>