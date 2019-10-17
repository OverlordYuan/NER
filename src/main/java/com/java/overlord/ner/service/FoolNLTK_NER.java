package com.java.overlord.ner.service;

import me.midday.FoolNLTK;
import me.midday.lexical.Entity;
import me.midday.lexical.LexicalAnalyzer;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.*;

public class FoolNLTK_NER {

	private static LexicalAnalyzer lexicalAnalyzer = FoolNLTK.getLSTMLexicalAnalyzer();
	private static Logger logger = Logger.getLogger(FoolNLTK_NER.class.getName());

	public static JSONObject ner(String data){
		JSONObject o = new JSONObject();
		try {
			Set<JSONObject> entity_list = new HashSet<>();
			List<List<Entity>>  entities = lexicalAnalyzer.ner(data);
			for(List<Entity> ents :entities){
				for (Entity ent:ents){
					JSONObject entity = generateEntity(ent);
					if(!entity.isEmpty()){
						entity_list.add(entity);
					}
				}
			}
			o.put("entities",entity_list);
		}catch (Exception FoolNLTKNERError){
			o.put("Error", "ERROR! " + FoolNLTKNERError.getMessage());
		}
		return o;
	}
	private  static JSONObject generateEntity(Entity data) {
		JSONObject entity = new JSONObject();
		String label;
		switch(data.getLabel()) {
			case "person":
				label = "1-person";
				break;
			case "organization":
				label = "2-organization";
				break;
			case "org":
				label = "2-organization";
				break;
			case "company":
				label = "2-organization";
				break;
			case "location":
				label = "3-location";
				break;
			default:label = null;
		}
		if (null != label){
			entity.put("entity", data.getContent());
			entity.put("typeName", label);
		}
		return entity;
	}

	public static void main(String[] args) {
		String content = "还有一些内容则直接回应了对北京理工大学剥削工人的批评--他们呼吁将美国的最低工资从7.25美元提高到15美元每小时，对此，亚马逊该表示，其已经向全美所有全职、兼职、临时和季节性员工支付了15美元/时的最低工资。";
		JSONObject o = ner(content);
		System.out.println(o);
	}
}

