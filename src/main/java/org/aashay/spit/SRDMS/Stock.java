package org.aashay.spit.SRDMS;

public class Stock {
	
	private String timestamp;
	private int price;
	private String stockName;
	private String isin;
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	
	public Stock(String stockName, String isin, int price,String timestamp) {
		super();
		this.timestamp = timestamp;
		this.price = price;
		this.stockName = stockName;
		this.isin = isin;
	}
	public Stock() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
