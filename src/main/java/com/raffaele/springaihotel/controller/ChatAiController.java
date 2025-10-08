package com.raffaele.springaihotel.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatAiController
{
  private final ChatClient chatClient;

  public ChatAiController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @GetMapping("/chat")
  public String chat(String userMessage) {
    return chatClient.prompt()
            .user(userMessage)
            .call()
            .content();
  }
}
