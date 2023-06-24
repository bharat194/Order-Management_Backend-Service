package com.main.apiDev.dbModel;

import java.util.Date;

import lombok.Data;

@Data
public class SoftwarePurchase {
	
    private Integer id;
    
    private Date date;
    
    private String user;
    
    private String department;
    
    private String software;
    
    private Integer seats;
    
    private Double amount;
    
}
