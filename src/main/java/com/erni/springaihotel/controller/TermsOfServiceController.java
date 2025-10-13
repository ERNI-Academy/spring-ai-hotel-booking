package com.erni.springaihotel.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/terms")
public class TermsOfServiceController
{

  private final VectorStore vectorStore;

  public TermsOfServiceController(VectorStore vectorStore)
  {
    this.vectorStore = vectorStore;
  }

  /**
   * Search through the terms of service using vector similarity search
   */
  @GetMapping("/search")
  public List<Document> searchTerms(@RequestParam String query,
      @RequestParam(defaultValue = "5") int limit)
  {
    SearchRequest searchRequest = SearchRequest.builder()
        .query(query)
        .topK(limit)
        .build();
    List<Document> results = vectorStore.similaritySearch(searchRequest);

    if (results == null)
    {
      return Collections.emptyList();
    }

    // Filter to only return terms of service documents
    return results.stream()
        .filter(doc -> "springaihotel-terms-of-service".equals(doc.getMetadata().get("source")))
        .toList();
  }

  /**
   * Get specific information about a policy (booking, changes, cancellation, etc.)
   */
  @GetMapping("/policy/{policyType}")
  public List<Document> getPolicyInfo(@PathVariable String policyType)
  {
    String searchQuery = switch (policyType.toLowerCase())
    {
      case "booking" -> "booking rooms payment requirements";
      case "changes", "change" -> "changing bookings fees modification";
      case "cancellation", "cancel" -> "cancelling bookings refund fees";
      case "check-in check-out" -> "check-in and check-out information";
      default -> policyType + " policy terms";
    };

    SearchRequest searchRequest = SearchRequest.builder()
        .query(searchQuery)
        .topK(3)
        .build();
    List<Document> results = vectorStore.similaritySearch(searchRequest);

    if (results == null)
    {
      return Collections.emptyList();
    }

    // Filter to only return terms of service documents
    return results.stream()
        .filter(doc -> "springaihotel-terms-of-service".equals(doc.getMetadata().get("source")))
        .toList();
  }
}
