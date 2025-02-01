package com.example.foodCarbCalculator.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodCarbCalculator.Model.OpenAIRequest;
import com.example.foodCarbCalculator.Model.OpenAIResponse;
import com.example.foodCarbCalculator.Service.OpenAIService;
import com.example.foodCarbCalculator.R;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText queryEditText;
    private Button calculateCarbButton;
    private TextView queryResponseTextView;

    private Retrofit retrofit;
    private OpenAIService openAIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queryEditText = findViewById(R.id.queryEditText);
        calculateCarbButton = findViewById(R.id.calculateCarbButton);
        queryResponseTextView = findViewById(R.id.responseTextView);

        // Initialize Retrofit and OpenAI service
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openAIService = retrofit.create(OpenAIService.class);

        calculateCarbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCarb();
            }
        });
    }
    private void calculateCarb() {
        String carbQuery = queryEditText.getText().toString();

        if (carbQuery.isEmpty()) {
            queryResponseTextView.setText("Please enter Query.");
            return;
        }

        // Create a Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the API service
        OpenAIService openAIAPI = retrofit.create(OpenAIService.class);

        // Create the message for the request
        List<OpenAIRequest.Message> messages = Arrays.asList(
                new OpenAIRequest.Message("system", "You are an AI that only responds with the number of carbs in food items, without any explanations."),
                new OpenAIRequest.Message("user", "How many carbs are in following : " + carbQuery)
        );

        // Create the request object
        OpenAIRequest request = new OpenAIRequest("gpt-3.5-turbo", messages, 2000);
        String apiKey = getString(R.string.openai_api_key);  // Access the API key

        // Content-Type for JSON data
        String contentType = "application/json";
        // Make the API call
        openAIAPI.calculateCarbs(apiKey,contentType,request).enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                if (response.isSuccessful()) {
                    String carbs = response.body().getChoices().get(0).getMessage().getContent();
                    queryResponseTextView.setText(carbs);
                } else {
                    queryResponseTextView.setText("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                queryResponseTextView.setText("Failed to calculate carbs: " + t.getMessage());
            }
        });
    }
}
