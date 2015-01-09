package com.theandroidsuit.frasesparapensar.util;

import android.content.Context;

import com.theandroidsuit.frasesparapensar.bean.Pensamiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Virginia Hernández on 6/01/15.
 */
public class JSONParser {


    public List<Pensamiento> getDatafromAsset(Context context){
        JSONObject jObject = loadJSON(context, FrasesParaPensarUtils.MODE_ASSET);

        return parse(jObject);
    }

    /** Receives a JSONObject and returns a list */
    public List<Pensamiento> parse(JSONObject jObject){

        JSONArray jWisdom = null;
        try {
            /** Retrieves all the elements in the 'wisdom' array */
            jWisdom = jObject.getJSONArray("pensamientos");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPensamientos(jWisdom);
    }


    private List<Pensamiento> getPensamientos(JSONArray jWisdom) {

        int wisdomCount = jWisdom.length();
        List<Pensamiento> wisdomList = new ArrayList<Pensamiento>();
        Pensamiento wisdom;

        /** Taking each Wisdom, parses and adds to list object */
        for(int i = 0; i < wisdomCount; i++){
            try {
                /** Call getCrystalWisdom with wisdom JSON object to parse the Wisdom */
                wisdom = getPensamiento((JSONObject)jWisdom.get(i));
                wisdomList.add(wisdom);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return wisdomList;
    }

    private Pensamiento getPensamiento(JSONObject jWisdom) {
        Pensamiento wisdom = new Pensamiento();

        try {

            // Extracting title, if available
            if(!jWisdom.isNull("frase")){
                wisdom.setFrase(jWisdom.getString("frase"));
            }

            // Extracting sentence, if available
            if(!jWisdom.isNull("autor")){
                wisdom.setAutor(jWisdom.getString("autor"));
            }

            // Extracting extras, if available
            if(!jWisdom.isNull("extra")){
                wisdom.setExtra(jWisdom.getString("extra"));
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

        return wisdom;
    }

    public JSONObject loadJSON(Context context, String mode){
        if (mode.equals(FrasesParaPensarUtils.MODE_ASSET)){
            return loadJSONFromAsset(context);
        }else if (mode.equals(FrasesParaPensarUtils.MODE_SERVICE)){
            // Call Sinatra/Google AppEngine and return JSON
            return null;
        }

        return null;
    }


    public JSONObject loadJSONFromAsset(Context context) {
        String jsonStr;
        try {

            String fileTheme = "pensamientos.json";

            InputStream is = context.getAssets().open(fileTheme);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            jsonStr = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }
}
