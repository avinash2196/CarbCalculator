package com.example.foodCarbCalculator.Service;

import com.example.foodCarbCalculator.Model.OpenAIRequest;
import com.example.foodCarbCalculator.Model.OpenAIResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OpenAIService {

    @POST("v1/chat/completions")
    Call<OpenAIResponse> calculateCarbs(@Header("Authorization") String apiKey,  // API Key
                                        @Header("Content-Type") String contentType,  // Content-Type header,
                                        @Body OpenAIRequest request);
}
