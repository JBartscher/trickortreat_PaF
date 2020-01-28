package main.java.configuration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.PrintWriter;

/**
 * class to read and write parameters in json file
 * @param <P>
 */
public class Configuration<P> {

    private final static String configPath = "res/config.json";

    /**
     * get parameters
     * @param key
     * @return
     */
    public P getParam(String key) {

        P param = null;

        try {
            // parsing file
            Object obj = new JSONParser().parse(new FileReader(configPath));

            // typecasting obj to JSONObject
            JSONObject jo = (JSONObject) obj;

            // get param
            param = (P) jo.get(key);

        } catch (Exception e) {
            System.out.println(e);
        }

        return param;
    }

    /**
     * set parameters
     * @param key
     * @param value
     */
    public void setParam(String key, P value) {

        try {
            // parsing file
            Object obj = new JSONParser().parse(new FileReader(configPath));

            // typecasting obj to JSONObject
            JSONObject jo = (JSONObject) obj;

            // putting data to JSONObject 
            jo.put(key, value); 

            // writing JSON to file
            PrintWriter pw = new PrintWriter(configPath); 
            pw.write(jo.toJSONString()); 
            pw.flush(); 
            pw.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}