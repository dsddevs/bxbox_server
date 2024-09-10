package dsd.bxbox.gemini;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@HttpExchange("/v1beta/models/")
public interface GeminiInterface {
    @GetExchange
    GeminiRecords.ModelList getModels();

    @PostExchange("{model}:countTokens")
    GeminiRecords.GeminiCountResponse countTokens(
            @PathVariable String model,
            @RequestBody GeminiRecords.GeminiRequest request);

    @PostExchange("{model}:generateContent")
    GeminiRecords.GeminiResponse getCompletion(
            @PathVariable String model,
            @RequestBody GeminiRecords.GeminiRequest request);

    @PostExchange("{model}:streamGenerateContent")
    Flux<GeminiRecords.GeminiResponse> getCompletionStream(
            @PathVariable String model,
            @RequestBody GeminiRecords.GeminiRequest request);
}
