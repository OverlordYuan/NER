package com.java.overlord.ner.service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NERService {
	private static Hanlp_NER HNER = new Hanlp_NER();
	private static Logger logger = LoggerFactory.getLogger(NERService.class.getName());

	public JSONObject ner(JSONObject data){
		JSONObject o = new JSONObject();
		if (null == data){
			o.put("Error","DataError:The input data is empty!");
			logger.error("DataError:The input data is empty!");
		}
		else{
			try{
				String content = data.get("content").toString().replaceAll("[@ \n]","");
				long start = System.currentTimeMillis();
				o  = HNER.ner(content);
				long end = System.currentTimeMillis();
				logger.info("result={},Time elapse = {} ms.",o.toJSONString(),(end - start));
			}
			catch (Exception NERError){
				o.put("NERError","NERError:"+ NERError.getMessage());
				logger.error("NERError:",NERError);
			}
		}
		return o;
	}
}
