package com.example.colat_citizen.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colat_citizen.MainActivity;
import com.example.colat_citizen.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.*;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView firestore1;
    private RecyclerView firestore2;
    private FirebaseFirestore firebasefirestore;
    private FirestoreRecyclerAdapter adapter;
    private FirestoreRecyclerAdapter adapter2;
    private Spinner spinner;
    private String spinnerText;
    //private Button bookButton;
    private FirebaseUser currentFirebaseUser;
    FirestoreRecyclerOptions<HomeViewModel> options;
    private FirebaseAuth mAuth;
    Query query1;
    Query query2;
    String name1;
    String address1;
    //int booked;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        firebasefirestore=FirebaseFirestore.getInstance();
        firestore1=(RecyclerView)root.findViewById(R.id.list1);
        firestore2=(RecyclerView)root.findViewById(R.id.list2);
        spinner=root.findViewById(R.id.spinner);
        mAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = mAuth.getInstance().getCurrentUser() ;
        //booked=0;
       // bookButton=findViewById(R.id.buttonBook);
        //query2=firebasefirestore.collection("slots").document("01-12-2020").collection("time").limit(1);

        spinnerText="01-12-2020";


        firebasefirestore.collection("slots").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }

                    Log.d(TAG, list.toString());
                    System.out.println(list.toString());

                    ArrayAdapter<String> ld=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,list);
                    ld.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinner.setAdapter(ld);


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        DocumentReference docRef = firebasefirestore.collection("users").document(currentFirebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("DocumentSnapshot data: " + document.getData());
                        address1=(document.get("address")).toString();
                        name1=(document.get("name")).toString();
                        //booked=Integer.parseInt(document.get("booked").toString());
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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item=parent.getItemAtPosition(position);

                if(item!=null)
                {

                    spinnerText = item.toString();
                    query1=firebasefirestore.collection("slots").document(spinnerText).collection("time");
//                    options =new FirestoreRecyclerOptions.Builder<HomeViewModel>()
//                            .setQuery(query2,HomeViewModel.class)
//                            .build();
//                    System.out.println(options.getSnapshots().toString());

                    // adapter.notifyDataSetChanged();
                    // adapter.startListening();
                    // System.out.println(query2.toString());
                    // callRecycler(query2);
                    // adapter.notifyDataSetChanged();



                    options =new FirestoreRecyclerOptions.Builder<HomeViewModel>()
                            .setQuery(query1,HomeViewModel.class)
                            .build();

                    adapter2 =new FirestoreRecyclerAdapter<HomeViewModel, productsViewHolder> (options)
                    {
                        @NonNull
                        @Override
                        public productsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.time_single,parent,false);
                            return new productsViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull productsViewHolder holder, int position, @NonNull HomeViewModel model)
                        {
                            holder.list_time.setText(model.getTime());
                            holder.list_emp.setText(String.valueOf(model.getEmp()));
                            String docuId=getSnapshots().getSnapshot(position).getId();
                            holder.idDoc=docuId;
                        }
                    };

                    firestore2.setHasFixedSize(true);
                    firestore2.setLayoutManager(new LinearLayoutManager(getContext()));
                    firestore2.setAdapter(adapter2);

                    adapter2.startListening();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         query2=firebasefirestore.collection("slots").document(spinnerText).collection("time");


        callRecycler(query2);


        // view Holder
        return root;
    }

    private class productsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView list_time;
        private TextView list_emp;
        private Button bookButton;
        private String idDoc;

        public productsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            list_time=itemView.findViewById(R.id.list_time);
            list_emp=itemView.findViewById(R.id.list_emp);
            bookButton=itemView.findViewById(R.id.buttonBook);

            bookButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it


                if(Integer.parseInt(list_emp.getText().toString())>0)
                {
                    DocumentReference docRef1 = firebasefirestore.collection("users").document(currentFirebaseUser.getUid());
                    docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists())
                                {
                                    if(Integer.parseInt(document.get("booked").toString())==0)
                                    {
                                        Toast.makeText(getContext(), spinnerText+" "+idDoc, Toast.LENGTH_SHORT).show();

                                        DocumentReference docref=firebasefirestore.collection("slots")
                                                .document(spinnerText)
                                                .collection("time")
                                                .document(idDoc);

                                        docref.update("emp",(Integer.parseInt(list_emp.getText().toString()))-1);

                                        Map<String, Object> city = new HashMap<>();

                                        city.put("name",name1);
                                        city.put("address",address1);

                                        firebasefirestore.collection("bookedSlots")
                                                .document(spinnerText)
                                                .collection("time")
                                                .document(list_time.getText().toString())
                                                .collection("users")


                                                .add(city);

                                        //DocumentReference docref2=

                                        firebasefirestore.collection("users")
                                                .document(currentFirebaseUser.getUid())
                                                .update("booked",1);

                                        Map<String, Object> appoint = new HashMap<>();
                                        appoint.put("date", spinnerText);
                                        appoint.put("time", list_time.getText().toString());

                                        firebasefirestore.collection("users")
                                                .document(currentFirebaseUser.getUid())
                                                .collection("appointment").document("details")
                                                .set(appoint);
                                    }

                                    else
                                    {
                                        Snackbar.make(getView(), "You have already made an appointment, Cancel the existing appointment to book a new one", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }

                                    //booked=Integer.parseInt(document.get("booked").toString());
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



//                    if(booked==1)
//                    {
//                        Snackbar.make(view, "You have already made an appointment, Cancel the existing appointment to book a new one", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(getContext(), spinnerText+" "+idDoc, Toast.LENGTH_SHORT).show();
//
//                        DocumentReference docref=firebasefirestore.collection("slots")
//                                .document(spinnerText)
//                                .collection("time")
//                                .document(idDoc);
//
//                        docref.update("emp",(Integer.parseInt(list_emp.getText().toString()))-1);
//
//                        Map<String, Object> city = new HashMap<>();
//
//                        city.put("name",name1);
//                        city.put("address",address1);
//
//                        firebasefirestore.collection("bookedSlots")
//                                .document(spinnerText)
//                                .collection("time")
//                                .document(list_time.getText().toString())
//                                .collection("users")
//
//
//                                .add(city);
//
//                        //DocumentReference docref2=
//
//                        firebasefirestore.collection("users")
//                                .document(currentFirebaseUser.getUid())
//                                .update("booked",1);
//
//                        Map<String, Object> appoint = new HashMap<>();
//                        appoint.put("date", spinnerText);
//                        appoint.put("time", list_time.getText().toString());
//
//                        firebasefirestore.collection("users")
//                                .document(currentFirebaseUser.getUid())
//                                .collection("appointment").document("details")
//                                .set(appoint);
//
//
//
//                    }


                }
                else
                {
                    Snackbar.make(view, "Slot not available, please choose available slots", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }


    }

    private void callRecycler(Query query)
    {
        options =new FirestoreRecyclerOptions.Builder<HomeViewModel>()
                .setQuery(query,HomeViewModel.class)
                .build();

        adapter =new FirestoreRecyclerAdapter<HomeViewModel, productsViewHolder> (options)
        {
            @NonNull
            @Override
            public productsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.time_single,parent,false);
                return new productsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull productsViewHolder holder, int position, @NonNull HomeViewModel model)
            {
                holder.list_time.setText(model.getTime());
                holder.list_emp.setText(String.valueOf(model.getEmp()));
                String docuId=getSnapshots().getSnapshot(position).getId();
                holder.idDoc=docuId;
//                bookButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getContext(),"Button clicked",Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        };




        firestore2.setHasFixedSize(true);
        firestore2.setLayoutManager(new LinearLayoutManager(getContext()));
        firestore2.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }


}
