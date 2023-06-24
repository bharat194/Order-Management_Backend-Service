package com.main.apiDev.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.main.apiDev.request.ApiRequest;
import com.main.apiDev.response.BaseResponse;
import com.main.apiDev.response.SystemError;
import com.main.apiDev.services.ApiService;


@RestController
@RequestMapping("/api")
public class Controller {
	
	private ApiService apiService;
	
	public Controller(ApiService apiService) {
		this.apiService = apiService;
	}
	
	@GetMapping("/importCSV")
	public BaseResponse importCSV() throws Exception{
		BaseResponse responseStatus = new BaseResponse(SystemError.OK);
		responseStatus.setResponse(apiService.importCSV());
		return responseStatus;
		
	}
	
	@PostMapping("/total_items")
	public BaseResponse totalItems(@RequestBody ApiRequest apiRequest) throws Exception{
		BaseResponse responseStatus = new BaseResponse(SystemError.OK);
		responseStatus.setResponse(apiService.getTotalItems(apiRequest));
		return responseStatus;
		
	}
	
	@PostMapping("/nth_most_total_item")
	public BaseResponse nthMostTotalItem(@RequestBody ApiRequest apiRequest) throws Exception{
		BaseResponse responseStatus = new BaseResponse(SystemError.OK);
		responseStatus.setResponse(apiService.getNthMostTotalItem(apiRequest));
		return responseStatus;
		
	}
	
	@PostMapping("/percentage_of_department_wise_sold_items")
	public BaseResponse percentageOfDepartmentWiseSoldItems(@RequestBody ApiRequest apiRequest) throws Exception{
		BaseResponse responseStatus = new BaseResponse(SystemError.OK);
		responseStatus.setResponse(apiService.getPercentageOfDepartmentWiseSoldItems(apiRequest));
		return responseStatus;
		
	}
	
	@PostMapping("/monthly_sales")
	public BaseResponse monthlySales(@RequestBody ApiRequest apiRequest) throws Exception{
		BaseResponse responseStatus = new BaseResponse(SystemError.OK);
		responseStatus.setResponse(apiService.getMonthlySales(apiRequest));
		return responseStatus;
		
	}
}
