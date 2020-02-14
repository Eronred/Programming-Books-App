package com.deveu.copus.app.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.deveu.copus.app.R;
import com.squareup.picasso.Picasso;

public class ViewHolderBooks extends RecyclerView.ViewHolder {

    View mView;
    public RatingBar rtw;
    public ViewHolderBooks(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
        rtw = itemView.findViewById(R.id.ratingBarItem);

        //item click

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });


        //item long click

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }


    public void setDetails (Context ctx, String bookid, String bookname, String bookdescription, String bookphoto, long viewcount, long downcount, long likecount, String categoryid, long count, long bookpage, String authorname, String pdflink

    ){
        TextView mBookName = mView.findViewById(R.id.name_book);
        ImageView mBookImage = mView.findViewById(R.id.commentuserpic);

        mBookName.setText(bookname);
        Picasso.get().load(bookphoto).resize(500,600)
                .into(mBookImage);




    }

    private ClickListener mClickListener;


    //interface to send callbacks

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ClickListener clickListener){

        mClickListener = clickListener;

    }
}
