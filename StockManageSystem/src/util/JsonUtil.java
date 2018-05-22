package util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {

	public static JSONArray formatRsToJsonArray(ResultSet rs) throws Exception{
		ResultSetMetaData md = rs.getMetaData();//get结果集
		int num = md.getColumnCount();//得到每个结果集有多少列
		JSONArray array = new JSONArray();
		while(rs.next()){
			JSONObject mapOfColValues = new JSONObject();
			for(int i=1;i<=num;i++){
				Object o = rs.getObject(i);
				if(o instanceof Date){
					//将得到的结果以列名和列对应的数据存入json
					mapOfColValues.put(md.getColumnName(i), DateUtil.formatDate((Date)o, "yyyy-MM-dd"));
				}else{
					mapOfColValues.put(md.getColumnName(i), rs.getObject(i));					
				}
			}
			array.add(mapOfColValues);
		}
		return array;
	}
}
