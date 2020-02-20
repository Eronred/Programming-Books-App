package com.deveu.copus.app.ReadingSection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;


import com.deveu.copus.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Comment_activity extends AppCompatActivity {


    FirebaseUser mevcutKullanici;
    private RatingBar rating_item;
    private EditText edittext_review;
    private Button add_review;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_activity);

        rating_item=findViewById(R.id.rating_item);
        edittext_review=findViewById(R.id.edittext_review);
        add_review=findViewById(R.id.add_review);



        String bookid = getIntent().getStringExtra("bookid");


        DatabaseReference refrate = FirebaseDatabase.getInstance().getReference("BooksReviews")
                .child(bookid);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();




        findViewById(R.id.imageView41).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String userreview = edittext_review.getText().toString();
                float rating = (rating_item.getRating());
                String ratingg= Float.toString(rating);
                if(rating!= 0.0 && !TextUtils.isEmpty(userreview)){
                    HashMap<String,Object> hashMap = new HashMap<>();

                    String currentTime = new SimpleDateFormat("HH:mm:ss",
                            Locale.getDefault()).format(new Date());
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy",
                            Locale.getDefault()).format(new Date());
                    String reviewid = refrate.push().getKey();
                    hashMap.put("review", userreview);
                    hashMap.put("date",currentDate +" "+currentTime);
                    hashMap.put("usid",mevcutKullanici.getUid());
                    hashMap.put("reviewid", reviewid);
                    hashMap.put("bookid",bookid);
                    hashMap.put("rating", ratingg);
                    refrate.child(reviewid).setValue(hashMap);
                    edittext_review.setText("");
                    rating_item.setRating(0);
                    finish();



                }else{
                    Toast.makeText(Comment_activity.this, "Please, fill required areas!", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
