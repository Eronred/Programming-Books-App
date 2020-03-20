package com.deveu.copus.app.Sign_in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deveu.copus.app.Adapter.SignSliderAdapter;
import com.deveu.copus.app.BottomNavi.bottomNavi;
import com.deveu.copus.app.Datas.slide;
import com.deveu.copus.app.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class Sign_In extends AppCompatActivity {

    private ImageView githubbutton,googlebutton;
    FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 100;

    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    private List<slide> firstSlides,upsilider;
    private ViewPager v_sign;
    private ConstraintLayout constraintLayoutSign;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        githubbutton = findViewById(R.id.githubsignbutton);
        googlebutton = findViewById(R.id.googlesignbutton);

        constraintLayoutSign = findViewById(R.id.constraintLayoutSign);

        //v_sign = findViewById(R.id.rv_sign);


        progressBar=findViewById(R.id.progressBar);
        constraintLayoutSign=findViewById(R.id.constraintLayoutSign);

        progressBar.setVisibility(View.GONE);


        /*Timer timer= new Timer();
        timer.scheduleAtFixedRate(new sliderTime2(),2000,3000);
        sliderPaperAdapter();*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();
      mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //check users

                FirebaseUser user = firebaseAuth.getCurrentUser();
/*
                if(user!=null)
                {
                    final String userId = user.getUid();
                    DatabaseReference userDF = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    userDF.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent moveToHome = new Intent(getApplicationContext(), bottomNavi.class);
                                moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(moveToHome);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }*/
            if(user!= null){







                /*progressBar.setVisibility(View.VISIBLE);
                constraintLayoutSign.setVisibility(View.GONE);*/
                Intent moveToHome = new Intent(Sign_In.this, bottomNavi.class);
                moveToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(moveToHome);
                finish();
            }


            }
        };


        mAuth.addAuthStateListener(mAuthListener);



        githubbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
                // Target specific email with login hint.
                provider.addCustomParameter("login", "your-email@gmail.com");
                List<String> scopes =
                        new ArrayList<String>() {
                            {
                                add("user:email");
                            }
                        };
                provider.setScopes(scopes);

                Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
                if (pendingResultTask != null) {
                    // There's something already here! Finish the sign-in for your user.
                    pendingResultTask
                            .addOnSuccessListener(
                                    new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            // User is signed in.
                                            // IdP data available in
                                            // authResult.getAdditionalUserInfo().getProfile().
                                            // The OAuth access token can also be retrieved:
                                            // authResult.getCredential().getAccessToken().
                                            Toast.makeText(Sign_In.this, "no connection for current time", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Sign_In.this, "already you have a connection", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                } else {
                    // There's no pending result so you need to start the sign-in flow.
                    // See below.
                }


                mAuth.startActivityForSignInWithProvider(/* activity= */ Sign_In.this, provider.build())
                        .addOnSuccessListener(
                                new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        // User is signed in.
                                        // IdP data available in
                                        // authResult.getAdditionalUserInfo().getProfile().
                                        // The OAuth access token can also be retrieved:
                                        String photoforgithub = mAuth.getCurrentUser().getPhotoUrl().toString();
                                        String emailgithub = mAuth.getCurrentUser().getEmail();
                                        String uidgithub = mAuth.getCurrentUser().getUid();



                                        Intent gitintent = new Intent(getApplicationContext(), GoogleUserProfile.class);
                                        gitintent.putExtra("uid", uidgithub);
                                        gitintent.putExtra("photo",photoforgithub);
                                        gitintent.putExtra("mail",emailgithub);

                                        startActivity(gitintent);
                                        Sign_In.this.finish(); //update
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure.
                                        Toast.makeText(Sign_In.this, "failure", Toast.LENGTH_SHORT).show();
                                    }
                                });
            }
        });


        googlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
               // Log.w(TAG, "Google sign in failed", e);
                // ...
                Toast.makeText(this, "failed"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }



    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String gmail = user.getEmail();
                            String photo = user.getPhotoUrl().toString();
                            String uid = user.getUid();

                            Intent intent = new Intent(getApplicationContext(), GoogleUserProfile.class);
                            intent.putExtra("uid", uid);
                            intent.putExtra("photo",photo);
                            intent.putExtra("mail",gmail);


                            //Toast.makeText(getActivity(), ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            Sign_In.this.finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Sign_In.this, "Failed", Toast.LENGTH_SHORT).show();
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Sign_In.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if(mAuth!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



}
