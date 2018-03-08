package com.example.devsmar.searchbook.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.devsmar.searchbook.R;
import com.example.devsmar.searchbook.loader.BookDetailsLoafer;

import java.util.HashMap;

public class BookReadActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<HashMap<Integer, String>> {

    public static final int DESCRIPTION_LOADER_ID = 201;
    String book_id, book_name;
    String description_org_url;
    TextView textView;
    TextView empty_text;
    ImageView image;
    ImageView empty_img;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_read);

        Intent intent = getIntent();
        book_id = intent.getStringExtra("id");
        book_name = intent.getStringExtra("book_name");

        description_org_url = getString(R.string.description_org_url);
        textView = findViewById(R.id.content);
        empty_text = findViewById(R.id.empty_text_description);

        image = findViewById(R.id.description_image);
        empty_img = findViewById(R.id.empty_img_description);

        empty_text.setVisibility(View.GONE);
        empty_img.setVisibility(View.GONE);
        init();
        getSupportLoaderManager().initLoader(DESCRIPTION_LOADER_ID, null, this);
    }

    private void init() {
        progressBar = findViewById(R.id.progressBarDescription);
        Toolbar toolbar = findViewById(R.id.toolbarDescription);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public Loader<HashMap<Integer, String>> onCreateLoader(int id, Bundle args) {
        String url = description_org_url.toString() + book_id.toString();
        Log.i("fuck", url);
        return new BookDetailsLoafer(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<HashMap<Integer, String>> loader, HashMap<Integer, String> data) {
        if (data != null) {

            String img_url = data.get(1);
            String description = data.get(0);

            if (!img_url.isEmpty() && !description.isEmpty()) {
                Glide.with(getApplicationContext())
                        .load(img_url)
                        .fitCenter()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(image);

                String b = description.replaceAll("<b>", "");
                String b2 = b.replaceAll("</b>", "");
                String p = b2.replaceAll("<p>", "");
                String p2 = p.replaceAll("</p>", "");
                String i = p2.replaceAll("<i>", "");
                String i2 = i.replaceAll("</i>", "");
                String li = i2.replaceAll("<li>", "");
                String li2 = li.replaceAll("</li>", "");
                String ui = li2.replaceAll("<ui>", "");
                String ui2 = ui.replaceAll("</ui>", "");
                String ul = ui2.replaceAll("<ul>", "");
                String ul2 = ul.replaceAll("</ul>", "");
                String br = ul2.replaceAll("<br>", "");
                String br2 = br.replaceAll("</br>", "");
                textView.setText(br2);
            } else {
                progressBar.setVisibility(View.GONE);
                empty_img.setVisibility(View.VISIBLE);
                empty_text.setVisibility(View.VISIBLE);
                empty_img.setBackgroundResource(R.drawable.ic_error_black_36dp);
                empty_text.setText("Invalid Data");
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<HashMap<Integer, String>> loader) {
        textView.setText("");
        image.setImageResource(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings2) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
