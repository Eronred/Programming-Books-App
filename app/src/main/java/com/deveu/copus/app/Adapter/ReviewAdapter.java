package com.deveu.copus.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deveu.copus.app.Datas.Reviews;
import com.deveu.copus.app.Datas.Users;
import com.deveu.copus.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.viewHolder> {


    public Context mContext;
    public List<Reviews> mGönderi;
    private FirebaseUser mevcutFirebaseUser;

    public ReviewAdapter(Context mContext, List<Reviews> mGönderi) {
        this.mContext = mContext;
        this.mGönderi = mGönderi;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public ImageView profil_resmi;
        public TextView txt_kullanici_adi, txt_yorumlar,txt_date;
        public RatingBar rating_review;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.userpic_review);
            txt_kullanici_adi = itemView.findViewById(R.id.username__review);
            txt_yorumlar = itemView.findViewById(R.id.comment_review);
            txt_date = itemView.findViewById(R.id.date_review);
            rating_review = itemView.findViewById(R.id.rating_review);

        }
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.item_review,parent,false);



        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int position) {

        mevcutFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Reviews gonderi = mGönderi.get(position);
        viewHolder.txt_yorumlar.setText(gonderi.getReview());
        viewHolder.txt_date.setText(gonderi.getDate());
        //viewHolder.ratingbarreview.setRating(Float.valueOf(gonderi.getRating()));
        gonderenBilgileri(viewHolder.profil_resmi,viewHolder.txt_kullanici_adi,gonderi.getUsid());




        DatabaseReference rtdb  = FirebaseDatabase.getInstance().getReference("BooksReviews")
                .child(gonderi.getBookid());

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

                        viewHolder.rating_review.setRating(rtx);
                    }
                }else{
                    viewHolder.rating_review.setRating(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    @Override
    public int getItemCount() {
        return mGönderi.size();
    }
    private void gonderenBilgileri(ImageView profil_resmi, TextView kullaniciadi, String kullaniciId){
        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Users").child(kullaniciId);
        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users kullanici = dataSnapshot.getValue(Users.class);
                Glide.with(mContext).load(kullanici.getUserimage()).into(profil_resmi);
                kullaniciadi.setText(kullanici.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
