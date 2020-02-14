package com.deveu.copus.app.Sign_in;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import com.deveu.copus.app.BottomNavi.bottomNavi;
import com.deveu.copus.app.MainActivity;
import com.deveu.copus.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class GoogleUserProfile extends AppCompatActivity {
    private ProgressDialog progressDialog;
    //private static final int SELECT_FILE = 2;
    //  private static final int REQUEST_CAMERA= 3;
    private EditText userNameEditText;

    Button saveProfileBtn;


    ProgressDialog mProgress;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private String currentUserID;
    //StorageReference UserProfileImagesRef;
    //StorageReference mStorageRef;
    private DatabaseReference RootRef;
    private DatabaseReference userDF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_user_profile);
        userNameEditText = (EditText) findViewById(R.id.userProfileName);
        saveProfileBtn = (Button)findViewById(R.id.saveProfile);

        //assign instances
        //final String useremail = getIntent().getStringExtra("e-mail");

        final String useremail = getIntent().getStringExtra("mail");
        final String photo = getIntent().getStringExtra("photo");
        final String uid = getIntent().getStringExtra("uid");




        mAuth = FirebaseAuth.getInstance();
        //progress dialog
        currentUserID = mAuth.getCurrentUser().getUid();
        mProgress = new ProgressDialog(this);

        //firebase database instance
        RootRef = FirebaseDatabase.getInstance().getReference();
        //  mStorageRef = FirebaseStorage.getInstance().getReference();
        // onclick save profile
        progressDialog = new ProgressDialog(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //check users

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if( user!=null)
                {
                    final String userId = user.getUid();
                    userDF = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    userDF.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent moveToHome = new Intent(GoogleUserProfile.this, bottomNavi.class);
                                moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(moveToHome);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        };



        mAuth.addAuthStateListener(mAuthListener);

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")

            @Override
            public void onClick(View v) {

                //logic for saving user profile
                Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username")
                        .equalTo(String.valueOf(userNameEditText.getText()));
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(GoogleUserProfile.this, "Choose a different user name", Toast.LENGTH_SHORT).show();
                        }  else {
                            if (String.valueOf(userNameEditText.getText()).equals("")) {
                                Toast.makeText(GoogleUserProfile.this, "Please type a username", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progressDialog.setTitle("Saving Profile..");
                                progressDialog.setMessage("Please Wait..");
                                progressDialog.show();

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                HashMap hashMp = new HashMap();
                                hashMp.put("username", String.valueOf(userNameEditText.getText()));
                                hashMp.put("userimage", photo);
                                hashMp.put("e-mail",useremail);
                                hashMp.put("userid",uid);
                                hashMp.put("isPro","no");
                                hashMp.put("userinfo","...");

                                databaseReference.child("Users").child(currentUserID).setValue(hashMp);
                                SendtoMainActivity();

                                Toast.makeText(GoogleUserProfile.this, "The account created successfully", Toast.LENGTH_SHORT).show();
                              //  saveProfileBtn.setVisibility(View.GONE);
                                progressDialog.dismiss();



                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        //user imageview onclick listener

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void SendtoMainActivity(){
        Intent mainIntent = new Intent(GoogleUserProfile.this, bottomNavi.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }

}
