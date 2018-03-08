package com.example.devsmar.searchbook.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.devsmar.searchbook.Helper.DataParser;

import java.util.HashMap;

/**
 * Created by Dev Smar on 3/7/2018.
 */

public class BookDetailsLoafer extends AsyncTaskLoader<HashMap<Integer, String>> {

    public String detail_url = "";

    public BookDetailsLoafer(Context context, String detail_url) {
        super(context);
        this.detail_url = detail_url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public HashMap<Integer, String> loadInBackground() {
        if (detail_url == null){
            return null;
        }

       HashMap<Integer, String> description_text = DataParser.getDescription(detail_url);
        return description_text;
    }
}
