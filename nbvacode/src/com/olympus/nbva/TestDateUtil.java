package com.olympus.nbva;

public class TestDateUtil {

	// naDate < edate -> -1
	// naDate == edate -> 0
	// naDate > edate -> 1
	
	public static void main(String[] args) {
		 
		String eDate = "2020-06-10";
		String naDate = "2020-07-10";
		int rVal = DateUtil.compareDates(naDate, eDate);
		
		System.out.println("***^^^*** eDate=" + eDate + "-- naDate=" + naDate + "-- rVal=" + rVal + "--");
	
		
	}

}
