<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.net.InetAddress"%>
 

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Results</title>
</head>
<body>
<h4>Results Page</h4>




<%
	String at = request.getParameter("actiontype");
	String at2 = request.getParameter("getID");
    String hostname = InetAddress.getLocalHost().getHostName();

	//out.println("atype=" + at);
	//System.out.println("***^^^*** AT=" + at + "--");
	if (at != null) {

		if (at.equals("100")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/nbvacode/nbvagetasset.jsp";
			response.sendRedirect(redirectURL);

		} else if (at.equals("125")) {
		
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/nbvacode/nbva.jsp";
			response.sendRedirect(redirectURL);
		
		
		
		
	} else if (at.equals("150")) {
		
		String hostname2 = "cvyhj3a27";
		String redirectURL = "http://" + hostname2  + ":8181/nbvacode/createdispfile.jsp";
		response.sendRedirect(redirectURL);
		
		
		
		
	} else if (at.equals("425")) {
			String hostname2 = "cvyhj3a27";
			String id = request.getParameter("id");
			//out.println("ID:" + id + "--");
			String redirectURL = "http://" + hostname2  + ":8181/nbvacode/nbva?id=" + id;
			response.sendRedirect(redirectURL);
		} else if (at.equals("150")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/nbvacode/nbvagetasset.jsp";
			response.sendRedirect(redirectURL);
		
		
		
		
	} else if (at.equals("375")) {
			String id = request.getParameter("id");
			//out.println("ID:" + id + "--");
			String redirectURL = "http://" + hostname  + ":8181/nbvacode/nbva?id=" + id;
			response.sendRedirect(redirectURL);

		} 
		 else if (at.equals("210")   ) {
			//String id = request.getParameter("id");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String redirectURL = "http://" + hostname  + ":8181/reports/orderrel?startDate=" + startDate + "&endDate=" + endDate ;
			response.sendRedirect(redirectURL);
		}
		
		
		
		
	}
%>
 
 

</body>
</html>