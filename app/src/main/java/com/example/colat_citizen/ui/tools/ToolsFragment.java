package com.example.colat_citizen.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.colat_citizen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    private FirebaseFirestore firebasefirestore;
    private FirebaseUser currentFirebaseUser;
    private FirebaseAuth mAuth;
    private TextView date,time,appoint,cancelDate,cancelTime;
    private String date1,time1;
    private Button cancelButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        firebasefirestore=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = mAuth.getInstance().getCurrentUser() ;
        final TextView textView = root.findViewById(R.id.text_tools);
        appoint=root.findViewById(R.id.textView);
        date=root.findViewById(R.id.textView2);
        time=root.findViewById(R.id.textView3);
        cancelDate=root.findViewById(R.id.cancel_date);
        cancelTime=root.findViewById(R.id.cancel_time);
        cancelButton=root.findViewById(R.id.cancel_button);



        DocumentReference docRef = firebasefirestore.collection("users").document(currentFirebaseUser.getUid()).collection("appointment").document("details");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        System.out.println("DocumentSnapshot data: " + document.getData());
                        date1=(document.get("date")).toString();
                        time1=(document.get("time")).toString();

                        cancelDate.setText(date1);
                        cancelTime.setText(time1);

                        cancelTime.setVisibility(View.VISIBLE);
                        cancelDate.setVisibility(View.VISIBLE);
                        date.setVisibility(View.VISIBLE);
                        time.setVisibility(View.VISIBLE);
                        appoint.setVisibility(View.VISIBLE);
                        cancelButton.setVisibility(View.VISIBLE);



                    }
                     else
                     {
                         //status=1;
                         toolsViewModel.getText().observe(getActivity(), new Observer<String>() {
                             @Override
                             public void onChanged(@Nullable String s) {
                                 textView.setText(s);
                             }
                         });
                     }
                } else {
                    System.out.println(task.getException());
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

//        if(status==1)
//        {
//            toolsViewModel.getText().observe(this, new Observer<String>() {
//                @Override
//                public void onChanged(@Nullable String s) {
//                    textView.setText(s);
//                }
//            });
//        }



        return root;
    }
}