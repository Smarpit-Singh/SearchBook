package com.example.devsmar.searchbook.Helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dev Smar on 3/4/2018.
 */

public class DataParser {

    private static String VOLUME_INFO = "volumeInfo";
    private static String DESCRIPTION = "description";
    private static String IMAGE_LINK = "imageLinks";
    private static String LARGE = "large";
    private static String ITEM = "items";
    private static String ID = "id";
    private static String TITLE = "title";
    private static String PUBLISHED_DATE = "publishedDate";
    private static String PAGE_COUNT = "pageCount";
    private static String SMALL_THUMBNAIL = "smallThumbnail";
    private static String AUTHORS = "authors";
    private static String NO_AUTHORS = "Undentified";

    public DataParser() {

    }

    public static ArrayList<Books> jsonParser(String url) {
        URL url1 = createUrl(url);
        String result = "";
        ArrayList<Books> booksArrayList = null;

        try {
            result = makeHTTPConnection(url1);
            booksArrayList = extractBooksFromJson(result);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return booksArrayList;
    }

    public static HashMap<Integer,String> getDescription(String url){
        URL url1 = createUrl(url);
        String result = "";
        HashMap<Integer, String> org_result = new HashMap<Integer, String>();

        try {
            result = makeHTTPConnection(url1);
            Log.i("fuck", result);
            org_result = getDescriptionFromJson(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return org_result;
    }

    private static HashMap<Integer, String> getDescriptionFromJson(String result) {
        HashMap<Integer, String> hmap = new HashMap<Integer, String>();
        try {
            JSONObject base = new JSONObject(result);
            JSONObject volumeInfo = base.getJSONObject(VOLUME_INFO);
            String description_text;
             description_text = volumeInfo.optString(DESCRIPTION);
            JSONObject image_obj = volumeInfo.getJSONObject(IMAGE_LINK);
            String image_link;
            image_link = image_obj.optString(LARGE);
            hmap.put(0,description_text);
            hmap.put(1,image_link);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hmap;
    }

    private static ArrayList<Books> extractBooksFromJson(String result) {
        ArrayList<Books> arrayList = new ArrayList<>();

        try {
            JSONObject base = new JSONObject(result);
            JSONArray items = base.getJSONArray(ITEM);

            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                String id = obj.getString(ID);
                JSONObject volume = obj.getJSONObject(VOLUME_INFO);
                String bookName = volume.getString(TITLE);

                String date = volume.getString(PUBLISHED_DATE);
                int pages = volume.getInt(PAGE_COUNT);

                JSONObject img = volume.getJSONObject(IMAGE_LINK);
                String image = img.getString(SMALL_THUMBNAIL);

                JSONArray authors = volume.optJSONArray(AUTHORS);
                ArrayList<String> authorsNames = new ArrayList<>();


                if (authors != null) {
                    for (int j = 0; j < authors.length(); j++) {
                        authorsNames.add(authors.getString(j));
                    }
                }else {
                    authorsNames.add(NO_AUTHORS);
                }

                Books books = new Books(id,bookName, date, pages, image, authorsNames);
                arrayList.add(books);
                Log.i("HaHaHa",id + " " +bookName+" "+" "+date+" "+" "+pages+" "+" "+image+" "+" "+authorsNames);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private static String makeHTTPConnection(URL url1) throws IOException {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        String response = "";
        try {
            httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            response = readFromStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return response;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder out = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readline = bufferedReader.readLine();
            while (readline != null) {
                out.append(readline);
                readline = bufferedReader.readLine();
            }
        }
        return out.toString();
    }

    private static URL createUrl(String url) {
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url1;
    }

}
