package com.example.storygenerator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText genreEditText;
    private Button generateStoryButton;
    private TextView storyTextView;

    private Retrofit retrofit;
    private OpenAIService openAIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        genreEditText = findViewById(R.id.genreEditText);
        generateStoryButton = findViewById(R.id.generateStoryButton);
        storyTextView = findViewById(R.id.storyTextView);

        // Initialize Retrofit and OpenAI service
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openAIService = retrofit.create(OpenAIService.class);

        generateStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateStory();
            }
        });
    }
    private void generateStory() {
        String genre = genreEditText.getText().toString();

        if (genre.isEmpty()) {
            storyTextView.setText("Please enter a genre.");
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
                new OpenAIRequest.Message("system", "You are a helpful assistant."),
                new OpenAIRequest.Message("user", "Write a " + genre + " story.")
        );

        // Create the request object
        OpenAIRequest request = new OpenAIRequest("gpt-3.5-turbo", messages, 2000);
        String apiKey = getString(R.string.openai_api_key);  // Access the API key

        // Content-Type for JSON data
        String contentType = "application/json";
        // Make the API call
        openAIAPI.generateStory(apiKey,contentType,request).enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                if (response.isSuccessful()) {
                    String story = response.body().getChoices().get(0).getMessage().getContent();
                    storyTextView.setText(story);
                } else {
                    storyTextView.setText("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                storyTextView.setText("Failed to generate story: " + t.getMessage());
            }
        });
    }
}
