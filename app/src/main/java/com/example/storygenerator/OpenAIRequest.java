package com.example.storygenerator;

import java.util.List;
public class OpenAIRequest {
    private final String model;
    private final List<Message> messages;
    private final int max_tokens;

    public OpenAIRequest(String model, List<Message> messages, int max_tokens) {
        this.model = model;
        this.messages = messages;
        this.max_tokens = max_tokens;
    }

    public static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
