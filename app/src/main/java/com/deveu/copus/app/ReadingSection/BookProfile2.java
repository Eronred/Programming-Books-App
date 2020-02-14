package com.deveu.copus.app.ReadingSection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import com.deveu.copus.app.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookProfile2 extends AppCompatActivity {
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mRef;
    FirebaseUser currentuser;
    private List<String> takipListesi;
    private List<Object> recyclerViewItems = new ArrayList<>();
    private RecyclerView rv_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile2);



/*

        currentuser= FirebaseAuth.getInstance().getCurrentUser();

        rv_user=findViewById(R.id.rv_user);

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("Book_Downloads").child(currentuser.getUid());

        //*********************************************Android Books

        Query androidbooks = mRef.orderByChild("bookid");

        FirebaseRecyclerAdapter<Books, ViewHolderMyBooks> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<Books, ViewHolderMyBooks>(
                        Books.class, R.layout.item_book, ViewHolderMyBooks.class, androidbooks
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderMyBooks viewHolder, Books Books, int position) {

                        viewHolder.setDetails(
                                BookProfile2.this.getApplicationContext(),
                                Books.getBookid(), Books.getBookname(), Books.getBookdescription(),
                                Books.getBookphoto(), Books.getViewcount(), Books.getDowncount(),
                                Books.getLikecount(), Books.getCategoryid(), Books.getCount(),
                                Books.getBookpage(), Books.getAuthorname(), Books.getPdflink()

                        );

                    }

                    @Override
                    public ViewHolderMyBooks onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderMyBooks viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderMyBooks.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Views


                                //get data from views
                                String mauthorname = getItem(position).getAuthorname();
                                String mBookid = getItem(position).getBookid();
                                String mBookName = getItem(position).getBookname();
                                String mBookImage = getItem(position).getBookphoto();
                                String mBookdescription = getItem(position).getBookdescription();
                                long mCount = getItem(position).getCount();
                                long mDownCount = getItem(position).getDowncount();
                                long mViewCount = getItem(position).getViewcount();
                                long mLikeCount = getItem(position).getLikecount();
                                long mBookPage = getItem(position).getBookpage();
                                String mCategoryid = getItem(position).getCategoryid();
                                String pdfLink = getItem(position).getPdflink();


                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), BookProfile.class);
                                intent.putExtra("authorname", mauthorname);
                                intent.putExtra("bookname", mBookName);
                                intent.putExtra("bookphoto", mBookImage);
                                intent.putExtra("bookdescription", mBookdescription);
                                intent.putExtra("count", mCount);
                                intent.putExtra("likecount", mLikeCount);
                                intent.putExtra("bookid", mBookid);
                                intent.putExtra("bookpage", mBookPage);
                                intent.putExtra("categoryid", mCategoryid);
                                intent.putExtra("downcount", mDownCount);
                                intent.putExtra("viewcount", mViewCount);
                                intent.putExtra("pdflink", pdfLink);

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
        rv_user.setAdapter(firebaseRecyclerAdapter2);
        rv_user.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));


    }*/


    }}
