<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.olympus.nbva.assets.AssetData"%>
<%@ page import="com.olympus.nbva.contracts.ContractData"%>
<%@ page import="org.apache.commons.lang3.tuple.*"%>
<%@ page import="com.olympus.olyutil.*"%>
<%@ page import="java.lang.reflect.*"%>

<% 
	String title =  "Olympus NBVA File Upload"; 
	 
	ArrayList<String> tokens = new ArrayList<String>();
	 
	HashMap<String, String> map = new HashMap<String, String>();
	 
	 
	//DecimalFormat df = new DecimalFormat("$###,##0.00");
	HashMap<String, String> paramMap =  (HashMap<String, String>) session.getAttribute("paramMap");
     

%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=title%></title>
</head>
<body>
    <center>
        <h5>${id}</h5>
        <h5>${date2}</h5>
         <h5>${invoice}</h5>
        <h5>${message}</h5>
    </center>
    
    <% 
    
    out.println("Test");
    out.println("ID=" + paramMap.get("id"));
    out.println("Date=" + paramMap.get("eDate"));
    out.println("Invoice=" + paramMap.get("invoice"));
    %>
    
    
    
</body>
</html>