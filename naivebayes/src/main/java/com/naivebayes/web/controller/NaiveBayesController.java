package com.naivebayes.web.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.naivebayes.model.AttributedAnomalies;
import com.naivebayes.service.NaiveBayesService;

@RestController
@EnableAutoConfiguration
public class NaiveBayesController {

	@Autowired
	public NaiveBayesService naiveService;

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@ResponseBody
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping(value = "/naive/predict/{id}", method = RequestMethod.POST)
	public String predict(@PathVariable ("id") String id) {
		
		try {
			return naiveService.predictData(id);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return "not predicted" + e;
		}
		
	}

	@RequestMapping(value = "/anomaly/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String getAnomalyById(@PathVariable("id") String id) {
		// 2309
		AttributedAnomalies anomaly;

		System.out.println("Value" + id);
		try {
			anomaly = naiveService.findById(new Long(id));
		} catch (Exception ex) {
			return "not found" + ex;
		}
		return "The anomalyd id status is: " + anomaly.getStatus();
	}
	
	@RequestMapping(value = "/anomaly/ids/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String getByIdsForAllSixMonth(@PathVariable("id") String id) {
		// 2309
		Long total;

		System.out.println("Value" + id);
		try {
			total = naiveService.getByIdsForAllSixMonth(new Long(id));
		} catch (Exception ex) {
			return "not found" + ex;
		}
		return "The Total is: " + total;
	}
}
