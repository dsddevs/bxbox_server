package dsd.bxbox.controller;

import dsd.bxbox.gemini.GeminiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("api")
public class GeminiController {

    private final GeminiService geminiService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/ai")
    public SseEmitter createAiQuery(@RequestBody String request) {
        SseEmitter emitter = new SseEmitter();
        executorService.execute(() -> {
            try {
                String fullResponse = geminiService.getCompletion(request);
                String[] paragraphs = fullResponse.split("\n");
                for (String paragraph : paragraphs) {
                    emitter.send(SseEmitter.event().data(paragraph + "\n"));
                    Thread.sleep(100);
                }
                emitter.send(SseEmitter.event().data("[END]"));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
