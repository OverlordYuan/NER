package com.java.overlord.ner.service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.*;

public class Hanlp_NER {

	private static Segment segment = HanLP.newSegment().enablePlaceRecognize(true).enableNameRecognize(true).enableOrganizationRecognize(true).enableTranslatedNameRecognize(true);
	private static Logger logger = Logger.getLogger(Hanlp_NER.class.getName());

	private static FoolNLTK_NER FNER = new FoolNLTK_NER();
	public static JSONObject ner(String data){
		JSONObject o = new JSONObject();
		try {
			Set<JSONObject> en = (Set<JSONObject>) FNER.ner(data).get("entities");
//			System.out.println(en);
//			Set<JSONObject> en = (Set<JSONObject>) FNER_0.get("entities");
			Set<JSONObject> entity_list = new HashSet<>();
			List<Term> termList = segment.seg(data);
			for(Term term :termList){
				JSONObject entity = generateEntity(term);
				if(!entity.isEmpty()){
//					System.out.println(entity);
					if (en.contains(entity)){
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
	private  static JSONObject generateEntity(Term data) {
		JSONObject entity = new JSONObject();
		String label;
		if(data.nature.toString().length()>1){
			label = data.nature.toString().substring(0,2);
			switch(label) {
				case "nr":
					label = "1-person";
					break;
				case "nt":
					label = "2-organization";
					break;
				case "ni":
					label = "2-organization";
					break;
				case "ns":
					label = "3-location";
					break;
				default:label = null;
		}
		if (null != label){
			entity.put("entity", data.word);
			entity.put("typeName", label);
		}
	}return entity;
	}

	public static void main(String[] args) {
		String content = "还有一些内容则直接回应了苹果公司" +
				"剥削工人的批评--他们呼吁将美国的最低工资从7.25美元提高到15美元每小时，对此，亚马逊该表示，其已经向全美所有全职、兼职、临时和季节性员工支付了15美元/时的最低工资。";
		JSONObject o = ner(content);
		System.out.println(o);
	}
}

