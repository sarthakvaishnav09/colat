package com.example.colat_citizen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;
    private TextView currentUserName;
    private TextView currentUserEmail;
    private DocumentReference docref;
    private FirebaseUser currentFirebaseUser;
    private String username1,email1;

   // private DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentFirebaseUser = mAuth.getInstance().getCurrentUser() ;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        currentUserEmail=(TextView) headerView.findViewById(R.id.header_email);
        currentUserName=(TextView) headerView.findViewById(R.id.header_username);

        docref = db.collection("users").document(currentFirebaseUser.getUid());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send message to the Labs", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //System.out.println(docref);
                //System.out.println(currentFirebaseUser.getUid());
            }
        });


        DocumentReference docRef = db.collection("users").document(currentFirebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("DocumentSnapshot data: " + document.getData());
                        email1=(document.get("email")).toString();
                        username1=(document.get("name")).toString();
                        currentUserEmail.setText(username1);
                        currentUserName.setText(email1);
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        System.out.println("No such document");
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    System.out.println(task.getException());
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        currentUserEmail.setText(username1);
        currentUserName.setText(email1);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_aboutLabs,
                R.id.nav_bookSlot, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_logout){
                    mAuth.signOut();
                    SendUserToLoginActivity();
                }
            }
        });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser==null)
        {
            SendUserToLoginActivity();
        }

    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent=new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();

    }

}
