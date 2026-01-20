import { Component, effect, ElementRef, inject, signal, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ChatService } from '../../services/chat.service';
import {
  HttpClient, HttpDownloadProgressEvent,
  HttpEvent,
  HttpEventType,
} from '@angular/common/http';

interface Message {
  id: string;
  content: string;
  isUser: boolean;
  timestamp: Date;
}

@Component({
  selector:    'app-chat',
  standalone:  true,
  imports:     [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    MatTableModule,
    MatChipsModule,
    MatDividerModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './chat.component.html',
  styleUrls:   ['./chat.component.scss']
})
export class ChatComponent {
  // Signals for reactive state management
  messages = signal<Message[]>([
    {
      id:        '1',
      content:   'Hello, how can I help you today? 😊',
      isUser:    false,
      timestamp: new Date(),
    }
  ]);
  currentMessage = signal('');
  isLoading = signal(false);
  // Computed signal for display columns
  //displayedColumns = signal(['bookingNumber', 'firstName', 'lastName', 'date', 'bookingStatus', 'from', 'to', 'roomType']);
  // ViewChild reference to the messages container for auto-scrolling
  @ViewChild('messagesContainer') private readonly messagesContainer!: ElementRef;

  constructor(private readonly chatService: ChatService) {
    // Effect to auto-scroll when messages change
    effect(() => {
      // This effect runs whenever messages() signal changes
      this.messages();
      this.scrollToBottom();
    });
  }

  sendMessage() {
    const messageText = this.currentMessage()
      .trim();
    if (!messageText) {
      return;
    }

    // Add user message
    const userMessage: Message = {
      id:        Date.now()
                   .toString(),
      content:   messageText,
      isUser:    true,
      timestamp: new Date()
    };

    this.messages.update(messages => [...messages, userMessage]);
    this.currentMessage.set('');
    this.isLoading.set(true);

    // Send message to API
    this.chatService.sendMessage(messageText)
      .subscribe({
        next:  (response) => {
          const aiResponse: Message = {
            id:        (Date.now() + 1).toString(),
            content:   response.text,
            isUser:    false,
            timestamp: new Date()
          };

          this.messages.update(messages => [...messages, aiResponse]);
          this.isLoading.set(false);
        },
        error: (error) => {
          console.error('Error sending message:', error);

          // Fallback message if API fails
          const errorResponse: Message = {
            id:        (Date.now() + 1).toString(),
            content:   'Sorry, I\'m having trouble connecting right now. Please try again in a moment.',
            isUser:    false,
            timestamp: new Date()
          };

          this.messages.update(messages => [...messages, errorResponse]);
          this.isLoading.set(false);
        }
      });
  }

  sendMessageStream() {
    const messageText = this.currentMessage().trim();
    if (!messageText) {
      return;
    }

    const userMessage: Message = {
      id: Date.now().toString(),
      content: messageText,
      isUser: true,
      timestamp: new Date()
    };

    this.messages.update(messages => [...messages, userMessage]);
    this.currentMessage.set('');
    this.isLoading.set(true);

    // Add placeholder for AI response
    const aiMessageId = (Date.now() + 1).toString();
    this.streamSSE(messageText, aiMessageId);
  }

  private async streamSSEnew(messageText: string, aiMessageId: string): Promise<void> {
    const es = new EventSource('/api/customer-support');
    let accumulatedContent = '';

    const aiResponse: Message = {
      id: aiMessageId,
      content: '',
      isUser: false,
      timestamp: new Date()
    };
    this.messages.update(messages => [...messages, aiResponse]);

    es.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log("Received:", data);

      accumulatedContent += data;
      this.messages.update(messages =>
        messages.map(msg =>
          msg.id === aiMessageId
            ? { ...msg, content: accumulatedContent }
            : msg
        )
      );
    };
    es.onerror = () => {
      console.warn("Connection lost. Reconnecting...");
    };
  }

  private async streamSSE(messageText: string, aiMessageId: string): Promise<void> {
    try {
      const response = await fetch('/api/customer-support', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'text/event-stream'
        },
        body: JSON.stringify({ text: messageText })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const reader = response.body?.getReader();
      const decoder = new TextDecoder();
      let accumulatedContent = '';
      let buffer = '';

      if (!reader) {
        throw new Error('No reader available');
      }

      let first = true;
      while (true) {
        const { done, value } = await reader.read();

        if (done) {
          this.isLoading.set(false);
          break;
        }

        buffer += decoder.decode(value, { stream: true });

        const lines = buffer.split('\n');
        buffer = lines.pop() || '';

        for (const line of lines) {
          if (line.startsWith('data:')) {
            const data = line.slice(5);

            if (data === '' || data === ' ') {
              // Empty data line represents a newline in the content
              accumulatedContent += '\n';
            } else if (data !== '[DONE]') {
              accumulatedContent += data;
            }
          }
        }

        if (first) {
          const aiResponse: Message = {
            id: aiMessageId,
            content: '',
            isUser: false,
            timestamp: new Date()
          };
          this.messages.update(messages => [...messages, aiResponse]);
          first = false;
        }

        this.messages.update(messages =>
          messages.map(msg =>
            msg.id === aiMessageId
              ? { ...msg, content: accumulatedContent }
              : msg
          )
        );
      }
    } catch (error) {
      console.error('Error streaming message:', error);

      this.messages.update(messages =>
        messages.map(msg =>
          msg.id === aiMessageId
            ? { ...msg, content: 'Sorry, I\'m having trouble connecting right now. Please try again in a moment.' }
            : msg
        )
      );
      this.isLoading.set(false);
    }
  }

  onKeyPress(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessageStream();
    }
  }

  // Scroll to the bottom of the messages container
  private scrollToBottom(): void {
    setTimeout(() => {
      if (this.messagesContainer) {
        const container = this.messagesContainer.nativeElement;
        container.scrollTop = container.scrollHeight;
      }
    }, 100);
  }
}
