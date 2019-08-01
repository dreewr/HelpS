package com.ebmacs.helpapp.ViewModel;

import com.ebmacs.helpapp.Models.Medicine;
import com.ebmacs.helpapp.Repository.Repository;
import com.ebmacs.helpapp.Repository.Resource;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddMedicineViewModel extends ViewModel {

    Repository repository = new Repository();

    public MutableLiveData<Medicine> medicine;

    public LiveData<Medicine> getMedicine() {
        if (medicine == null) {
           medicine = new MutableLiveData<>();
        }
        return medicine;
    }

//    public void fetchMedicine(String code) {
//        //medicine = repository.getMedicine(code);
//    }
}

