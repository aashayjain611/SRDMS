package org.aashay.spit.SRDMS;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/stocks")
@Produces(MediaType.APPLICATION_JSON)
public class StockResource {

	private StockService stockService=new StockService();
	
	@GET
	public ArrayList<Stock> getAllStocks(@QueryParam("begin") String begin,@QueryParam("end") String end,@QueryParam("offset") String offset)
	{
		if(begin==null || end==null)
		{
			if(offset==null)
				return stockService.getAllFromDatabase();
			else
			{
				System.out.println(offset);
				return stockService.getEarlierStock(stockService.offsetToDays(offset));
			}
		}
		else
		{
			if(offset==null)
				return stockService.getAllByDate(begin, end);
			return null;
		}
	}
	
	@GET
	@Path("/{isin}")
	public ArrayList<Stock> getStockByIsin(@PathParam("isin") String isin,@QueryParam("begin") String begin,@QueryParam("end") String end,@QueryParam("offset") String offset)
	{
		if(begin==null || end==null)
			return stockService.getStockByIsin(isin);
		else
		{
			if(offset==null)
				return stockService.getStockByDate(isin, begin, end);
			else
			{
				System.out.println(offset);
				return stockService.getEarlierStock(isin, stockService.offsetToDays(offset));
			}
		}
	}
	
	@GET
	@Path("/{isin}/latest")
	public ArrayList<Stock> getLatest(String isin)
	{
		return stockService.getLatest(isin);
	}
	
	@GET
	@Path("/latest")
	public ArrayList<Stock> getAllLatest()
	{
		return stockService.getAllLatest();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public int updateDatabase(Stock stock)
	{
		return stockService.postToDatabase(stock);
	}
}
