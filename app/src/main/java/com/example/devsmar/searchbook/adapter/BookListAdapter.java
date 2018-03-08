package com.example.devsmar.searchbook.adapter;

import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.devsmar.searchbook.Helper.Books;
import com.example.devsmar.searchbook.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dev Smar on 3/3/2018.
 */

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookHolder>{

    private final BookClickListener bookClickListener;

    public interface BookClickListener{
        void onLlickListener(int position);
        void onLongLlickListener(int position);
    }

    private ArrayList<Books> booksArrayList;
    Context context;

    public BookListAdapter(ArrayList<Books> booksList, Context context, BookClickListener bookClickListener) {
        this.booksArrayList = booksList;
        this.context = context;
        this.bookClickListener = bookClickListener;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_for_book_list_rc,parent,false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Books books = booksArrayList.get(position);

        String bookNameObj = books.getBook_name();
        ArrayList authorNameObj = books.getMultiple_authors();

        String str = Arrays.toString(authorNameObj.toArray());

        int currentPagesObj = books.getPage_count();
        String publishedDateObj = books.getPulished_date();

        Glide.with(context)
                .load(books.getImg_url())
                .into(holder.myimageView);

        holder.bookName.setText(bookNameObj);
        holder.authorName.setText(str);
        holder.pageCount.setText(String.valueOf("Page : "+currentPagesObj));
        holder.publishedDate.setText(publishedDateObj);
    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView bookName, authorName, pageCount, publishedDate;
        public ImageView myimageView;
        public BookHolder(View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.txt_book_name);
            authorName = itemView.findViewById(R.id.txt_book_author);
            pageCount = itemView.findViewById(R.id.txt_pages_count);
            publishedDate = itemView.findViewById(R.id.txt_published_date);
            myimageView = itemView.findViewById(R.id.img_tbn_booklist);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            bookClickListener.onLlickListener(position);
        }
    }

}
