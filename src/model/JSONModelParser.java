package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by arup3 on 5/12/2017.
 */
public class JSONModelParser {

    JSONObject configObject;

    public ArrayList<ScheduleModel> parseJson(String mJson){
        ArrayList<ScheduleModel> modelList = new ArrayList<>();
        configObject = new JSONObject(mJson);
        try {
            JSONArray array = configObject.getJSONArray("events");
            for(int i = 0; i < array.length(); i++) {
//                addImage( array.getString(i) );
                ScheduleModel model = new ScheduleModel();
                JSONObject obj = array.getJSONObject(i);
                model.setYear(obj.getInt("year"));
                model.setMonth(obj.getInt("month"));
                model.setDay(obj.getInt("day"));
                model.setTitle(obj.getString("title"));
                model.setStartTime(obj.getString("start"));
                model.setEndTime(obj.getString("end"));
                modelList.add(model);
            }
        } catch(Exception e) {
            System.out.println("Configuration Error: "+ e.toString());
            return null;

            // add the default bg image if possible
//            addImage("resources/bg.jpg");
        }
        return modelList;
    }
}
