package com.olympus.nbva.nbvacode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import com.olympus.nbva.assets.AssetData;
import com.olympus.nbva.contracts.ContractData;
import com.olympus.nbva.kits.GetKitData;
import com.olympus.olyutil.Olyutil;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.formula.functions.FinanceLib;

@WebServlet("/nbvacode2")
public class NbvaCodeAsset extends HttpServlet {
	
	/*****************************************************************************************************************************************************************/
	
	
	
	/*****************************************************************************************************************************************************************/
    /****************************************************************************************************************************************************/
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 doGet(  request,   response);	
	} // End doGet()

	/****************************************************************************************************************************************************/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		String dispatchJSP = "/nbvadetail_code_result.jsp";
		HttpSession session = request.getSession(true);
		List<Pair<ContractData, List<AssetData>>> list = (List<Pair<ContractData, List<AssetData>>>) session.getAttribute("rtnPairList");
		
		HashMap<String, ArrayList<Integer>> sqlErrMap = new HashMap<String, ArrayList<Integer>>();
		ArrayList<Integer> errIDArrayRtn = new ArrayList<>();
		String s1 = (String) session.getAttribute("JB");
		String dispCoveVals = request.getParameter("dispCodeArr_0");
		 
		
		// setAssetObj(list, request);
		*/
		
		
		
	}
}
