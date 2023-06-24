package com.main.apiDev.services;

import java.util.List;
import com.main.apiDev.dbModel.SoftwarePurchase;
import com.main.apiDev.request.ApiRequest;

public interface ApiService {
	
	public List<SoftwarePurchase> importCSV() throws Exception;
	
	public Object getTotalItems(ApiRequest apiRequest) throws Exception;
	
	public Object getNthMostTotalItem(ApiRequest apiRequest) throws Exception;
	
	public Object getPercentageOfDepartmentWiseSoldItems(ApiRequest apiRequest) throws Exception;
	
	public Object getMonthlySales(ApiRequest apiRequest) throws Exception;

}
