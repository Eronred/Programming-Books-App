package com.deveu.copus.app.ReadingSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.deveu.copus.app.Adapter.ViewHolderBookSearch;
import com.deveu.copus.app.Datas.Books;
import com.deveu.copus.app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    EditText search_books;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.recy_view_arama);


        search_books = findViewById(R.id.edt_arama_bar);
        search_books.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchBooks(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    private void searchBooks(String s) {
        FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mfirebaseDatabase.getReference("Books");
        Query androidbooks = mRef.orderByChild("search").startAt(s).endAt(s+"\uf8ff");

        FirebaseRecyclerAdapter<Books, ViewHolderBookSearch> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<Books, ViewHolderBookSearch>(
                        Books.class, R.layout.item_bookseach, ViewHolderBookSearch.class, androidbooks
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderBookSearch viewHolder, Books Books, int position) {

                        viewHolder.setDetails(SearchActivity.this,
                                Books.getBookid(),Books.getBookname(),Books.getBookdescription(),
                                Books.getBookphoto(),Books.getViewcount(),Books.getDowncount(),
                                Books.getLikecount(),Books.getCategoryid(),Books.getCount(),
                                Books.getBookpage(),Books.getAuthorname(),Books.getPdflink()

                        );

                        DatabaseReference rtdb  = FirebaseDatabase.getInstance().getReference("BooksReviews")
                                .child(Books.getBookid());

                        rtdb.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    long datacount = dataSnapshot.getChildrenCount();
                                    float sum = 0;
                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        Map<String,String> map = (Map<String,String>) snapshot.getValue();
                                        String rating = map.get("rating");

                                        float pValue = Float.parseFloat(rating);
                                        sum+=pValue;

                                        float rtx = sum / (float) datacount;

                                        viewHolder.rtw.setRating(rtx);
                                    }
                                }else{
                                    viewHolder.rtw.setRating(0);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public ViewHolderBookSearch onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderBookSearch viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderBookSearch.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Views


                                //get data from views
                                String mauthorname=getItem(position).getAuthorname();
                                String mBookid = getItem(position).getBookid();
                                String mBookName = getItem(position).getBookname();
                                String mBookImage =getItem(position).getBookphoto();
                                String mBookdescription = getItem(position).getBookdescription();
                                long mCount = getItem(position).getCount();
                                long mDownCount = getItem(position).getDowncount();
                                long mViewCount = getItem(position).getViewcount();
                                long mLikeCount = getItem(position).getLikecount();
                                long mBookPage = getItem(position).getBookpage();
                                String mCategoryid = getItem(position).getCategoryid();
                                String pdfLink = getItem(position).getPdflink();
                                String catid = "1";



                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), BookProfile.class);
                                intent.putExtra("authorname", mauthorname);
                                intent.putExtra("bookname", mBookName);
                                intent.putExtra("bookphoto",mBookImage);
                                intent.putExtra("bookdescription",mBookdescription);
                                intent.putExtra("count", mCount);
                                intent.putExtra("likecount", mLikeCount);
                                intent.putExtra("bookid",mBookid);
                                intent.putExtra("bookpage",mBookPage);
                                intent.putExtra("categoryid", mCategoryid);
                                intent.putExtra("downcount", mDownCount);
                                intent.putExtra("viewcount", mViewCount);
                                intent.putExtra("pdflink", pdfLink);
                                intent.putExtra("catid",catid);

                                startActivity(intent);
                            }


                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO
                            }
                        });

                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter2);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL) );

    }


}
