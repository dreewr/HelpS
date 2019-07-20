package com.ebmacs.helpapp.Repository;

import com.ebmacs.helpapp.Models.Medicine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

//Interface API Service: implementa os modelos das chamadas
public interface RetrofitService {

    /**MedId - API **/

   // @Headers("token: ab9aaa03-0f22-4f8e-9533-def1e2553dc1")
    @GET("medicine/by-gtin-or-name/{gtin-or-name}?region=BR")
    Call<List<Medicine>> getMedicine(@Path("gtin-or-name") String gtinOrName);

}