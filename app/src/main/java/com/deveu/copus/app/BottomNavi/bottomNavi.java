package com.deveu.copus.app.BottomNavi;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.deveu.copus.app.Adapter.BottomNavigationViewBehavior;
import com.deveu.copus.app.R;

import com.deveu.copus.app.Sign_in.GoogleUserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class bottomNavi extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    BottomNavigationView bottomNavi;
    Toolbar toolbarss;
    Fragment selectedFragment=null;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navi);
        bottomNavi=findViewById(R.id.bottomNavi);


        bottomNavi.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavi.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener);



        if(selectedFragment == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BookStoreFragment()).commit();
            selectedFragment = new BookStoreFragment();
        }



        mAuth= FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //check users

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if( user!=null)
                {
                   /* final String userId = user.getUid();
                    DatabaseReference userDF = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    userDF.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedFragment=new BookStoreFragment();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });*/
                }
            }
        };



        mAuth.addAuthStateListener(mAuthListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavi.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

    }

    private BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {

        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:

                            if(selectedFragment==new BookStoreFragment()){

                            }else{
                                selectedFragment=new BookStoreFragment();

                            }
                            break;
                        case R.id.nav_pdf:
                            selectedFragment=new ComputerFragment();
                            break;
                        case R.id.nav_bookstore:
                            selectedFragment=new profileuser();
                            break;

                    }
                    if (selectedFragment!=null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }


                    return true;

                }
            };



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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
