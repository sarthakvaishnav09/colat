package com.example.colat_citizen.ui.home;

import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private String time;
    private int emp;
//    private String docId;


    public HomeViewModel(){}

    public HomeViewModel(String time,int emp/*,String docId*/) {
        this.time=time;
        this.emp=emp;
       // this.docId=docId;
    }

    public LiveData<String> getText() {
        return mText;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public int getEmp() {
        return emp;
    }

    public void setEmp(int emp) {
        this.emp = emp;
    }

//    public String getDocId() {
//        return docId;
//    }
//
//    public void setDocId(String docId) {
//        this.docId = docId;
//    }
}