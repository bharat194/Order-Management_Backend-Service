package com.main.apiDev.services;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.stereotype.Service;

import com.main.apiDev.dbModel.SoftwarePurchase;
import com.main.apiDev.request.ApiRequest;
import com.opencsv.CSVReader;

import lombok.Data;

@Service
public class ApiServiceImpl implements ApiService {

	private List<SoftwarePurchase> purchases;

	@Override
	public List<SoftwarePurchase> importCSV() throws Exception {
		purchases = new ArrayList<>();
		try (CSVReader reader = new CSVReader(new FileReader("/Users/bharatjoshi/Downloads/data.csv"))) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
			String[] line;
			reader.readNext();
			while ((line = reader.readNext()) != null) {
				SoftwarePurchase purchase = new SoftwarePurchase();
				purchase.setId(Integer.parseInt(line[0]));
				purchase.setDate(dateFormat.parse(line[1]));
				purchase.setUser(line[2]);
				purchase.setDepartment(line[3]);
				purchase.setSoftware(line[4]);
				purchase.setSeats(Integer.parseInt(line[5]));
				purchase.setAmount(Double.parseDouble(line[6]));
				purchases.add(purchase);
			}
		}
		return purchases;

	}

	@Override
	public Object getTotalItems(ApiRequest apiRequest) throws Exception {
		Integer resInteger = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		Date startDate = dateFormat.parse(apiRequest.getStartDate()),
				endDate = dateFormat.parse(apiRequest.getEndate());
		for (SoftwarePurchase curPurchase : purchases) {
			if (curPurchase.getDate().compareTo(startDate) >= 0 && curPurchase.getDate().compareTo(endDate) <= 0
					&& curPurchase.getDepartment().contentEquals(apiRequest.getDepartment())) {
				resInteger++;
			}
		}
		return resInteger;
	}

	@Override
	public Object getNthMostTotalItem(ApiRequest apiRequest) throws Exception {
		PriorityQueue<Pair> priorityQueue;
		Map<String, Double> priceMap = new HashMap<>(), quantityMap = new HashMap<>();
		if (apiRequest.getItemBy().contentEquals("price")) {
			priorityQueue = new PriorityQueue<>(
					(a, b) -> (int) Math.round(a.totalPrice) - (int) Math.round(b.totalPrice));
		} else {
			priorityQueue = new PriorityQueue<>((a, b) -> a.qunatity - b.qunatity);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		Date startDate = dateFormat.parse(apiRequest.getStartDate()),
				endDate = dateFormat.parse(apiRequest.getEndate());
		for (SoftwarePurchase curPurchase : purchases) {
			if (curPurchase.getDate().compareTo(startDate) >= 0 && curPurchase.getDate().compareTo(endDate) <= 0) {
				if (apiRequest.getItemBy().contentEquals("price")) {
					priceMap.put(curPurchase.getSoftware(),
							priceMap.getOrDefault(curPurchase, (double) 0) + curPurchase.getAmount());
				} else {
					quantityMap.put(curPurchase.getSoftware(), quantityMap.getOrDefault(curPurchase, (double) 0) + 1);
				}
			}
		}
		if (apiRequest.getItemBy().contentEquals("price")) {
			for (String str : priceMap.keySet()) {
				priorityQueue.add(new Pair(str, null, priceMap.get(str)));
			}
			Integer val = apiRequest.getInteger();
			while (val-- > 1 && !priorityQueue.isEmpty()) {
				priorityQueue.poll();
			}
			if (!priorityQueue.isEmpty())
				return priorityQueue.peek().itemName;
		} else {
			for (String str : quantityMap.keySet()) {
				priorityQueue.add(new Pair(str, (int) Math.round(quantityMap.get(str)), null));
			}
			Integer val = apiRequest.getInteger();
			while (val-- > 1 && !priorityQueue.isEmpty()) {
				priorityQueue.poll();
			}
			if (!priorityQueue.isEmpty())
				return priorityQueue.peek().itemName;
		}
		return null;
	}

	@Override
	public Object getPercentageOfDepartmentWiseSoldItems(ApiRequest apiRequest) throws Exception {
		Map<String, Integer> countsMap = new HashMap<>();
		Integer totalCount = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		Date startDate = dateFormat.parse(apiRequest.getStartDate()),
				endDate = dateFormat.parse(apiRequest.getEndate());
		for (SoftwarePurchase curPurchase : purchases) {
			if (curPurchase.getDate().compareTo(startDate) >= 0 && curPurchase.getDate().compareTo(endDate) <= 0) {
				totalCount++;
				countsMap.put(curPurchase.getDepartment(), countsMap.getOrDefault(curPurchase.getDepartment(), 0) + 1);
			}
		}
		StringBuilder resStringBuilder = new StringBuilder();
		for (String department : countsMap.keySet()) {
			resStringBuilder.append(department + ":");
			Double percentage = ((double) countsMap.get(department) / (double) totalCount) * 100;
			resStringBuilder.append(percentage);
			resStringBuilder.append("   ");
		}
		return resStringBuilder;
	}

	@Override
	public Object getMonthlySales(ApiRequest apiRequest) {
		Map<String, Double> salesMap = new HashMap<>();
		salesMap.put("Jan", 0.0);salesMap.put("Feb", 0.0);
		salesMap.put("Mar", 0.0);salesMap.put("Apr", 0.0);
		salesMap.put("May", 0.0);salesMap.put("Jun", 0.0);
		salesMap.put("Jul", 0.0);salesMap.put("Aug", 0.0);
		salesMap.put("Sep", 0.0);salesMap.put("Oct", 0.0);
		salesMap.put("Dec", 0.0);salesMap.put("Nov", 0.0);
		for(SoftwarePurchase curPurchase:purchases) {
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(curPurchase.getDate());
	        int month = calendar.get(Calendar.MONTH);
	        switch (month) {
	            case Calendar.JANUARY:
	                salesMap.put("Jan", salesMap.get("Jan") + curPurchase.getAmount());
	                break;
	            case Calendar.FEBRUARY:
	            	salesMap.put("Feb", salesMap.get("Feb") + curPurchase.getAmount());
	                break;
	            case Calendar.MARCH:
	            	salesMap.put("Mar", salesMap.get("Mar") + curPurchase.getAmount());
	                break;
	            case Calendar.APRIL:
	            	salesMap.put("Apr", salesMap.get("Apr") + curPurchase.getAmount());
	                break;
	            case Calendar.MAY:
	            	salesMap.put("May", salesMap.get("May") + curPurchase.getAmount());
	                break;
	            case Calendar.JUNE:
	            	salesMap.put("Jun", salesMap.get("Jun") + curPurchase.getAmount());
	                break;
	            case Calendar.JULY:
	            	salesMap.put("Jul", salesMap.get("Jul") + curPurchase.getAmount());
	                break;
	            case Calendar.AUGUST:
	            	salesMap.put("Aug", salesMap.get("Aug") + curPurchase.getAmount());
	                break;
	            case Calendar.SEPTEMBER:
	            	salesMap.put("Sep", salesMap.get("Sep") + curPurchase.getAmount());
	                break;
	            case Calendar.OCTOBER:
	            	salesMap.put("Oct", salesMap.get("Oct") + curPurchase.getAmount());
	                break;
	            case Calendar.NOVEMBER:
	            	salesMap.put("Nov", salesMap.get("Nov") + curPurchase.getAmount());
	                break;
	            case Calendar.DECEMBER:
	            	salesMap.put("Dec", salesMap.get("Dec") + curPurchase.getAmount());
	                break;
	            default:
	                break;
	        }
		}
		return salesMap;
	}

	@Data
	class Pair {

		private String itemName;

		private Integer qunatity;

		private Double totalPrice;

		Pair(String itemName, Integer qunatity, Double totalPrice) {
			this.itemName = itemName;
			this.qunatity = qunatity;
			this.totalPrice = totalPrice;
		}

	}

}
