package com.pocketworks.taxi.taxiproto;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class FavouriteBookTask extends AsyncTask<String, Void, List<JSONObject>> {
        private ProgressDialog pDialog;
        private List<Favourite> favourites;

    FavouritesFragment container;

    public FavouriteBookTask(FavouritesFragment f) {
        this.container = f;
    }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(this.container.getActivity());
            pDialog.setMessage("Test Progress (press cancel) ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected List<JSONObject> doInBackground(String... arg) {

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("api/book");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("company_id", "12345"));
                nameValuePairs.add(new BasicNameValuePair("pickup_address", "Hi"));
                nameValuePairs.add(new BasicNameValuePair("pickup_time", "30/06/2016 12:17"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                JSONObject res = getJSONFromUrl("api/book");

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

        }



        protected void onPostExecute(JSONObject arg) {
            Favourite fav = new Favourite();

            pDialog.dismiss();

        }

    public JSONObject getJSONFromUrl(String uri) {

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
    }
