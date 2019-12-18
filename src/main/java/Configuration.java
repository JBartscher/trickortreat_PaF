package main.java;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.PrintWriter;

public class Configuration<P> {

    public P getParam(String key) {

        P param = null;

        try {
            // parsing file
            Object obj = new JSONParser().parse(new FileReader("src/main/java/configuration/config.json"));

            // typecasting obj to JSONObject
            JSONObject jo = (JSONObject) obj;

            // get param
            param = (P) jo.get(key);

            // print param
            // System.out.println(key + ": " + param);

        } catch (Exception e) {
            System.out.println(e);
        }

        return param;
    }

    public void setParam(String key, P value) {

        try {
            // parsing file
            Object obj = new JSONParser().parse(new FileReader("src/main/java/configuration/config.json"));

            // typecasting obj to JSONObject
            JSONObject jo = (JSONObject) obj;

            // putting data to JSONObject 
            jo.put(key, value); 

            // writing JSON to file
            PrintWriter pw = new PrintWriter("src/main/java/configuration/config.json"); 
            pw.write(jo.toJSONString()); 
            pw.flush(); 
            pw.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}