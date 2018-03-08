package com.example.devsmar.searchbook.activity;



import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devsmar.searchbook.Helper.Books;
import com.example.devsmar.searchbook.R;
import com.example.devsmar.searchbook.adapter.BookListAdapter;
import com.example.devsmar.searchbook.loader.BooksLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Books>>, BookListAdapter.BookClickListener {

    public static final int LOADER_ID = 101;
    public static String finalUrl = "";


    RecyclerView recyclerView;
    BookListAdapter bookListAdapter;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    ArrayList<Books> arrayList = new ArrayList<>();
    EditText searchEditText;
    ImageView searchButton, empty_img;
    TextView emptyText;
    String retrieveUrl, BOOK_LISTING_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("lifecycle","onCreate invoked");
        init();

        BOOK_LISTING_URL = getApplicationContext().getString(R.string.book_listing_url);

        empty_img = findViewById(R.id.empty_img);
        empty_img.setBackgroundResource(R.drawable.ic_error_black_36dp);

        if (savedInstanceState != null){
            retrieveUrl = savedInstanceState.getString("url");
            searchBook();
        }
    }

    private void init() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        bookListAdapter = new BookListAdapter(arrayList, this, this);
        recyclerView.setAdapter(bookListAdapter);

        searchEditText = findViewById(R.id.search_book);
        searchButton = findViewById(R.id.img_btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBook();
            }
        });
        emptyText = findViewById(R.id.emptyText);
        emptyText.setText(getString(R.string.empty_string));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.toolbar_title));
        setSupportActionBar(toolbar);

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    searchBook();
                    return true;
                }
                return false;
            }
        });
    }

    private void searchBook() {
        String text = searchEditText.getText().toString();
        finalUrl = BOOK_LISTING_URL + text +getString(R.string.max_result);
        srartLoader(finalUrl);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void srartLoader(String finalUrl) {
        if (finalUrl != null && finalUrl != "" && finalUrl != " "){
            emptyText.setVisibility(View.GONE);
            empty_img.setVisibility(View.GONE);
            if (isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                getSupportLoaderManager().destroyLoader(LOADER_ID);
                arrayList.clear();
                bookListAdapter.notifyItemRangeRemoved(0, arrayList.size());

                getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            }else {
                empty_img.setVisibility(View.VISIBLE);
               // empty_img.setBackgroundResource(R.drawable.ic_cloud_off_black_36dp);
                emptyText.setText("No network.");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("lifecycle","onSavedInstance invoked");

        savedInstanceState.putString("url", finalUrl);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("lifecycle","onRestoreInstanceStart invoked");
        retrieveUrl = savedInstanceState.getString("url");
    }

    @NonNull
    @Override
    public Loader<ArrayList<Books>> onCreateLoader(int id, @Nullable Bundle args) {
        if (retrieveUrl == null) {
            return new BooksLoader(this, finalUrl);
        }
        else {
            return new BooksLoader(this, retrieveUrl);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Books>> loader, ArrayList<Books> data) {
        if (data != null && !data.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            updateUi(data);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText("No book found");
            empty_img.setVisibility(View.VISIBLE);
        }
    }

    private void updateUi(ArrayList<Books> data) {
        arrayList.addAll(data);
        bookListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Books>> loader) {
        arrayList.clear();
        bookListAdapter.notifyItemRangeRemoved(0, arrayList.size());
    }

    @Override
    public void onLlickListener(int position) {
        Intent intent = new Intent(this,BookReadActivity.class);
        String id = arrayList.get(position).getBook_id();
        String name = arrayList.get(position).getBook_name();
        intent.putExtra("id",id);
        intent.putExtra("book_name",name);
        startActivity(intent);
    }

    @Override
    public void onLongLlickListener(int position) {

    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
        getSupportLoaderManager().destroyLoader(LOADER_ID);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        arrayList.clear();
        bookListAdapter.notifyItemRangeRemoved(0, arrayList.size());
    }

    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked");
    }
}
