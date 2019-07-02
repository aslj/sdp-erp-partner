package com.solucionesdigitales.partner.kernel;

import java.math.BigDecimal;
import java.sql.Date;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Osain Dabián
 * 201808018
 *
 */
@Service("table")
public class Table {
	
	private Object[][] array;
	private int size;
	
	public void setArray(Object[][] array) {
		this.array = array;
		this.size = array!=null?array.length:0;
	}
	
	public int size() {
		return(size);
	}
	
	public Cell getCell(int row, int col) {
		Cell cell = null;
		try {
			cell = new Cell(array[row][col]);
		} catch(Exception e) {}
		return(cell);
	}
	
	public class Cell {
		private static final int ROUNDED = BigDecimal.ROUND_HALF_UP;
		private final BigDecimal ZERO = new BigDecimal("0.00");
		private Object object;
		private String type;
		private BigDecimal bd;
		private Double db;
		public Cell(Object object) {
			this.object = object;
			this.type = object!=null?object.getClass().getSimpleName():null;
		}
		public Integer getAsInteger() {
			Integer value = null;
			if(object!=null) {
				try {
					if(type.equals("Integer") || type.equals("int")) {
						value = (Integer) object;
					} else if(type.equals("BigDecimal")) {
						bd = (BigDecimal) object;
						bd = bd.setScale(0,ROUNDED);
						value = Integer.parseInt(""+object);
					} else if(type.equals("Double") || type.equals("double")) {
						db = (Double) object;
						bd = (new BigDecimal(db)).setScale(0,ROUNDED);
						value = Integer.parseInt(""+object);
					} else {
						value = Integer.parseInt(""+object);
					}
				} catch(Exception e) {}
			}
			return(value!=null?value:0);
		}
		public Long getAsLong() {
			Long value = null;
			if(object!=null) {
				try {
					if(type.equals("Long") || type.equals("long") || type.equals("Integer") || type.equals("int")) {
						value = (Long) object;
					} else if(type.equals("BigDecimal")) {
						bd = (BigDecimal) object;
						bd = bd.setScale(0,ROUNDED);
						value = Long.parseLong(""+object);
					} else if(type.equals("Double") || type.equals("double")) {
						db = (Double) object;
						bd = (new BigDecimal(db)).setScale(0,ROUNDED);
						value = Long.parseLong(""+object);
					} else {
						value = Long.parseLong(""+object);
					}
				} catch(Exception e) {}
			}
			return(value!=null?value:0L);
		}
		public Double getAsDouble() {
			Double value = null;
			if(object!=null) {
				try {
					if(type.equals("Double") || type.equals("double")) {
						value = (Double) object;
					} else if(type.equals("BigDecimal")) {
						bd = (BigDecimal) object;
						value = bd.doubleValue();
					} else {
						value = Double.parseDouble(""+object);
					}
				} catch(Exception e) {}
			}
			return(value!=null?value:0D);
		}
		public BigDecimal getAsBigDecimal() {
			BigDecimal value = null;
			if(object!=null) {
				try {
					if(type.equals("BigDecimal")) {
						value = (BigDecimal) object;
					} else if(type.equals("Double") || type.equals("double")) {
						value = new BigDecimal((double)object);
					} else if(type.equals("Long") || type.equals("long")) {
						value = new BigDecimal((long)object);
					} else if(type.equals("Integer") || type.equals("int")) {
						value = new BigDecimal((int)object);
					} else {
						value = new BigDecimal(""+object);
					}
				} catch(Exception e) {}
			}
			return(value!=null?value:ZERO);
		}
		public String getAsString() {
			String value = null;
			if(object!=null) {
				try {
					value = ""+object;
				} catch(Exception e) {}
			}
			return(value!=null?value:"");
		}
		public Date getAsDate() {
			Date value = null;
			if(object!=null) {
				try {
					if(type.equals("Date")) {
						value = (Date) object;
					} else {
						value = Utl.stringToDate(""+object);
					}
				} catch(Exception e) {}
			}
			return(value);
		}
	}
}
