package com.example.devsmar.searchbook.loader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.devsmar.searchbook.Helper.Books;
import com.example.devsmar.searchbook.Helper.DataParser;

import java.util.ArrayList;

/**
 * Created by Dev Smar on 3/4/2018.
 */

public class BooksLoader extends AsyncTaskLoader<ArrayList<Books>> {
    private String url;

    public BooksLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public ArrayList<Books> loadInBackground() {
        if (url == null){
            return null;
        }

        ArrayList<Books> resultedList = DataParser.jsonParser(url);
        return resultedList;
    }


}
