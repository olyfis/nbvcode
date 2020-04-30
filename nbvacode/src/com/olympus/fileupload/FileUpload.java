package com.olympus.fileupload;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

@WebServlet("/fileupload3")
public class FileUpload extends HttpServlet {
	
private final static Logger LOGGER = Logger.getLogger(FileUpload.class.getCanonicalName());
	
	// location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "uploadDir";
 
    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
 

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
            return;
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
        
        System.out.println("***^^^*** UploadPath=" + uploadPath);
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
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
            	while (it.hasNext()) {
    				FileItem item = (FileItem) it.next();
    				if (item.isFormField()) {
    					// Plain request parameters will come here.
    					String name = item.getFieldName();
    					String value = item.getString();
    					System.out.println("***^^*** Adding Name:" + name + "-- Value:" + value + "--");
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
						request.setAttribute("message",
								"Upload has been done successfully! File located at: " + filePath);
						//System.out.println("*** ELSE: FN" + fileName);
						System.out.println("***!!!*** fileName:" + fileName + "--");					
    				}
    			}
            }
            
            
        } catch (Exception ex) {
            request.setAttribute("message",
                    "There was an error: " + ex.getMessage());
        }
        request.getSession().setAttribute("paramMap", paramMap);
        // redirects client to message page
        getServletContext().getRequestDispatcher("/message.jsp").forward(
                request, response);
        
        
	} // End doPost()
} // End Class
