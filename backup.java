package com.ebmacs.helpapp.Repository;

import com.ebmacs.helpapp.Models.Medicine;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ebmacs.helpapp.Repository.ResourceUtils.EMPTY;
import static com.ebmacs.helpapp.Repository.ResourceUtils.ERROR;
import static com.ebmacs.helpapp.Repository.ResourceUtils.LOADING;
import static com.ebmacs.helpapp.Repository.ResourceUtils.SUCCESS;


public class Repository {

        public MutableLiveData<Resource<Medicine>> getMedicine(String gtinOrName) {

        MutableLiveData<Resource<Medicine>> resourceData = new MutableLiveData<>();

        Resource<Medicine> resource = new Resource<>(LOADING, null, null);

        resourceData.setValue(resource);

        RetrofitServiceFactory.createService(RetrofitService.class)
                .getMedicine(gtinOrName)
                .enqueue(new Callback<List<Medicine>>() {
                    @Override
                    public void onResponse(Call<List<Medicine>> call, Response<List<Medicine>> response) {

                        if (response.isSuccessful()&&response.body()!= null){

                            resource.setData(response.body().get(0));//Resposta ok
                            resource.setStatus(SUCCESS);

                        } else if (response.isSuccessful()&&response.body()== null){
                            // Vazio
                            resource.setStatus(EMPTY);
                        } else {

                            resource.setStatus(ERROR);  //Erro inesperado
                        }

                        resourceData.setValue(resource);
                    }

                    @Override
                    public void onFailure(Call<List<Medicine>> call, Throwable t) {

                        resource.setData(null); //Erro inesperado
                        resource.setStatus(ERROR);
                        resourceData.setValue(resource);
                    }

                });

        return resourceData;
    }

}



