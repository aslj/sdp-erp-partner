package com.solucionesdigitales.partner.kernel;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 
 * @author Osain Dabián
 * 201808018
 *
 */
public class Utl {
	private static final Logger LOGGER = LoggerFactory.getLogger(Utl.class);
	
	public static double round(double nmb, int dec) {
		double bas = Math.pow(10,dec);
		double ntg = Math.floor(nmb);
		double rsl = Math.round((nmb-ntg)*bas);
		rsl = (rsl/bas)+ntg;
		return(rsl);
	}
	
	public static String strZero(int nmb, int pst) {
		return(String.format("%0"+pst+"d",nmb));
	}
	
	public static int year(Date date) {
		Calendar cln = Calendar.getInstance();
		cln.setTime(date);
		return(cln.get(Calendar.YEAR));
	}
	
	public static java.sql.Date stringToDate(String str) {
		str = str.trim().replace("/","-");
		str = (str.indexOf("-")==2?str.substring(6,10)+str.substring(3,5)+str.substring(0,2):str);
		str = str.replace("-","");
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date aux = null;
		try {
			aux = sdf.parse(str);
		} catch(Exception e) {LOGGER.error(e.getMessage());}
		return(new java.sql.Date(aux.getTime()));
	}
	
	public static String dateToString(Date dat) {return(dateToString(dat,"yyyy-mm-dd"));}
	public static String dateToString(Date dat, String fmt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		int yr = cal.get(Calendar.YEAR);
		int mn = cal.get(Calendar.MONTH)+1;
		String ms = ",Enero,Febrero,Marzo,Abril,Mayo,Junio,Julio,Agosto,Septiembre,Octubre,Noviembre,Diciembre";
		String mt = ms.split(",")[mn];
		int dy = cal.get(Calendar.DATE);
		String str = fmt.equals("dd/Mmmm/aaaa")?strZero(dy,2)+"/"+mt+"/"+strZero(yr,4)
					:fmt.equals("dd/MMMM/aaaa")?strZero(dy,2)+"/"+mt.toUpperCase()+"/"+strZero(yr,4)
					:fmt.equals("dd/mmmm/aaaa")?strZero(dy,2)+"/"+mt.toLowerCase()+"/"+strZero(yr,4)
					:fmt.equals("dd/Mmm/aaaa")?strZero(dy,2)+"/"+mt.substring(0,3)+"/"+strZero(yr,4)
					:fmt.equals("dd/MMM/aaaa")?strZero(dy,2)+"/"+mt.substring(0,3).toUpperCase()+"/"+strZero(yr,4)
					:fmt.equals("dd/mmm/aaaa")?strZero(dy,2)+"/"+mt.substring(0,3).toLowerCase()+"/"+strZero(yr,4)
					:fmt.equals("dd/mm/aaaa")?strZero(dy,2)+"/"+strZero(mn,2)+"/"+strZero(yr,4)
					:strZero(yr,4)+"-"+strZero(mn,2)+"-"+strZero(dy,2);
		return(str);
	}
	
	public static int datesDiference(Date d1, Date d2) {
		Long end = d1.getTime()/1000;
		Long start = d2.getTime()/1000;
		if(end<start) {
			Long aux = start;
			start = end;
			end = aux;
		}
		Double dfr = (double) (end-start);
		int days = (int)Math.round(dfr/86400.0d);
		return(days);
	}

	public static ResponseEntity<byte[]> readPDF(String path) {
        FileInputStream fileStream;
        try {
            fileStream = new FileInputStream(new File(path));
            byte[] contents = IOUtils.toByteArray(fileStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String filename = "test.pdf";
            headers.setContentDispositionFormData(filename, filename);
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
            return(response);
		} catch(Exception e) {LOGGER.error(e.getMessage());}
        return(null);
    }
}
