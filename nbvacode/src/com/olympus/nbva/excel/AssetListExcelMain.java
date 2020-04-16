package com.olympus.nbva.excel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.NodeList;
import org.apache.poi.xssf.usermodel.*;
import com.olympus.nbva.assets.AssetData;
import com.olympus.nbva.contracts.ContractData;
import com.olympus.nbva.kits.GetKitData;
import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;

@WebServlet("/nbvalistexcelmain")
public class AssetListExcelMain extends HttpServlet {
	private final Logger logger = Logger.getLogger(AssetListExcelMain.class.getName()); // define logger
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\NBVAassetList.sql";
	static String kitFileName = "C:\\Java_Dev\\props\\kitdata\\kitdata.csv";
	
/****************************************************************************************************************************************************/

	public static ArrayList<String> getDbData(String id, String sqlQueryFile, String booked, String qType) throws IOException {
		FileInputStream fis = null;
		FileReader fr = null;
		String s = new String();
		String sep = "";
        StringBuffer sb = new StringBuffer();
        ArrayList<String> strArr = new ArrayList<String>();
		try {
			fis = new FileInputStream(propFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties connectionProps = new Properties();
		connectionProps.load(fis);
		fr = new FileReader(new File(sqlQueryFile));	
		// be sure to not have line starting with "--" or "/*" or any other non alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while((s = br.readLine()) != null){
		      sb.append(s);       
		}
		br.close();
		//displayProps(connectionProps);
		String query = new String();
		query = sb.toString();	
		//System.out.println( query);	 
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				statement = con.prepareStatement(query);
				//System.out.println("***^^^*** contractID=" + contractID);
				statement.setString(1, id);
				sep = ";";	 
				res = Olyutil.getResultSetPS(statement);		 	 
				strArr = Olyutil.resultSetArray(res, sep);			
			}		
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return strArr;
	}
	/*****************************************************************************************************************************************************/
	/***********************************************************************************************************************************/
	//   Map<String, CellStyle> styles = createStyles(workbook); // return styles to Hash
	// Ex. --> titleCell.setCellStyle(styles.get("title")); // deref title in hash and set cell
	public static Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap();
        
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)18);
        titleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        style.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());  
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND); 
        styles.put("title", style); // assign to Map
        
        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short)11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style); // assign to Map

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style); // assign to Map

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula", style); // assign to Map

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula_2", style); // assign to Map

        return styles;
    }
	
	
	/***********************************************************************************************************************************/
	public static XSSFSheet newWorkSheet(XSSFWorkbook workbook, String label) {

		XSSFSheet sheet = workbook.createSheet(label);
		return sheet;
	}
	/***********************************************************************************************************************************/
	public static XSSFWorkbook newWorkbook() {

		XSSFWorkbook workbook = new XSSFWorkbook();
		return workbook;
	}
	/****************************************************************************************************************************************************/
	public static void contractHeader(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> headerArr) {

		int rowNum = 4;
		int colNum = 0;
		
		
		Map<String, CellStyle> styles = createStyles(workbook);
		
		
		Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(45);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("NBVA Asset List");
        
        
        titleCell.setCellStyle(styles.get("cell"));
        
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$K$1"));
       
        
        
		 Font font = workbook.createFont();
         font.setFontHeightInPoints((short) 12);
         font.setFontName("Times New Roman");
         font.setColor(IndexedColors.BLACK.getIndex());
         font.setBold(true);
         CellStyle style = workbook.createCellStyle();
         
         
         
         
         
         style.setFont(font);
         
         style.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());  
         style.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
         style.setBorderRight(BorderStyle.THIN);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderBottom(BorderStyle.THIN);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft(BorderStyle.THIN);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderTop(BorderStyle.THIN);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	 
			 
			 
			
			
         
		for (Object field : headerArr) {
			Row row = sheet.createRow(rowNum++);
			Cell cell = row.createCell(colNum);
			if (field instanceof String) {
				cell.setCellStyle(style);
				cell.setCellValue((String) field);
			}
		}	
		sheet.autoSizeColumn(0); 
	}
	/****************************************************************************************************************************************************/
	public static void assetHeader(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> headerArr) {
			
		Row row = sheet.createRow(14);
		int colNum = 0;
		 Font font = workbook.createFont();
         font.setFontHeightInPoints((short) 12);
         font.setFontName("Times New Roman");
         font.setColor(IndexedColors.BLACK.getIndex());
         font.setBold(true);
         CellStyle style = workbook.createCellStyle();
         style.setFont(font);
         
         
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	 
         
         style.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());  
         style.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
		
		for (Object field : headerArr) {
			Cell cell = row.createCell(colNum++);
			if (field instanceof String) {
				cell.setCellStyle(style);
				cell.setCellValue((String) field);
			}
		}
	}
	

	
	/*****************************************************************************************************************************************************/
	public static AssetData loadAssetObj(String[] line) {
		AssetData asset = new AssetData();
		double equipCost = Olyutil.strToDouble(line[20]);
		double residual = Olyutil.strToDouble(line[19]);
		String desc = line[10];
		
		if (desc.equals("EUA") || desc.equals("B/O")) {
			//System.out.println("***^^^*** AssetData: Desc="  +  desc );
			if (residual == 0.00 || equipCost == 0.00) {
				//System.out.println("***^^^*** AssetData: residual="  +  residual  + " -- EC=" + equipCost);
				asset = null;
				return(asset);
			}	
		}
		
		 //System.out.println("*** AssetData:" + line.toString() );
		//System.out.println("***^^^*** AssetData: L22="  +  line[22] + "-- Fmt" + Olyutil.strToInteger(line[22] ) );
		//System.out.println("***^^^*** AssetData: L11="  +  line[11] + "--"   );
		//System.out.println("***^^^*** AssetData: L12="  +  line[12] + "--"   );
		
		 asset.setAssetId(Olyutil.strToLong(line[7]));
		 asset.setEquipType(line[8]); 
		 //asset.setCustomerID(line[9]); 
		 asset.setEquipDesc(desc); 
		 asset.setModel(line[11]); 
		 asset.setSerNum(line[12]); 
		 asset.setQty(Olyutil.strToInteger(line[13])); 
		 asset.setEquipAddr1(line[14]); 
		 asset.setEquipAddr2(line[15]); 
		 asset.setEquipCity(line[16]); 
		 asset.setEquipState(line[17]);
		 asset.setEquipZip(line[18]); 
		 
		 asset.setResidAmt(residual);
		 
		 asset.setEquipCost(equipCost);
		 
		 asset.setaRentalAmt(Olyutil.strToDouble(line[21]));
		 asset.setDispCode(Olyutil.strToInteger(line[22])); 
		 //asset.setTermDate( line[23]); 
		 return(asset);
	}
	/****************************************************************************************************************************************************/
	public static ContractData loadContractObj(String[] strSplitArr, String effectiveDate) {
	 
		ContractData contract = new ContractData();
		//double servicePay = 0.0;
		//double equipPay = 0.0; 
		contract.setContractID(strSplitArr[0]); 
		contract.setCustomerName(strSplitArr[1]); 
		contract.setCommenceDate(strSplitArr[2]);
		contract.setTerm(Olyutil.strToLong(strSplitArr[3])); 
		contract.setTermDate(strSplitArr[23]); 
		contract.setEquipPayment(Olyutil.strToDouble(strSplitArr[5])); 
		contract.setServicePayment(Olyutil.strToDouble(strSplitArr[6]));; 
		contract.setContractStatus(strSplitArr[24]); 
		contract.setInvoiceCode(strSplitArr[25]); 
		contract.setPurOption(strSplitArr[26]); 
		contract.setEffectiveDate(effectiveDate);
		 
		contract.setFinalInvDueDate(strSplitArr[4]);
		contract.setCustomerID(strSplitArr[9]);
		contract.setCustomerAddr1(strSplitArr[14]); 
		contract.setCustomerAddr2(strSplitArr[15]); 
		contract.setCustomerCity(strSplitArr[16]); 
		contract.setCustomerState(strSplitArr[17]);
		contract.setCustomerZip(strSplitArr[18]); 
		 
		
		//System.out.println("*** ContractData:" + strSplitArr.toString() );
		return(contract);
	}
	/****************************************************************************************************************************************************/
	/****************************************************************************************************************************************************/
	public static void loadWorkSheetContracts(XSSFWorkbook workbook, XSSFSheet sheet, List<Pair<ContractData, List<AssetData> >> rtnPair ) {
		int listArrSZ = rtnPair.size();
		 ContractData contractData = new ContractData();
		 
		 
		 		
		if (listArrSZ > 0) {	
			//System.out.println("*** listArrSZ=" + listArrSZ);
			for (int i = 0; i < listArrSZ; i++ ) {
				contractData = rtnPair.get(i).getLeft();
		 
				CellStyle style = workbook.createCellStyle();
				style.setBorderRight(BorderStyle.THIN);
				style.setRightBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderBottom(BorderStyle.THIN);
				style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderLeft(BorderStyle.THIN);
				style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderTop(BorderStyle.THIN);
				style.setTopBorderColor(IndexedColors.BLACK.getIndex());	
				
		            
				Row row = sheet.getRow(4);
				Cell cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue((String) contractData.getContractID());
				
				row = sheet.getRow(5); // AgreementNum
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue((String) contractData.getCustomerID());		
				//cell.setCellValue((String) "TBD");
				
				
				row = sheet.getRow(6);
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue((String) contractData.getCustomerName());		
		
				row = sheet.getRow(7);
				sheet.autoSizeColumn(1); 
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue((String) contractData.getCommenceDate());	
				
				row = sheet.getRow(8);
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue((String) contractData.getTermDate());
				
				
				row = sheet.getRow(9);
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue((long) contractData.getTerm());
				style.setAlignment(HorizontalAlignment.LEFT);
				
				row = sheet.getRow(10);
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue((double) contractData.getEquipPayment());
				style.setAlignment(HorizontalAlignment.LEFT);
				
				row = sheet.getRow(11);
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue((double) contractData.getServicePayment());
				style.setAlignment(HorizontalAlignment.LEFT);
			}
		}
	}

	
	/****************************************************************************************************************************************************/
	public static void loadWorkSheetAssets(XSSFWorkbook workbook, XSSFSheet sheet, List<Pair<ContractData, List<AssetData> >> rtnPair ) {
		AssetData assetData = new AssetData();
		int listArrSZ = rtnPair.size();
		
		if (listArrSZ > 0) {	
			for (int i = 0; i < listArrSZ; i++ ) {
				int rtnArrSZ = rtnPair.get(i).getRight().size();
				List<AssetData> assetList = new ArrayList<AssetData>();
				assetList	= rtnPair.get(i).getRight();
				//System.out.println("<h5> listArrSZ =" + listArrSZ + " -- rtnArrSZ=" +  rtnArrSZ + "--</h5>");
				for (int n = 0; n < rtnArrSZ; n++ ) {
					AssetData asset = new AssetData();
					asset = assetList.get(n);
					//System.out.println("*** AssetReturn: EquipmentType=" + asset.getEquipType() + "--");
					//System.out.println("*** AssetReturn: N=" + n + " -- CustomerID=" + asset.getCustomerID() + "--");
					
					
					CellStyle style = workbook.createCellStyle();
					style.setBorderRight(BorderStyle.THIN);
					style.setRightBorderColor(IndexedColors.BLACK.getIndex());
					style.setBorderBottom(BorderStyle.THIN);
					style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
					style.setBorderLeft(BorderStyle.THIN);
					style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
					style.setBorderTop(BorderStyle.THIN);
					style.setTopBorderColor(IndexedColors.BLACK.getIndex());	
					
					
					
					Row row = sheet.createRow(n + 15);
					Cell cell = row.createCell(0);
					cell.setCellValue((long) asset.getAssetId());
					
					cell.setCellStyle(style);
					cell = row.createCell(1);
					cell.setCellValue((String) asset.getEquipType());
					//sheet.autoSizeColumn(1); 
					cell.setCellStyle(style);
					/*
					cell = row.createCell(2);
					cell.setCellStyle(style);
					cell.setCellValue((String) asset.getCustomerID());
					sheet.autoSizeColumn(2); 
					
					*/
					
					cell = row.createCell(2);
					cell.setCellStyle(style);
					cell.setCellValue((String) asset.getEquipDesc());					
					//sheet.autoSizeColumn(2); 
					cell = row.createCell(3);
					cell.setCellStyle(style);
					cell.setCellValue((String) asset.getModel());
					//sheet.autoSizeColumn(3); 
					cell = row.createCell(4);
					cell.setCellStyle(style);
					cell.setCellValue( asset.getSerNum().replaceAll("null", ""));
					//sheet.autoSizeColumn(4); 
					cell = row.createCell(5);
					cell.setCellStyle(style);
					cell.setCellValue((int) asset.getQty());
					//sheet.autoSizeColumn(5); 
					cell = row.createCell(6);
					cell.setCellStyle(style);
					cell.setCellValue((String) asset.getEquipAddr1());
					//sheet.autoSizeColumn(6); 
					cell = row.createCell(7);
					cell.setCellStyle(style);
					cell.setCellValue((String) asset.getEquipCity());
					//sheet.autoSizeColumn(7); 
					
					cell = row.createCell(8);
					cell.setCellStyle(style);
					cell.setCellValue((String) asset.getEquipState());
					//sheet.autoSizeColumn(8); 
					
					cell = row.createCell(9);
					cell.setCellStyle(style);
					cell.setCellValue((String) asset.getEquipZip());
					//sheet.autoSizeColumn(9); 
					
					cell = row.createCell(10);
					cell.setCellStyle(style);
					cell.setCellValue((int) asset.getDispCode());
					//sheet.autoSizeColumn(10); 
					
					
				/*	
					cell = row.createCell(11);
					cell.setCellStyle(style);
					cell.setCellValue((double) asset.getResidAmt());
					sheet.autoSizeColumn(11); 
					
					cell = row.createCell(12);
					cell.setCellStyle(style);
					cell.setCellValue((double) asset.getEquipCost());
					sheet.autoSizeColumn(12); 
					
					cell = row.createCell(13);
					cell.setCellStyle(style);
					cell.setCellValue((double) asset.getaRentalAmt());
					sheet.autoSizeColumn(13); 
					cell = row.createCell(14);
					cell.setCellStyle(style);
					cell.setCellValue((int) asset.getDispCode());
					sheet.autoSizeColumn(14);
					
					
				*/
				} // end for n
				//System.out.println("*** End n loop");
			} //end for i
			//System.out.println("*** End i loop");
		} // end if	
		//System.out.println("*** End if");
	}
	
/****************************************************************************************************************************************************/

	
	/*****************************************************************************************************************************************************/
	public static void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    int sz = mp.size();
	    
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println("Map Size= " + sz + " -- " + pair.getKey() + " = " + pair.getValue());
	        
	    }
	}
	/****************************************************************************************************************************************************/
	public static  List<Pair<ContractData, List<AssetData> >> parseData(ArrayList<String> strArr, int sz, String effDate ) {
		String[] strSplitArr = null;
		ContractData contract = null;
		AssetData asset = null;
		List<AssetData> assets = new ArrayList<AssetData>();
		List<Pair<ContractData, List<AssetData> >> listRtn = new ArrayList<>();
		
		int i = 0;
		 //System.out.println("*** SZ=" + sz );
		for (i = 0; i < sz; i++) {
			//System.out.println("*** Data:" + strArr.get(i) );
			strSplitArr = Olyutil.splitStr(strArr.get(i), ";");
			//purchOption = strSplitArr[26];	
			 //System.out.println("i=" + i + " -- Value=" + strSplitArr[i]);  
			if (i == 0) { // get Contract data
				contract = loadContractObj(strSplitArr, effDate);
				
					asset = loadAssetObj(strSplitArr);
				
			} else { // get Asset data && run checks	
				asset = loadAssetObj(strSplitArr);
			}
			if (asset != null) {
				
				assets.add(asset);	
			}
		}
		//org.apache.commons.lang3.tuple.MutablePair<ContractData, List<AssetData>> p = org.apache.commons.lang3.tuple.MutablePair.of(contract, assets);
		//
		//listRtn.add(p);	
		//listRtn.add(Pair.of(contract, assets));   
		listRtn.add(Pair.of(contract, assets));   
		//System.out.println("*** ContractReturn: ID=" + contract.getContractID() + "--");
		//System.out.println("*** ContractReturn: EquipCost=" + contract.getEquipPayment() + "--");
		//System.out.println("*** AssetReturn: SerNum=" + asset.getSerNum() + "--");
		return(listRtn); 
	}

	/****************************************************************************************************************************************************/
	
	/*****************************************************************************************************************************************************/
	public static void buildExcel(HashMap<String, List<Pair<ContractData, List<AssetData>>>> objMap, HttpServletResponse response) throws FileNotFoundException {
		 
		String contractHeaderFile = "C:\\Java_Dev\\props\\headers\\NBVA_ContractHrd.txt";
		String headerFile = "C:\\Java_Dev\\props\\headers\\NBVA_AssetHrdExcel.txt";
		String ageFile = "C:\\Java_Dev\\props\\nbvaupdate\\dailyAge.csv";
		
		ArrayList<String> assetHeaderArr = new ArrayList<String>();
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		String tab1 = "";
		workbook = new XSSFWorkbook();
		ArrayList<String> contractHeaderArr = new ArrayList<String>();
		contractHeaderArr = Olyutil.readInputFile(contractHeaderFile);
		String FILE_NAME = "NBVA_Asset_List_Report_" + dateStamp + ".xlsx";
		FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
		assetHeaderArr = Olyutil.readInputFile(headerFile);
		Iterator it = objMap.entrySet().iterator();
	    int sz = objMap.size();
	    String key = "";
	    int i = 0;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        key = (String) pair.getKey();
	       // System.out.println("Map Size= " + sz + " -- " + pair.getKey() + " = " + pair.getValue());
	        //System.out.println("Key=" + key);
	        //Olyutil.printStrArray(objMap.get(key));
	        List<Pair<ContractData, List<AssetData> >> list =  objMap.get(key);
	        String cn = objMap.get(key).get(i).getLeft().getCustomerName();
	        
	        
	        sheet = newWorkSheet(workbook, key);
	        //System.out.println("Key=" + key + "-- CN=" + cn);
	        contractHeader(workbook, sheet, contractHeaderArr);

			assetHeader(workbook, sheet, assetHeaderArr);
			//System.out.println("** Call loadWorkSheetContracts");
			loadWorkSheetContracts(workbook, sheet, list);
			//System.out.println("** Call loadWorkSheetAssets");
			loadWorkSheetAssets(workbook, sheet, list);
	        
	        //System.out.println("***********************************************************************************************************");
	    }
		
		// BufferedInputStream in = null;

		try {
			// HttpServletResponse response = getResponse(); // get ServletResponse
			response.setContentType("application/vnd.ms-excel"); // Set up mime type
			response.addHeader("Content-Disposition", "attachment; filename=" + FILE_NAME);
			OutputStream out2 = response.getOutputStream();
			workbook.write(out2);
			out2.flush();

			// ********************************************************************************************************************************

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		
	}
/****************************************************************************************************************************************************/

	// Service method
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		HashMap<String, ArrayList<String>> idMap = new HashMap<String, ArrayList<String>>();
		String logFileName = "assetListApp.log";
		String directoryName = "D:/Kettle/logfiles/assetListApp";
		Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
		List<Pair<ContractData, List<AssetData>>> rtnPair = new ArrayList<>();
		HashMap<String, List<Pair<ContractData, List<AssetData>>>> objMap = new HashMap<String, List<Pair<ContractData, List<AssetData>>>>();
		
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		
		ArrayList<String> ageArr = new ArrayList<String>();
		ArrayList<String> idArrList = new ArrayList<String>();
		ArrayList<String> kitArr = new ArrayList<String>();
		String contractHeaderFile = "C:\\Java_Dev\\props\\headers\\NBVA_ContractHrd.txt";
		String headerFile = "C:\\Java_Dev\\props\\headers\\NBVA_AssetHrdExcel.txt";
		String ageFile = "C:\\Java_Dev\\props\\nbvaupdate\\dailyAge.csv";
		String FILE_NAME = "NBVA_Asset_List_Report_" + dateStamp + ".xlsx";
		String excelTemplate = "C:\\Java_Dev\\props\\nbvaupdate\\excelTemplates\\AssetList.xlsx";
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		idArrList = (ArrayList<String>) request.getSession().getAttribute("idArrList");
		//assetHeaderArr = Olyutil.readInputFile(headerFile);
		for( String idVal : idArrList) { // get data for each ID and store in hash
			String dateFmt = Olyutil.formatDate("yyyy-MM-dd hh:mm:ss.SSS");
			logger.info(dateFmt + ": " + "------------------Begin processing contractID: " + idVal);
			  // System.out.println("***%%%*** ID=" + idVal);		   
			   ArrayList<String> strArr = new ArrayList<String>();
			   strArr = getDbData(idVal, sqlFile, "", "Asset");
			   int arrSZ = strArr.size();
			   idMap.put(idVal, strArr);
			   
			   kitArr = GetKitData.getKitData(kitFileName);
				// Olyutil.printStrArray(kitArr);
				//rtnPair = parseData(strArr, arrSZ, effDate );
				rtnPair = parseData(strArr, arrSZ, "");
			
			   objMap.put(idVal, rtnPair);
		}

		
		buildExcel(objMap, response);
		
		
	
		
		
	 
		 
	 
			fileHandler.close();   //must call h.close or a .LCK file will remain.
	 
		
		 
		 
	} // End doGet

} // End Class
