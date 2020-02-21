package com.deveu.copus.app.ReadingSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

import com.deveu.copus.app.Adapter.ReviewAdapter;
import com.deveu.copus.app.Adapter.ViewHolderBooks;
import com.deveu.copus.app.BuildConfig;
import com.deveu.copus.app.Datas.Books;
import com.deveu.copus.app.Datas.Reviews;
import com.deveu.copus.app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookProfile extends AppCompatActivity {
    private TextView like_number, down_number, yazar_name, book_name, description_book,page_num,view_num;
    private ImageView bookimage,like_button,add_comment;
    private Button download_book;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser fuser;
    private DatabaseReference userRef;
    private DatabaseReference likeRef;
    private DatabaseReference downRef;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mRef;
    private DatabaseReference fRef;
    private RatingBar ratingBar;
    TextView messeage_reniewNum,messeage_reniew;
    FirebaseUser mevcutKullanici;
    private ReviewAdapter gonderiAdapter;
    private List<Reviews> gonderiListeleri;
    private RecyclerView recyclerView,recyclerView6;


    String pdflink;
    RewardedVideoAd adforbooks;
    //InterstitialAd adInter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);
        messeage_reniewNum = findViewById(R.id.messeage_reniewNum);
        messeage_reniew = findViewById(R.id.messeage_reniew);

        like_number = findViewById(R.id.like_number);
        down_number = findViewById(R.id.down_number);
        yazar_name = findViewById(R.id.yazar_name);
        book_name = findViewById(R.id.book_name);
        description_book = findViewById(R.id.description_book);
        page_num = findViewById(R.id.page_number);
        view_num = findViewById(R.id.view_num);
        bookimage = findViewById(R.id.image_book);
        like_button = findViewById(R.id.like_button);
        download_book = findViewById(R.id.download_book);
        ratingBar = findViewById(R.id.ratingBar);
        add_comment = findViewById(R.id.add_comment);
        recyclerView = findViewById(R.id.rv_comments);
        recyclerView6 = findViewById(R.id.recyclerView6);



        MobileAds.initialize(BookProfile.this,"ca-app-pub-3739839397874462~1984963841");
       /* adInter = new InterstitialAd(this);
        adInter.setAdUnitId("ca-app-pub-1884263917338927/4251932907");
        adInter.loadAd(new AdRequest.Builder().build());*/
       /* MobileAds.initialize(this,"ca-com.deveu.copus.app-pub-1884263917338927~9693953543");
*/
        adforbooks=MobileAds.getRewardedVideoAdInstance(this);
        adforbooks.loadAd("ca-app-pub-3739839397874462/3181467773",new AdRequest.Builder().build());

        findViewById(R.id.share_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share on"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //required id's
        String bid = getIntent().getStringExtra("bookid");

        String cid = getIntent().getStringExtra("categoryid");
        //count=bookid
        Long bcount = getIntent().getLongExtra("count",0);
        //other incomings..
        String bname = getIntent().getStringExtra("bookname");
        pdflink = getIntent().getStringExtra("pdflink");
        String aname = getIntent().getStringExtra("authorname");
        String bimage = getIntent().getStringExtra("bookphoto");
        String bdesc = getIntent().getStringExtra("bookdescription");
        Long

                bviewcount = getIntent().getLongExtra("viewcount",0);
        Long bpage = getIntent().getLongExtra("bookpage",0);
        Long blikecount = getIntent().getLongExtra("likecount",0);
        Long bdowncount = getIntent().getLongExtra("downcount",0);
        //put incoming data to variables
        yazar_name.setText(aname);
        book_name.setText(bname);
        description_book.setText(bdesc);
        fuser = FirebaseAuth.getInstance().getCurrentUser();






        final String userid = fuser.getUid();

        DatabaseReference refrate = FirebaseDatabase.getInstance().getReference("Book_Rates")
                .child(bid);


        page_num.setText(String.valueOf(bpage));
        Picasso.get().load(bimage).into(bookimage);


        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookProfile.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        gonderiListeleri = new ArrayList<>();
        gonderiAdapter = new ReviewAdapter(BookProfile.this, gonderiListeleri);
        recyclerView.setAdapter(gonderiAdapter);
        gonderileriOku(bid);



        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent=new Intent(BookProfile.this,Comment_activity.class);
                ıntent.putExtra("bookid",bid);
                startActivity(ıntent);
            }
        });





        //Recommend methot
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("Books");
        RecommendSection(cid);



        firebaseDatabase = FirebaseDatabase.getInstance();
        fRef = firebaseDatabase.getReference("Books").child(bid);
        DatabaseReference foryes;
        foryes = FirebaseDatabase.getInstance().getReference("Books").child(bid);
        HashMap hashMap = new HashMap();
        hashMap.put("viewcount",bviewcount+1);
        foryes.updateChildren(hashMap);


        userRef = firebaseDatabase.getReference("Users").child(fuser.getUid());
        likeRef = firebaseDatabase.getReference("Book_Likes").child(fuser.getUid()).child(bid);
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    like_button.setVisibility(View.GONE);
                }
                else{
                    like_button.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hashMap1 = new HashMap();
                hashMap1.put("isLiked","yes");
                hashMap1.put("bookid",bid);
                hashMap1.put("userid",userid);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Book_Likes");
                databaseReference.child(fuser.getUid()).child(bid).setValue(hashMap1);
                DatabaseReference forlike;
                forlike = FirebaseDatabase.getInstance().getReference("Books").child(bid);
                HashMap hashMap3 = new HashMap();
                hashMap3.put("likecount",blikecount+1);
                forlike.updateChildren(hashMap3);
                like_button.setVisibility(View.GONE);
            }
        });
        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Long likecount1 = (Long) dataSnapshot.child("likecount").getValue();
                    Long downcount1 = (Long) dataSnapshot.child("downcount").getValue();
                    Long viewcount1 = (Long) dataSnapshot.child("viewcount").getValue();
                    view_num.setText(viewcount1.toString());
                    like_number.setText(likecount1.toString());
                    down_number.setText(downcount1.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        download_book.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
             /*   */
             //  DownloadManager downloadManager = (DownloadManager)getApplicationContext(),getSys


/*

                if(adInter.isLoaded()){
                    adInter.show();
                }
*/



                if (adforbooks == null || !adforbooks.isLoaded()){
                    Toast.makeText(BookProfile.this, "Please, control your internet connection!", Toast.LENGTH_SHORT).show();
                }else{
                    adforbooks.show();
                }



        /*        adInter.setAdListener(new AdListener(){
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        Toast.makeText(BookProfile.this, "The ad can't be loaded.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdOpened() {

                        Toast.makeText(BookProfile.this, "The book is downloading...", Toast.LENGTH_SHORT).show();
                        final String myid = fuser.getUid();
                        DatabaseReference formybooks = FirebaseDatabase.getInstance().getReference("Book_Downloads")
                                .child(myid).child(bid);
                        formybooks.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Toast.makeText(BookProfile.this, "You have already the book in your library.", Toast.LENGTH_SHORT).show();
                                }else{

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        downFile(getApplicationContext(),bname,".pdf",DIRECTORY_DOWNLOADS,pdflink);
                        DatabaseReference fordown;
                        fordown = FirebaseDatabase.getInstance().getReference("Books").child(bid);
                        HashMap hashMap3 = new HashMap();
                        String bdcountstring = down_number.getText().toString();
                        Long bdcount = Long.parseLong(bdcountstring);
                        hashMap3.put("downcount",bdcount+1);
                        fordown.updateChildren(hashMap3);

                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                    }

                    @Override
                    public void onAdClosed() {
                        adInter.loadAd(new AdRequest.Builder().build());



                        final String myid = fuser.getUid();
                        DatabaseReference formybooks = FirebaseDatabase.getInstance().getReference("Book_Downloads")
                                .child(myid).child(bid);
                        formybooks.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                }else{
                                    HashMap hashMap1 = new HashMap();
                                    hashMap1.put("isdown","yes");
                                    hashMap1.put("bookid",bid);
                                    hashMap1.put("userid",userid);
                                    hashMap1.put("bookname",book_name.getText().toString());
                                    hashMap1.put("bookphoto",bimage);
                                    hashMap1.put("pdflink",pdflink);
                                    formybooks.setValue(hashMap1);

                                    Toast.makeText(BookProfile.this, "The book added to your library", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Intent intent = new Intent(BookProfile.this, PDFActivity.class);
                        intent.putExtra("link",pdflink);
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
*/





                adforbooks.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                    @Override
                    public void onRewardedVideoAdLoaded() {

                    }

                    @Override
                    public void onRewardedVideoAdOpened() {
                        Toast.makeText(BookProfile.this, "When the video is over, the book will be downloaded.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRewardedVideoStarted() {
                        final String myid = fuser.getUid();
                        DatabaseReference formybooks = FirebaseDatabase.getInstance().getReference("Book_Downloads")
                                .child(myid).child(bid);
                        formybooks.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Toast.makeText(BookProfile.this, "You have already the book in your library.", Toast.LENGTH_SHORT).show();
                                }else{

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onRewardedVideoAdClosed() {
                        adforbooks.loadAd("ca-app-pub-3739839397874462/3181467773",new AdRequest.Builder().build());
                        Toast.makeText(BookProfile.this, "Respect for our labour :)) ", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onRewarded(RewardItem rewardItem) {
                        final String myid = fuser.getUid();
                        DatabaseReference formybooks = FirebaseDatabase.getInstance().getReference("Book_Downloads")
                                .child(myid).child(bid);
                        formybooks.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                }else{
                                    HashMap hashMap1 = new HashMap();
                                    hashMap1.put("isdown","yes");
                                    hashMap1.put("bookid",bid);
                                    hashMap1.put("userid",userid);
                                    hashMap1.put("bookname",book_name.getText().toString());
                                    hashMap1.put("bookphoto",bimage);
                                    hashMap1.put("pdflink",pdflink);
                                   formybooks.setValue(hashMap1);

                                    Toast.makeText(BookProfile.this, "The book added to your library", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        downFile(getApplicationContext(),bname,".pdf",DIRECTORY_DOWNLOADS,pdflink);
                        Intent intent = new Intent(BookProfile.this, PDFActivity.class);
                        intent.putExtra("link",pdflink);
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
                        DatabaseReference fordown;
                        fordown = FirebaseDatabase.getInstance().getReference("Books").child(bid);
                        HashMap hashMap3 = new HashMap();
                        String bdcountstring = down_number.getText().toString();
                        Long bdcount = Long.parseLong(bdcountstring);
                        hashMap3.put("downcount",bdcount+1);
                        fordown.updateChildren(hashMap3);

                    }
                });
            }
        });

        ratingRate(bid);

    }



    private void ratingRate(String bookid){
        DatabaseReference rtdb  = FirebaseDatabase.getInstance().getReference("BooksReviews")
                .child(bookid);
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

                        ratingBar.setRating(rtx);
                    }
                }
                else{
                    ratingBar.setRating(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void RecommendSection(String catid) {
        //*********************************************Android Books



        Query androidbooks = mRef.orderByChild("categoryid").equalTo(catid);


        FirebaseRecyclerAdapter<Books, ViewHolderBooks> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<Books, ViewHolderBooks>(
                        Books.class, R.layout.item_book, ViewHolderBooks.class, androidbooks
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderBooks viewHolder, Books Books, int position) {

                            viewHolder.setDetails(BookProfile.this.getApplicationContext(),
                                    Books.getBookid(), Books.getBookname(), Books.getBookdescription(),
                                    Books.getBookphoto(), Books.getViewcount(), Books.getDowncount(),
                                    Books.getLikecount(), Books.getCategoryid(), Books.getCount(),
                                    Books.getBookpage(), Books.getAuthorname(), Books.getPdflink()

                            );
                            DatabaseReference rtdb = FirebaseDatabase.getInstance().getReference("BooksReviews")
                                    .child(Books.getBookid());

                            rtdb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        long datacount = dataSnapshot.getChildrenCount();
                                        float sum = 0;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                                            String rating = map.get("rating");

                                            float pValue = Float.parseFloat(rating);
                                            sum += pValue;

                                            float rtx = sum / (float) datacount;

                                            viewHolder.rtw.setRating(rtx);
                                        }
                                    } else {
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
        recyclerView6.setAdapter(firebaseRecyclerAdapter2);
        recyclerView6.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL) );
    }

    public void downFile(Context context, String fileName, String fileExtension, String directory,String url){
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,directory,fileName+fileExtension);
        downloadManager.enqueue(request);
        /*Intent intent = new Intent();
        intent.setType(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
*/


    }




    private void gonderileriOku(String genid){
        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("BooksReviews").child(genid);

        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gonderiListeleri.clear();

                Long numComment=dataSnapshot.getChildrenCount();

                messeage_reniewNum.setText(numComment.toString());


                if (numComment==0){
                    messeage_reniew.setText("There is no review! Make one.");

                }else {
                    messeage_reniew.setVisibility(View.GONE);
                }




                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Reviews gonderi = snapshot.getValue(Reviews.class);
                    gonderiListeleri.add(gonderi);
                }
                gonderiAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        adforbooks.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        adforbooks.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        adforbooks.destroy(this);
        super.onDestroy();
    }

/*
    @Override
    protected void onResume() {
        adforbooks.resume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        adforbooks.destroy(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        adforbooks.pause(this);
        super.onPause();
    }*/
}


