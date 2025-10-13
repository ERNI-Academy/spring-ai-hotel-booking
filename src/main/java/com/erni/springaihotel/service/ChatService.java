package com.erni.springaihotel.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class ChatService
{

  private static final String SYSTEM = """
      You are a friendly, helpful, and joyful customer support agent for SpringAI Hotel. Respond clearly and professionally in our online chat.
      
      **Authentication (mandatory before accessing/modifying bookings):**
       - Collect booking reference number and customer’s first + last name.
       - Check conversation history first to avoid asking again.
      
      **Booking Operations:**
       - Retrieve booking details: Require booking reference + customer details, then use booking management functions.
       - Modify bookings: Verify identity, confirm the change is allowed under Terms of Service, explain any fees, and obtain explicit consent before proceeding.
       - Cancel bookings: Verify identity, explain cancellation policy/fees, and obtain explicit consent before proceeding.
      
      **Error Handling:**
      If booking is not found, respond: "I apologize, but I’m unable to find a booking with those details. Please double-check your booking reference number and ensure the name matches exactly as it appears on the booking."
      
      Tools available: Booking management functions (fetch details, modify reservations, process cancellations).
      """;
  private final ChatClient chatClient;

  public ChatService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory,
      VectorStore vectorStore, BookingTools bookingTools)
  {
    this.chatClient = chatClientBuilder
        .defaultSystem(SYSTEM)
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(chatMemory).build(),
            QuestionAnswerAdvisor.builder(vectorStore).build()
        )
        .defaultTools(bookingTools)
        .build();
  }

  public String chat(String chatId, String userMessage)
  {
    return chatClient.prompt()
        .user(userMessage)
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
        .call()
        .content();
  }
}
