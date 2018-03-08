package com.example.devsmar.searchbook.Helper;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Dev Smar on 3/3/2018.
 */

public class Books {
    public ArrayList<String> multiple_authors;
    private String book_name;
    private String pulished_date;
    private int page_count;
    private String img_url;
    private String book_id;


    public Books(String book_id, String book_name, String pulished_date, int page_count, String img_url, ArrayList<String> multiple_authors) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.pulished_date = pulished_date;
        this.page_count = page_count;
        this.img_url = img_url;
        this.multiple_authors = multiple_authors;
    }


    public String getBook_name() {
        return book_name;
    }

    public String getBook_id() {
        return book_id;
    }


    public ArrayList<String> getMultiple_authors(){
        return multiple_authors;
    }

    public String getPulished_date() {
        return pulished_date;
    }



    public int getPage_count() {
        return page_count;
    }



    public String getImg_url() {
        return img_url;
    }


}
