package controller;

import model.ScheduleModel;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by arup3 on 5/11/2017.
 */
public class FileUtils {

    public String readFile(){
        BufferedReader br = null;
        String jsonString= null;
        try {
            br= new BufferedReader(new FileReader("events.txt"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            jsonString = sb.toString();
        }catch(Exception e){
            System.out.println("error reading file:  "+e.toString());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("error reading file:  "+e.toString());
            }
            return jsonString;
        }
    }

    private void writeJson(String list) throws IOException{
        File f = new File("events.txt");
        System.out.println(f);
        FileWriter fw = new FileWriter(f,true);
        System.out.println(fw);
        try{
            BufferedWriter bw = new BufferedWriter(fw);
            System.out.println(bw);
            bw.newLine();
            bw.write(list);
            bw.flush();
            bw.close();
        }
        catch(Exception e) {
            System.out.println("error in write json :"+e.toString());
        }
    }


    public String createJosn(ArrayList<ScheduleModel> modelList){
        String mJson = "{" +
                "\"events\":[ " ;
        for(int i=0;i<modelList.size();i++){
            if(i!=0 && i<modelList.size()){
                mJson+=",";
            }
            mJson +="{\"year\": "+modelList.get(i).getYear()+
                    ","+
                    "\"month\": "+modelList.get(i).getMonth()+
                    ","+
                    "\"day\": "+modelList.get(i).getDay()+
                    ","+
                    "\"title\": \""+modelList.get(i).getTitle()+"\""+
                    ","+
                    "\"start\": \""+modelList.get(i).getStartTime()+"\""+
                    ","+
                    "\"end\": \""+modelList.get(i).getEndTime()+"\"}";
        }
        mJson+="]" +
                "}";

//        System.out.println("created json: "+mJson);
        return mJson;
    }

    public void rewriteJson (String mJson) {
        BufferedWriter bw = null;
        try {
            System.out.println("started file writing");
            checkFileExistance();
            bw = new BufferedWriter(new FileWriter("events.txt", false));
            bw.write(mJson);
            bw.newLine();
            bw.flush();
            System.out.println("file writing ended");

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("exception io :"+ioe.toString());
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {
                System.out.println("exception append: "+ioe2.toString());
            }
        }
    }


    public boolean checkFileExistance(){
        File file = new File("events.txt");
        boolean exists = file.exists();
        if (file.exists() && file.isFile())
        {
            //
        }
        return exists;
    }
}
