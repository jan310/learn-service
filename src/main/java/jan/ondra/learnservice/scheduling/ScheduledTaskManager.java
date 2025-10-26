package jan.ondra.learnservice.scheduling;

import jan.ondra.learnservice.curriculum.model.StudyContext;
import jan.ondra.learnservice.curriculum.service.CurriculumService;
import jan.ondra.learnservice.openai.OpenAiClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.MINUTES;

@Component
public class ScheduledTaskManager {

    private final CurriculumService curriculumService;
    private final EmailService emailService;
    private final OpenAiClient openAiClient;

    public ScheduledTaskManager(
        CurriculumService curriculumService,
        EmailService emailService,
        OpenAiClient openAiClient
    ) {
        this.curriculumService = curriculumService;
        this.emailService = emailService;
        this.openAiClient = openAiClient;
    }

    @Transactional
    @Scheduled(cron = "0 0,15,30,45 * * * *")
    public void scheduledTask() {
        OffsetDateTime currentUtcDateTime = OffsetDateTime.now(UTC).truncatedTo(MINUTES);
        List<StudyContext> studyContexts = curriculumService.getStudyContexts(currentUtcDateTime);

        for (var studyContext : studyContexts) {
            emailService.sendMarkdownEmailAsync(
                studyContext.email(),
                "Your daily learning unit is here!",
                studyContext.currentContent()
            );
        }

        List<UUID> curriculumsToFinish = new ArrayList<>();
        studyContexts.removeIf(studyContext -> {
            if (studyContext.nextHeading() == null) {
                curriculumsToFinish.add(studyContext.curriculumId());
                return true;
            }
            return false;
        });

        Map<UUID, CompletableFuture<String>> futureUnitContents = new HashMap<>();

        for (var studyContext : studyContexts) {
            CompletableFuture<String> futureUnitContent = openAiClient.generateLearningUnitContentAsync(
                studyContext.language(),
                studyContext.topic(),
                studyContext.nextHeading(),
                studyContext.nextSubheading()
            );
            futureUnitContents.put(studyContext.nextUnitId(), futureUnitContent);
        }

        CompletableFuture.allOf(futureUnitContents.values().toArray(new CompletableFuture[0])).join();

        Map<UUID, String> nextUnitContents = futureUnitContents.entrySet().stream().collect(
            Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().join()
            )
        );

        curriculumService.setStatusToFinished(curriculumsToFinish);

        curriculumService.incrementCurrentUnitNumber(
            studyContexts.stream().map(
                StudyContext::curriculumId
            ).toList()
        );

        curriculumService.updateLearningUnitContents(nextUnitContents);
    }

}
