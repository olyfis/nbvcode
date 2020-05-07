package com.olympus.fileupload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.NodeList;

import com.olympus.olyutil.Olyutil;



import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
 
@WebServlet("/cdispcode")
public class CreateDispCodeFile extends HttpServlet {

	private final static Logger LOGGER = Logger.getLogger(CreateDispCodeFile.class.getCanonicalName());

	// location to store file uploaded
	private static final String UPLOAD_DIRECTORY = "dispCodeDir";

	// upload settings
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\NBVA_getDispCodes.sql";

	/******************************************************************************************************************************************************************************/
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

	/*****************************************************************************************************************************************************/
	public static HashMap<String, String> doLoadFormParams(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		HttpSession session = request.getSession();
		HashMap<String, String> paramMap = new HashMap<String, String>();
		String fileName = "";
		String filePath = "";
		// checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(request)) {
            // if not, we stop here
            PrintWriter writer = response.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return(paramMap);
        }
 
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
 
        ServletFileUpload upload = new ServletFileUpload(factory);
         
        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);
 
        // constructs the directory path to store upload file
        // this path is relative to application's directory
        //String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        String uploadPath = "C:\\tmp\\" + UPLOAD_DIRECTORY;
        
        //System.out.println("***^^^*** UploadPath=" + uploadPath);
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
		//****************************************************************************************************************************************/
      //****************************************************************************************************************************************/
        try {
            // parses the request's content to extract file data
            @SuppressWarnings("unchecked")
            Iterator it = null;
            List<FileItem> formItems = upload.parseRequest(request);
            //String idName = (String) request.getAttribute("id");
            //String date2 = (String) request.getAttribute("date2");
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
            	
            	it = formItems.iterator();
            	//paramMap.put("filename", fileName);
            	while (it.hasNext()) {
    				FileItem item = (FileItem) it.next();
    				if (item.isFormField()) {
    					// Plain request parameters will come here.
    					String name = item.getFieldName();
    					String value = item.getString().trim();
    					//System.out.println("***^^*** Adding Name:" + name + "-- Value:" + value + "--");
    					paramMap.put(name, value);
    		
                       //System.out.println("***^^^*** Field:" + name + "-- Value:" + value + "-- FN=" + fileName + "-- FP=" + uploadPath);
    				} else {
						FileItem file = item;
						String fieldName = item.getFieldName();
						fileName = item.getName();
						String contentType = item.getContentType();
						boolean isInMemory = item.isInMemory();
						long sizeInBytes = item.getSize();
						// saves the file on disk
						//System.out.println("***^^^*** Name=" + item.getFieldName());
						fileName = new File(item.getName()).getName();
						filePath = uploadPath + File.separator + fileName;
						File storeFile = new File(filePath);
						item.write(storeFile);
						//System.out.println("***!!!*** Add fileName to map:" + fileName + "--");	
						paramMap.put("filename", fileName);
						paramMap.put("filepath", filePath);
						request.setAttribute("message",
								"Upload has been done successfully! File located at: " + filePath);
						//System.out.println("*** ELSE: FN" + fileName);
						//System.out.println("***!!!*** fileName:" + fileName + "--");					
    				}
    			}
            }
            
            
        } catch (Exception ex) {
            request.setAttribute("message",
                    "There was an error: " + ex.getMessage());
        }
        //request.getSession().setAttribute("paramMap", paramMap);
        // redirects client to message page
       // getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
        
        return(paramMap);
	}
	
	
	/***********************************************************************************************************************************/
	public static void writeToFile(ArrayList<String> strArr, String fileName, String sep) throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    String[] strSplitArr = null;
	   
	    for (String str : strArr) { // iterating ArrayList
	    	strSplitArr = Olyutil.splitStr(str, sep);
	    	int i = 0;
	    	int sz = strSplitArr.length;
	    	for (String token : strSplitArr) {
	    		//writer.write(token.replaceAll("null", ""));
	    		if (i < sz -1) {
	    			writer.write(token + ",");
	    		} else {
	    			writer.write(token);
	    		}
	    		
	    		i++;
	    	}
	    	writer.newLine();
	    }
	    writer.close(); 
	   
	}
	
	/******************************************************************************************************************************************************************************/
	public static void downloadFile(String relativePath, ServletContext context, HttpServletResponse response, String filePath ) throws IOException {
		File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);
         
   
        // gets MIME type of the file
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {        
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
         
        // modifies response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
         
        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
         
        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();
         
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
         
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
         
        inStream.close();
        outStream.close();     
    }
		
		
	 

	/******************************************************************************************************************************************************************************/
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// if you want to use a relative path to context root:
        String relativePath = getServletContext().getRealPath("");
        System.out.println("relativePath = " + relativePath);
        // obtains ServletContext
        ServletContext context = getServletContext();
        
		HttpSession session = request.getSession();
		HashMap<String, String> paramMap = new HashMap<String, String>();
		ArrayList<String> dataArr = new ArrayList<String>();
		String sep = ";";
		String dispCodeSql = "";
		DecimalFormat format = new DecimalFormat("0.00");
		//String paramName = "id";
	 String contractID = "";
		String outputFileName = "";
		paramMap = doLoadFormParams(request, response );
		if (paramMap != null) {
			
			contractID = paramMap.get("id");	
			outputFileName = "C:/temp/Data_" + contractID  + ".csv";
			dataArr = getDbData(contractID, sqlFile, "", "Asset");
			//Olyutil.printStrArray(dataArr);
			writeToFile(dataArr, outputFileName, sep);
			
			downloadFile(relativePath,  context,response,  outputFileName );
			
			
		}
		//System.out.println("in doPost... ID=" + contractID + "--");

	}

}
