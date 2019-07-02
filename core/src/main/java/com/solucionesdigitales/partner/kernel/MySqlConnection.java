package com.solucionesdigitales.partner.kernel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Osain Dabián
 * 201808018
 *
 */
@Service("mySqlConnection")
public class MySqlConnection {
	@Value("${spring.datasource.url:jdbc:mysql://localhost:3306/ma?autoReconnect=true&useSSL=false&tinyInt1isBit=false}")
	private String url;
	@Value("${spring.datasource.username:root}")
	private String usr;
	@Value("${spring.datasource.password:root}")
	private String psw;
	
	private static final Logger LOG = LoggerFactory.getLogger(MySqlConnection.class);
	private Statement  stm = null;
	public  Connection cnc = null;
	public  ResultSet  rsl = null;
	public  int        len = 0;
	
	public void copy(String src, String obj) {
		drop(obj);
		execute("CREATE TABLE `"+obj+"` SELECT * FROM `"+src+"`;");
	}
	
	public void drop(String tbl) {
		execute("DROP TABLE IF EXISTS `"+tbl+"`;");
	}
	
	public void zap(String tbl) {
		if(!execute("TRUNCATE `"+tbl+"`;")) {
			execute("DELETE FROM `"+tbl+"`;");
		}		
	}
	
	public long count(String tbl) {
		select("SELECT COUNT(*) AS records FROM `"+tbl+"`;");
		len = 0;
		try {
			if(rsl.next()) {
				len = rsl.getInt("records");
			}
		} catch(Exception e) {}
		return(len);
	}
	
	public boolean select(String cmd) {
		len = 0;
		if(cnc==null) logOn();
		try {
			stm = cnc.createStatement();
			rsl = stm.executeQuery(cmd);
			if(rsl.last()) {
				len = rsl.getRow();
				rsl.beforeFirst();
			}
		} catch(Exception e) {
			LOG.error(e.getMessage());
			//e.printStackTrace();
			return(false);
		}
		return(true);
	}
	
	public boolean execute(String cmd) {
		if(cnc==null) logOn();
		try {
			stm = cnc.createStatement();
			stm.execute(cmd);
		} catch(Exception e) {
			LOG.error(e.getMessage());
			//e.printStackTrace();
			return(false);
		}
		return(true);
	}
	
	public void logOn() {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			cnc = DriverManager.getConnection(url,usr,psw);
		} catch(Exception e) {
			LOG.error(e.getMessage());
		}
	}
	
	public void logOff() {
		if(rsl!=null) try {rsl.close();rsl=null;} catch(Exception e) {}
		if(stm!=null) try {stm.close();stm=null;} catch(Exception e) {}
		if(cnc!=null) try {cnc.close();cnc=null;} catch(Exception e) {}
	}
}
