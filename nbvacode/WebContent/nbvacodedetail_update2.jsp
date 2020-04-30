 
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
<%@ page import="java.lang.reflect.*"%>

<% 
	String title =  "Olympus NBVA Asset List Report"; 
	 
	ArrayList<String> tokens = new ArrayList<String>();
	ArrayList<String> kitArr = new ArrayList<String>();
	String formUrl =  (String) session.getAttribute("formUrl");
	double sumTotal = (double) session.getAttribute("sumTotal");
 	String kitFileName = "C:\\Java_Dev\\props\\kitdata\\kitdata.csv";
	HashMap<String, String> map = new HashMap<String, String>();
	int mthRem = (int) session.getAttribute("mthRem");
	String termPlusSpan =  (String) session.getAttribute("termPlusSpan");
	//DecimalFormat df = new DecimalFormat("$###,##0.00");
	String opt =  (String) session.getAttribute("opt");
	HashMap<String, String> CodeMap = new HashMap<String, String>();
	CodeMap = (HashMap<String, String> )session.getAttribute("codeMapRtn");
	String useCodeData = (String) session.getAttribute("useCodeData");
	
	Set<String> keys = CodeMap.keySet();  //get all keys
	for(String key: keys) {
	
	    System.out.println("**----** Key=" + key + "-- Value=" + CodeMap.get(key) + "--");
	}
	
	
	
	if (useCodeData.equals("true")) {
		System.out.println("^^^^^^^ Using code data-- CM_ID="  + CodeMap.get("asset"));	
	} else {
		System.out.println("^^^^^^^ Not using code data-- CM_ID="  + CodeMap.get("asset") +"-- UCD=" +  useCodeData);	
	}
	
	
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title><%=title%></title>
<!--  	101-0015003-034 -->
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="includes/js/tableFilter.js"></script>
<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
<style><%@include file="includes/css/header.css"%></style>
<style><%@include file="includes/css/flex.css"%></style>


<link rel="stylesheet" href="includes/css/calendar.css" />

<!-- ********************************************************************************************************************************************************* -->


<script>

$(function() {

  // call the tablesorter plugin
  $("table").tablesorter({
    theme: 'blue',
    // initialize zebra striping of the table
    widgets: ["zebra"],
    // change the default striping class names
    // updated in v2.1 to use widgetOptions.zebra = ["even", "odd"]
    // widgetZebra: { css: [ "normal-row", "alt-row" ] } still works
    widgetOptions : {
      zebra : [ "normal-row", "alt-row" ]
    }
  });

});	
			
    </script>
    
   <script>
  function openWin(myID) {
  
  
   myID2 = document.getElementById(b_app).value;

  alert("ID" + myID2);
  //window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=" + myID2);
	}
	
	
	var call = function(id){
		var myID = document.getElementById(id).value;
		//alert("****** myID=" + myID + " ID=" + id);		 
			//window.open("http://cvyhj3a27:8181/nbvacode/kitdata?key=" + myID);
		window.open("http://localhost:8181/nbvacode/kitdata?key=" + myID);
				
				
	}



	var getExcel = function(urlValue){
		var formUrl = document.getElementById(urlValue).value;
		//alert("SD=" + startDate + "****** formUrl=" + formUrl + " \n***** urlValue=" + urlValue);
		//alert("in Quote" + myID + " --- id=" +id);
		window.open( formUrl, 'popUpWindow','height=500,width=800,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes' );
	
	}
	
	
	
</script> 
    
</head>
<body>

<%!  

String formUrl = null;
/*************************************************************************************************************************************************************/
public ArrayList<String> readHeader(String filePath) throws IOException {
	
	ArrayList<String> strArr = new ArrayList<String>();
	String header = null;
	BufferedReader reader = null;
	StringBuilder sb = null;
	String line = null;
	try {
	 	reader = new BufferedReader(new FileReader(filePath));
    	 sb = new StringBuilder();
    
	} catch (FileNotFoundException fex) {
		fex.printStackTrace();	
	}
	try { 
	    while((line = reader.readLine())!= null){
	    	strArr.add(line);
	    }	   
		reader.close();
	
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}
	
	return strArr;	
}
/*************************************************************************************************************************************************************/
public String  buildHeader( JspWriter out2, ArrayList<String> dataArr   ) throws IOException {
	
	String header = "";
	String style = "b3";
	if (dataArr.size() > 0) {
		for (int k = 0; k < dataArr.size(); k++) {
			
			if (k == 1) {
				style = "b3a";
			} else {
				style = "b3";
			}
			//header += "<th class=\"b3\" >" + dataArr.get(k) + " </th>";
			header += "<th class=\" " + style + "  \" >" + dataArr.get(k) + " </th>";
			
		}
	}
	return header;
	
}
/*************************************************************************************************************************************************************/
public String  buildCells( JspWriter out, ArrayList<String> dataArr  ) throws IOException {
	String cells = "";
	String xDataItem = null;
	String color1 = "plum";
	String style1 = "font-family: sans-serif; color: white;";
	String rowEven = "#D7DBDD";
	String rowOdd = "AEB6BF";
	String excel = null;
	String rowColor = null;
	
	//t.println("<tr>");
	//cells = "<tr>";	
	if (dataArr.size() > 0) {
		for (int k = 0; k < dataArr.size(); k++) {
			rowColor = (k % 2 == 0) ? rowEven : rowOdd;
			cells +="<tr bgcolor=" + rowColor + ">";
			xDataItem = dataArr.get(k);
			String token_list[] = xDataItem.split(";");
			for (int x = 0; x < token_list.length; x++) {
				cells += "<td class=\"odd\">" + token_list[x] + "</td>";
			}
			cells += "</tr >";
			
			//cells += "<td class=\"a\" >" + dataArr.get(k) + " </td>";
		}
	}
	
	
	return cells;
}
/*************************************************************************************************************************************************************/
public void  buildCellsTotals( JspWriter out, ContractData contract, String formUrl, double sumTotal  ) throws IOException {
	DecimalFormat df = new DecimalFormat("$###,##0.00");
	String cells = "";
	String xDataItem = null;
	String color1 = "plum";
	String style1 = "font-family: sans-serif; color: white;";
	String rowEven = "#D7DBDD";
	String rowOdd = "AEB6BF";
	String excel = null;
	String rowColor = null;
	String style = "b3c";
	String hold = "TBD";
	out.println("<tr>"); 
	
	
	//String sumTotal_df = df.format(sumTotal);
	String sumTotal_df = Olyutil.decimalfmt(sumTotal, "$###,##0.00");

	out.println("<th class=\" " + style + "  \" >Contract Total</th>");
	//out.println( "<td class=\"a\">" + contract.getContractID() + "</td></tr>");
	out.println( "<td class=\"a\">" + sumTotal_df + "</td></tr>");
	out.println("<tr>"); 
	out.println("<th class=\" " + style + "  \" >Contract Residual</th>");
	 out.println( "<td class=\"a\">" + hold + "</td></tr>");
	
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Contract Tax</th>");
	 out.println( "<td  width=\"70%\"  >" + hold + "</td></tr>");
	
	out.println( "  </td></tr>");

}
/*************************************************************************************************************************************************************/
public void  buildCellsContract( JspWriter out, ContractData contract, String formUrl, int mthRem  ) throws IOException {
	//DecimalFormat df = new DecimalFormat("$###,##0.00");
	
	String cells = "";
	String xDataItem = null;
	String color1 = "plum";
	String style1 = "font-family: sans-serif; color: white;";
	String rowEven = "#D7DBDD";
	String rowOdd = "AEB6BF";
	String excel = null;
	String rowColor = null;
	String style = "b3c";
 	
	out.println("<tr>"); 
	out.println("<th class=\" " + style + "  \" >Contract Number</th>");
	out.println( "<td class=\"a\">" + contract.getContractID() + "</td></tr>");
	 
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Customer Name</th>");
	out.println( "<td  width=\"70%\"  >" + contract.getCustomerName() + "</td></tr>");
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Customer ID</th>");
	out.println( "<td  width=\"70%\"  >" + contract.getCustomerID() + "</td></tr>");
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Effective Date</th>");
	out.println( "<td class=\"a\">" + contract.getEffectiveDate() + "</td></tr>");
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Commencement Date</th>");
	out.println( "<td class=\"a\">" + contract.getCommenceDate() + "</td></tr>");
	
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Term Date</th>");
	out.println( "<td class=\"a\">" + contract.getTermDate() + "</td></tr>");
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Final Invoice Due Date</th>");
	out.println( "<td class=\"a\">" + contract.getFinalInvDueDate() + "</td></tr>");
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Term Plus Nine Date</th>");
	out.println( "<td class=\"a\">" + contract.getTermPlusSpan() + "</td></tr>");
	
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Term</th>");
	out.println( "<td class=\"a\">" + contract.getTerm() + "</td></tr>");
	
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Months Remaining</th>");
	out.println( "<td class=\"a\">" + mthRem  + "</td></tr>");
	
	//double equipPayment = contract.getEquipPayment();
	//String equipPayment_df = df.format(equipPayment);
	String equipPayment_df = Olyutil.decimalfmt(contract.getEquipPayment(), "$###,##0.00");
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Equipment Payment</th>");
	out.println( "<td class=\"a\">" + equipPayment_df + "</td></tr>");
	/*
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Service Payment</th>");	
	out.println( "<td class=\"a\">" + contract.getServicePayment() + "</td></tr>");
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Contract Buyout</th>");
	out.println( "<td class=\"a\">" +    Olyutil.decimalfmt(contract.getBuyOut(), "$###,##0.00")  + "</td></tr>");
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Contract Rollover</th>");
	out.println( "<td class=\"a\">" + Olyutil.decimalfmt(contract.getRollTotal(), "$###,##0.00")   + "</td></tr>");
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Contract Returns</th>");
	out.println( "<td class=\"a\">" + Olyutil.decimalfmt(contract.getRtnTotal(), "$###,##0.00")  + "</td></tr>");
	*/
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Invoice Code</th>");
	out.println( "<td class=\"a\">"  + contract.getInvoiceCode() + "</td></tr>");
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Status Code</th>");
	out.println( "<td class=\"a\">"  + contract.getContractStatus().replaceAll("null", "") + "</td></tr>");
	
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Purchase Option</th>");
	out.println( "<td class=\"a\">"  + contract.getPurOption() + "</td></tr>");
	
	
	
 /*
	out.println("<tr>");
	out.println("<th class=\" " + style + "  \" >Save as Excel File <br> May take a while to build file.</th>");	
	out.println( "<td class=\"a\"> ");
	out.println(" <form name=\"excelForm\"    enctype=\"multipart/form-data\"   method=\"get\" action=" +   formUrl  +  " >    ");
 	out.println("<input type=\"submit\" value=\"Save Excel File\" class=\"btn\" /> ");
	*/
	out.println("</table>");
	//out.println("</form> </td></tr></table>");
	
	out.println( "  </td></tr>");
	//System.out.println("*** boDate=" + contract.getBuyoutDate() + "--");
}

/*************************************************************************************************************************************************************/

public static void displayObj(Object obj) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	
	for (Method m : obj.getClass().getMethods())
	    if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
	      final Object r = m.invoke(obj);
	      // do your thing with r
	      //System.out.println("Method:" + m.getName() + " : " + r.toString() );
	    }

}
/*************************************************************************************************************************************************************/

/*************************************************************************************************************************************************************/
// Invoke: strArr = GetKitData.getKitData(fileName);
	public static ArrayList<String> getKitData(String fileName) throws IOException {

		ArrayList<String> strArr = new ArrayList<String>();
		strArr = Olyutil.readInputFile(fileName);
		return (strArr);
	}
/*************************************************************************************************************************************************************/

	public static HashMap<String, String> getKitHash(ArrayList<String> kitDataArr  ) throws IOException {
		HashMap<String, String> map = new HashMap<String, String>();
		String key = "";
		
		for (String str : kitDataArr) {
			String[] items = str.split(",");
			key = items[0];
			map.put(key, "true");
		}
		return (map);
	}
	/*************************************************************************************************************************************************************/
public HashMap<String, String> getTotals(List<AssetData> assetList) {
	HashMap<String, String> map = new HashMap<String, String>();
	int rtnArrSZ = assetList.size();
	double resTotal = 0.00;
	double equipTotal = 0.00;
	double rentalTotal = 0.00;
	for (int j = 0; j < rtnArrSZ; j++ ) {
		AssetData asset = new AssetData();
		asset = assetList.get(j);
		 resTotal += asset.getResidAmt();
		 equipTotal += asset.getEquipCost();
		 rentalTotal += asset.getaRentalAmt();
	}	
	String resTotal_df = Olyutil.decimalfmt(resTotal, "$###,##0.00");
	String equipTotal_df = Olyutil.decimalfmt(equipTotal, "$###,##0.00");
	String rentalTotal_df = Olyutil.decimalfmt(rentalTotal, "$###,##0.00");
	map.put("resTotal", resTotal_df);

	map.put("equipTotal", equipTotal_df);
	map.put("rentalTotal", rentalTotal_df);
	//System.out.println("*** ResTotal=" + resTotal_df + "-- EquipTotal=" + equipTotal_df + "-- RentalAmt=" + rentalTotal_df);
	return (map);
}
	
	
	
/*************************************************************************************************************************************************************/
public String  buildCellsAsset( HashMap<String, String> hm, JspWriter out,  List<Pair<ContractData, List<AssetData> >> rtnPair, String opt  ) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	HashMap<String, String> rtnMap = new HashMap<String, String>();
	DecimalFormat df = new DecimalFormat("$###,##0.00");
	String cells = "";
	String xDataItem = null;
	String color1 = "plum";
	String style1 = "font-family: sans-serif; color: white;";
	String rowEven = "#D7DBDD";
	String rowOdd = "AEB6BF";
	String excel = null;
	String rowColor = null;
	String model = "";
	String[] dispArr = null;
	int n = 0;
	//String formUrlValue = "/nbvadetail_flex.jsp" ;
	String formUrlValue = "/nbvadetail_update.jsp" ;
	int listArrSZ = rtnPair.size();
	if (listArrSZ > 0) {
		//cells +="  <form name=\"getAsset\" enctype=\"multipart/form-data\" method=\"POST\" action=\"/nbvacode/nbvamod\" > ";
		cells +="  <form name=\"getAsset\"  method=\"POST\" action=\"/nbvacode/nbvamod\" > ";
		int rtnArrSZ = rtnPair.get(0).getRight().size();
		List<AssetData> assetList = new ArrayList<AssetData>();
		assetList	= rtnPair.get(0).getRight();		 
				 
		rtnMap = getTotals( assetList);
				
		for (int i = 0; i < listArrSZ; i++ ) {
			//int rtnArrSZ = rtnPair.get(i).getRight().size();
			//List<AssetData> assetList = new ArrayList<AssetData>();
			//assetList	= rtnPair.get(i).getRight();
			//out.println("<h5> listArrSZ =" + listArrSZ + " -- rtnArrSZ=" +  rtnArrSZ + "--</h5>");
			 
			cells +="<tr bgcolor=" + rowOdd + ">";
			cells +="<TD>" + "Totals" + "</td>";
			cells +="<TD colspan=\"10\">" + "" + "</td>";
			cells +="<TD>" + rtnMap.get("resTotal") + "</td>";
			cells +="<TD>" + rtnMap.get("equipTotal") + "</td>";
			cells +="<TD>" + rtnMap.get("rentalTotal") + "</td>";
			//cells +="<TD >" + "" + "</td>";
			cells +="  </tr>";
				
			
			for (int j = 0; j < rtnArrSZ; j++ ) {
				AssetData asset = new AssetData();
				asset = assetList.get(j);
				//System.out.println("*** AssetReturn: EquipmentType=" + asset.getEquipType() + "--");
				//System.out.println("*** AssetReturn: CustomerID=" + asset.getCustomerID() + "--");
				model = asset.getModel();
				//System.out.println("*** AssetReturn: Model ->" + model + "--");
				
				rowColor = (j % 2 == 0) ? rowEven : rowOdd;
				cells +="<tr bgcolor=" + rowColor + ">";
				cells +="<TD>" + asset.getAssetId() + "</td> ";
				
				
				String code = "";
				if (asset.getDispCode() == 0) {
					code = "Rollover";
				} else if (asset.getDispCode() == 1 ) { 
					code = "Buyout";
				} else if (asset.getDispCode() == 2 ) { 
					code = "Return";
				}
			
				 //cells +="<TD> 	<select  name=\"dispCodeArr_"  + n++  + "\" > "; 
				// cells +="<TD> 	<select  name=\"dispCodeArr_"  + n  + "\"   id=\"dispCodeArr_"  + n  + "\"             > "; 
				
				 cells +="<TD> 	<select  name=\"dispCodeArr_"   + Long.toString(asset.getAssetId() )  + "\"  > "; 
				  n++;
				cells +="<option value=\"" + asset.getDispCode() + "\" selected    > " +code+    " </option>  ";
				
				
				cells +="<option value=\"0\">Rollover</option>  ";
				cells +="<option value=\"1\">Buyout</option>  ";
				cells +="<option value=\"2\">Return</option>  ";
				cells +=" </select> </td> ";
				
				
				cells +="<TD>" + asset.getEquipType() + "</td> ";
				cells +="<TD>" + asset.getEquipDesc() + "</td> ";
				
				cells +="<TD>" + asset.getModel() + "</td> ";
	      
			
			cells +="<TD>" + asset.getSerNum().replaceAll("null", "") + "</td> ";
			cells +="<TD>" + asset.getQty() + "</td> ";
			cells +="<TD>" + asset.getEquipAddr1() + "</td> ";
			cells +="<TD>" + asset.getEquipCity() + "</td> ";
			cells +="<TD>" + asset.getEquipState() + "</td> ";
			cells +="<TD>" + asset.getEquipZip() + "</td>  ";
	
			
			String residAmt_df = Olyutil.decimalfmt(asset.getResidAmt(), "$###,##0.00");
			//System.out.println("*** ResidualAmt=" + asset.getResidAmt()   + "FMT:" + residAmt_df );
			cells +="<TD>" + residAmt_df + "</td>  ";
			
	 
			String equipCost_df = Olyutil.decimalfmt(asset.getEquipCost(), "$###,##0.00");
			
			
			cells +="<TD>" + equipCost_df  + "</td>  ";
 
			String rentalAmt_df  = Olyutil.decimalfmt(asset.getaRentalAmt(), "$###,##0.00");
			cells +="<TD>" + rentalAmt_df+ "</td>  ";
		 
				
				
			}
		}
		cells +="<TR><TD COLSPAN=\"17\" align=\"center\">           </td></tr>  ";
		cells +="<input type=\"hidden\" id=\"num\" name=\"num\"  value=" +  n + "> ";
		cells +="<TR><TD COLSPAN=\"17\" align=\"center\">  <input type=\"Submit\" name=\" dispCode2\"   value=\" Update\"   >      </td></tr>";
		cells +=" </form>";
		 
	}
	return(cells);
}

/*************************************************************************************************************************************************************/

%>

<div style="padding-left:20px">
<%@include  file="includes/header.html" %>

<h3><%=title%></h3>
<h5>This page will provide an on-demand NBVA Asset List Report.</h5>
<!-- <BR> <font color="red"> Note: Some queries take more time to run. Please be patient.</font>   -->

<%
	String filePath = "C:\\Java_Dev\\props\\headers\\NBVA_ContractHrd.txt";
	ArrayList<String> headerArr = readHeader(filePath);
  
	String filePath2 = "C:\\Java_Dev\\props\\headers\\NBVA_AssetHrdCode.txt";
	ArrayList<String> headerArr2 = readHeader(filePath2);
	kitArr = getKitData(kitFileName);
	map = getKitHash(kitArr);
	
	//ArrayList<String> list2 = new ArrayList<String>();
	 List<Pair<ContractData, List<AssetData> >> list = (List<Pair<ContractData, List<AssetData> >>) session.getAttribute("rtnPair");
	 request.getSession().setAttribute("rtnPairList", list);
	 request.getSession().setAttribute("JB", "JB_TEST");
	 
	 int lsz = list.size();
	 int rtnArrSZ = 0;
	 ContractData contractData = new ContractData();
	 AssetData assetData = new AssetData();		
	 		

	 if (lsz > 0) {	
	 	
	 	contractData = list.get(0).getLeft();
	 	rtnArrSZ = list.get(0).getRight().size();
	 	//out.println("<h5> List Size:" + lsz + " -- rtnArrSZ=" +  rtnArrSZ + "--</h5>");

	 	if (rtnArrSZ > 0) {
	 		
	 		list.get(0).getRight().get(0);
	 		
	 	}
	 	
	 	
	 	
	 }
	
	
	
	
	 
	//list2.add("xx");
	
	//out.println("listSize=" + list.size());
	if (list.size() > 0) {
		/**********************************************************************************************************************************************************/
		// Output Table 
	%>	
	<div class="flex-container">
	  <div>Contract Data
	  <%
		/* Display Contract Data */
		out.println("<table class=\"tablesorter\" border=\"1\"  width=\"600\"> <thead> <tr>");
		out.println("<tbody id=\"report\">");
		  buildCellsContract(out, contractData, formUrl, mthRem); // build data cells from file
		//out.println(cells);
		out.println("</tbody></table><BR>"); // Close Table

	  %> 
	  </div>
	  <div>Contract Totals 
	   <%
		/* Display Contract Totals */
		out.println("<table class=\"tablesorter\" border=\"1\"  width=\"600\"> <thead> <tr>");
	 
		out.println("<tbody id=\"report\">");
		  buildCellsTotals(out, contractData, formUrl, sumTotal); // build data cells from file
		//out.println(cells);
		out.println("</tbody></table><BR>"); // Close Table

	  %> 
 
	</div>
	
	
	<!--   <table border="0"  ><tr> -->
	
	<!--  
	<td><input id="search" type="text" placeholder="Enter Text to Filter..."></td>
-->
<td>
<!-- 
 <form name="excelForm" enctype="multipart/form-data" method="get" action=" <%=formUrl%> " \>
 --> 
  	
  
  
<%
/*
out.println(" <form name=\"excelForm\"    enctype=\"multipart/form-data\"   method=\"get\" action=" +   formUrl  +  " >    ");
out.println("<input type=\"submit\" value=\"Save Excel File\" class=\"btn\" /> ");
out.println("</form> </td></tr></table>");
*/


		/* Display Contract Data */
		/*
		out.println("<table class=\"tablesorter\" border=\"1\"  width=\"600\"> <thead> <tr>");
		//String header = buildHeader(out, headerArr); // build header from file
		//out.println(header);
		//out.println("</tr></thead>");
		out.println("<tbody id=\"report\">");
		  buildCellsContract(out, contractData, formUrl); // build data cells from file
		//out.println(cells);
		out.println("</tbody></table><BR>"); // Close Table
		*/
		
		
		
		/**********************************************************************************************************************************************************/
	/* Display Asset Data  */
	
	out.println("<BR>");
	out.println("<BR><div>Asset Data" );
	out.println("<table class=\"tablesorter\" border=\"1\"> <thead> <tr>");
		String header2 = buildHeader(out, headerArr2); // build header from file
		out.println(header2);
		out.println("</tr></thead>");
		out.println("<tbody id=\"report\">");
		out.println(buildCellsAsset(map, out, list, opt)); // build data cells from file
		
		out.println("</tbody></table><BR>"); // Close Table
	
		out.println("</div>");
	
		/**********************************************************************************************************************************************************/

	
	} else {
		out.println("No Asset data to display." + "<br>");

	}
	 
	

%>

</body>
</html>