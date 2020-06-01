<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.olympus.nbva.assets.AssetData"%>
<%@ page import="com.olympus.nbva.contracts.ContractData"%>

<%@ page import="org.apache.commons.lang3.tuple.*"%>
<%@ page import="com.olympus.olyutil.*"%>
<%@ page import="com.olympus.nbva.DateUtil"%>
<%@ page import="java.lang.reflect.*"%>

<% 
	String title =  "Olympus NBVA Asset List Report"; 
out.println("<H4>  TEST</H4>");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title><%=title%></title>
<!--  	101-0015003-034 -->
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<!--   <script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="includes/js/tableFilter.js"></script>
-->
<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
<style><%@include file="includes/css/header.css"%></style>
<style><%@include file="includes/css/flex.css"%></style>


<link rel="stylesheet" href="includes/css/calendar.css" />

<!-- ********************************************************************************************************************************************************* -->
</body>
</html>