package com.pocketworks.taxi.taxiproto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Vasco on 29/06/2016.
 */

public final class JSONComunications {

    //JSON Node Post Tags
    private static final String TAG_REFERENCE = "reference";
    private static final String TAG_ADDRESS = "pickup_address";
    private static final String TAG_TIME = "pickup_time";

    private static final String TAG_SUCESS = "success";
    private static final String TAG_ERROR = "error_message";


    // constructor
    public JSONComunications() {

    }

    public JSONObject getJSONFromUrl(String uri) {
//TODO: getJSONObject in JSON Comunications
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while((json = bufferedReader.readLine())!= null){
                sb.append(json+"\n");
            }

            //TODO: check if should use  trim() or not....
            String stream = sb.toString();

            JSONObject res = new JSONObject(stream);

            return res;

        }catch(Exception e){
            return null;
        }
        }


    public void getBookResponse(JSONObject response) {
        // I am not doing anything with the result from this method
        // just to show you some code for JSON files... (but we dont have WS working)
        try {
            if(response.getInt(TAG_SUCESS)==1){
                response.getString(TAG_ADDRESS);
                response.getString(TAG_REFERENCE);
                String date_string = response.getString(TAG_TIME);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date date = (Date) dateFormat.parse(date_string);
            }
            else
            {
                response.getString(TAG_ERROR);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}