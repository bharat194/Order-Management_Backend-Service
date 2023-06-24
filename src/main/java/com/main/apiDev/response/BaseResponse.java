package com.main.apiDev.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

	private Integer httpStatus;
	
	private String message;
	
	private Object response;
	
	public BaseResponse(SystemError systemError){
		super();
		this.httpStatus = systemError.getStatusCode();
		this.message = systemError.getStatusMessage();
	}
	
}
