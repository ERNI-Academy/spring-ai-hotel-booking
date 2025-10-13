package com.erni.springaihotel.controller;

import com.erni.springaihotel.service.ChatService;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer-support")
public class BookingController
{

  private final String chatId = UUID.randomUUID().toString();
  private final ChatService chatService;

  public BookingController(ChatService chatService)
  {
    this.chatService = chatService;
  }

  @PostMapping
  public ChatMessage chat(@RequestBody ChatMessage userMessage)
  {
    var message = chatService.chat(chatId, userMessage.text());
    return new ChatMessage(message);
  }

  record ChatMessage(String text)
  {
  }
}
