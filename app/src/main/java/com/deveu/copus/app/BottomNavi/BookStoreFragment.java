package com.deveu.copus.app.BottomNavi;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;


import com.deveu.copus.app.Adapter.BookSliderAdapter;
import com.deveu.copus.app.Adapter.ViewHolderBooks;
import com.deveu.copus.app.Adapter.ViewHolderMyBooks;
import com.deveu.copus.app.Datas.Books;
import com.deveu.copus.app.Datas.MyBooks;
import com.deveu.copus.app.Datas.slide;
import com.deveu.copus.app.R;
import com.deveu.copus.app.ReadingSection.BookProfile;
import com.deveu.copus.app.ReadingSection.PDFActivity;
import com.deveu.copus.app.ReadingSection.SearchActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class BookStoreFragment extends Fragment {
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mRef;
    FirebaseUser currentuser;
    private TabLayout indicator_tab_book,indicator_tab_bookdown;
    private List<slide> firstSlides,upsilider;
    private ViewPager ViewPagerBookstore,viewpager_reading;
    RewardedVideoAd adforbooks;
    TextView textView19,search_edittext3;
    ImageView imageView2;


    InterstitialAd adInter;

    ConstraintLayout consforhome;
    ProgressBar progressBarHome;

    private RecyclerView rvYourReading,rvAndroid,rvİosStore,rvPerStore,rvWeb,rvGameStore,rvDesignData,rv_store_language;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

















        return inflater.inflate(R.layout.fragment_book_store,
                container, false);



    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        viewpager_reading=getView().findViewById(R.id.viewpager_reading);
        rvAndroid=getView().findViewById(R.id.rvAndroidStore);
        rvİosStore=getView().findViewById(R.id.rvİosStore);
        rvPerStore=getView().findViewById(R.id.rvPerStore);
        rvWeb=getView().findViewById(R.id.rvWeb);
        rvGameStore=getView().findViewById(R.id.rvGameStore);
        rvDesignData=getView().findViewById(R.id.rvDesignData);
        rv_store_language=getView().findViewById(R.id.rv_store_language);
        textView19=getView().findViewById(R.id.textView19);


        consforhome=getView().findViewById(R.id.consforhome);
        progressBarHome=getView().findViewById(R.id.progressBarHome);

        search_edittext3=getView().findViewById(R.id.search_edittext3);
        imageView2=getView().findViewById(R.id.imageView2);


       /* Timer timer1=new Timer();
        timer1.scheduleAtFixedRate(new BookStoreFragment(). sliderTime2(),2000,3000);*/

        MobileAds.initialize(getContext(),"ca-app-pub-1884263917338927~9693953543");
        adInter = new InterstitialAd(getContext());
        adInter.setAdUnitId("ca-app-pub-1884263917338927/4251932907");
        adInter.loadAd(new AdRequest.Builder().build());



       /* adforbooks=MobileAds.getRewardedVideoAdInstance(getContext());
        adforbooks.loadAd("ca-com.deveu.copus.app-pub-1884263917338927/7576100244",
        new AdRequest.Builder().build());*/

        currentuser= FirebaseAuth.getInstance().getCurrentUser();

        rvYourReading=getView().findViewById(R.id.rvYourReading);

        userDownloads();

        Timer timer= new Timer();
        timer.scheduleAtFixedRate(new sliderTime2(),2000,3000);
        sliderPaperAdapter();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("Books");

        SearchIntent();

        //*********************************************Android Books
        Query androidbooks = mRef.orderByChild("categoryid").equalTo("1");

        FirebaseRecyclerAdapter<Books, ViewHolderBooks> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<Books, ViewHolderBooks>(
                        Books.class, R.layout.item_book, ViewHolderBooks.class, androidbooks
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderBooks viewHolder, Books Books, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),
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
                    public ViewHolderBooks onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderBooks viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderBooks.ClickListener() {
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
        rvAndroid.setAdapter(firebaseRecyclerAdapter2);
        rvAndroid.setLayoutManager(new StaggeredGridLayoutManager(6,StaggeredGridLayoutManager.HORIZONTAL) );

        //*********************************************İos Books
        Query İosbooks = mRef.orderByChild("categoryid").equalTo("2");


        FirebaseRecyclerAdapter<Books, ViewHolderBooks> firebaseRecyclerAdapter3 =
                new FirebaseRecyclerAdapter<Books, ViewHolderBooks>(
                        Books.class, R.layout.item_book, ViewHolderBooks.class, İosbooks
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderBooks viewHolder, Books Books, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),
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
                    public ViewHolderBooks onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderBooks viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderBooks.ClickListener() {
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

                                String catid = "2";


                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), BookProfile.class);
                                intent.putExtra("catid",catid);
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
        rvİosStore.setAdapter(firebaseRecyclerAdapter3);
        rvİosStore.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL) );
        //*********************************************web Books
        Query web = mRef.orderByChild("categoryid").equalTo("3");


        FirebaseRecyclerAdapter<Books, ViewHolderBooks> firebaseRecyclerAdapter4 =
                new FirebaseRecyclerAdapter<Books, ViewHolderBooks>(
                        Books.class, R.layout.item_book, ViewHolderBooks.class, web
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderBooks viewHolder, Books Books, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),
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
                    public ViewHolderBooks onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderBooks viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderBooks.ClickListener() {
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

                                String catid = "5";


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
        rvWeb.setAdapter(firebaseRecyclerAdapter4);
        rvWeb.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL) );

        //*********************************************newpublish Books
        Query newpublish = mRef.orderByChild("categoryid").equalTo("6");


        FirebaseRecyclerAdapter<Books, ViewHolderBooks> firebaseRecyclerAdapter5 =
                new FirebaseRecyclerAdapter<Books, ViewHolderBooks>(
                        Books.class, R.layout.item_book, ViewHolderBooks.class, web
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderBooks viewHolder, Books Books, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),
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
                    public ViewHolderBooks onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderBooks viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderBooks.ClickListener() {
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


                                String catid = "6";

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
        rv_store_language.setAdapter(firebaseRecyclerAdapter5);
        rv_store_language.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL) );
        //*********************************************gamedev Books
        Query gameDev = mRef.orderByChild("categoryid").equalTo("4");


        FirebaseRecyclerAdapter<Books, ViewHolderBooks> firebaseRecyclerAdapter6 =
                new FirebaseRecyclerAdapter<Books, ViewHolderBooks>(
                        Books.class, R.layout.item_book, ViewHolderBooks.class, gameDev
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderBooks viewHolder, Books Books, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),
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
                    public ViewHolderBooks onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderBooks viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderBooks.ClickListener() {
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


                                String catid = "7";

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
        rvGameStore.setAdapter(firebaseRecyclerAdapter6);
        rvGameStore.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL) );

        //*********************************************webDev Books
        Query Database = mRef.orderByChild("categoryid").equalTo("5");
        FirebaseRecyclerAdapter<Books, ViewHolderBooks> firebaseRecyclerAdapter7 =
                new FirebaseRecyclerAdapter<Books, ViewHolderBooks>(
                        Books.class, R.layout.item_book, ViewHolderBooks.class, Database
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderBooks viewHolder, Books Books, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),
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
                    public ViewHolderBooks onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderBooks viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderBooks.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Views

                                String catid = "5";

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
        rvDesignData.setAdapter(firebaseRecyclerAdapter7);
        rvDesignData.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL) );


        //*********************************************personal Books
        Query personal = mRef.orderByChild("categoryid").equalTo("9");
        FirebaseRecyclerAdapter<Books, ViewHolderBooks> firebaseRecyclerAdapter8 =
                new FirebaseRecyclerAdapter<Books, ViewHolderBooks>(
                        Books.class, R.layout.item_book, ViewHolderBooks.class, personal
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderBooks viewHolder, Books Books, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),
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
                    public ViewHolderBooks onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderBooks viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderBooks.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Views

                                String catid = "9";

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
        rvPerStore.setAdapter(firebaseRecyclerAdapter8);
        rvPerStore.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL) );
    }



    private void SearchIntent(){

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        search_edittext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });


    }

    private void sliderPaperAdapter() {

        progressBarHome.setVisibility(View.VISIBLE);
        consforhome.setVisibility(View.GONE);
        firstSlides=new ArrayList<>();

        DatabaseReference homesliders = FirebaseDatabase.getInstance()
                .getReference("BookSlider").child("home");

        homesliders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        String title = snapshot.child("title").getValue().toString();
                        String image = snapshot.child("image").getValue().toString();
                        firstSlides.add(new slide(image,title));
                        BookSliderAdapter adapter=new BookSliderAdapter(getContext(),firstSlides);
                        viewpager_reading.setAdapter(adapter);
                        progressBarHome.setVisibility(View.GONE);
                        consforhome.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // int id = getResources().getIdentifier(name, "drawable", getContext().getPackageName());
        //Drawable drawable = getResources().getDrawable(id);

       /* firstSlides.add(new slide(R.drawable.unity_slidera,"Be gamer Developer"));
        firstSlides.add(new slide(R.drawable.unity_sliderb,"Be gamer Developer"));
        firstSlides.add(new slide(R.drawable.asfcsa,"Be gamer Developer"));
        firstSlides.add(new slide(R.drawable.html_slider,"Do you want to be Web Developer?"));
        firstSlides.add(new slide(R.drawable.python_sliderb,""));*/


    }
    class sliderTime2 extends TimerTask { //to change pics!

        @Override
        public void run() {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewpager_reading.getCurrentItem()<firstSlides.size()-1){
                        viewpager_reading.setCurrentItem(viewpager_reading.getCurrentItem()+1);
                    }else{
                        viewpager_reading.setCurrentItem(0);
                    }


                }
            });
        }
    }
    private void userDownloads() {
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("Book_Downloads").child(currentuser.getUid());
        Query androidbooks = mRef.orderByChild("bookid");



        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Long numbofBook=dataSnapshot.getChildrenCount();


                if (numbofBook==0){

                    textView19.setText("You have not any book :/");
                }else {
                    textView19.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        FirebaseRecyclerAdapter<MyBooks, ViewHolderMyBooks> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<MyBooks, ViewHolderMyBooks>(
                        MyBooks.class, R.layout.item_book, ViewHolderMyBooks.class, androidbooks
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderMyBooks viewHolder, MyBooks Books, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(),
                                Books.getBookid(), Books.getBookname(), Books.getBookphoto(),
                                Books.getPdflink()

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
                    public ViewHolderMyBooks onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderMyBooks viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolderMyBooks.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
/*
                                if(adforbooks.isLoaded()){
                                    adforbooks.show();
                                }*/

                                if(adInter.isLoaded()){
                                    adInter.show();
                                }

                                adInter.setAdListener(new AdListener(){
                                    @Override
                                    public void onAdLoaded() {
                                        super.onAdLoaded();
                                    }

                                    @Override
                                    public void onAdFailedToLoad(int i) {
                                        Toast.makeText(getActivity(), "The ad can't be loaded.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onAdOpened() {
                                        Toast.makeText(getContext(), "Respect for our labour :)) ", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onAdLeftApplication() {
                                        super.onAdLeftApplication();
                                    }

                                    @Override
                                    public void onAdClosed() {
                                        adInter.loadAd(new AdRequest.Builder().build());
                                        String openpdf = getItem(position).getPdflink();

                                        Intent intent = new Intent(getContext(), PDFActivity.class);
                                        intent.putExtra("link",openpdf);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                    }
                                });
             /*

                                adforbooks.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                                    @Override
                                    public void onRewardedVideoAdLoaded() {

                                    }

                                    @Override
                                    public void onRewardedVideoAdOpened() {

                                    }

                                    @Override
                                    public void onRewardedVideoStarted() {
                                        Toast.makeText(getContext(), "When the video is over, the book will be opened.", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onRewardedVideoAdClosed() {
                                        adforbooks.loadAd("ca-com.deveu.copus.app-pub-1884263917338927/7576100244",new AdRequest.Builder().build());
                                        Toast.makeText(getContext(), "Respect for our labour :)) ", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onRewarded(RewardItem rewardItem) {
                                        String openpdf = getItem(position).getPdflink();

                                        Intent intent = new Intent(getContext(), PDFActivity.class);
                                        intent.putExtra("link",openpdf);
                                        startActivity(intent);


                                    }

                                    @Override
                                    public void onRewardedVideoAdLeftApplication() {

                                    }

                                    @Override
                                    public void onRewardedVideoAdFailedToLoad(int i) {

                                    }

                                    @Override
                                    public void onRewardedVideoCompleted() {

                                    }
                                });


*/


                            }


                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO
                            }
                        });

                        return viewHolder;
                    }
                };
        rvYourReading.setAdapter(firebaseRecyclerAdapter2);
        rvYourReading.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
    }


}
