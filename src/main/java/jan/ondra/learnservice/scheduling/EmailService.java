package jan.ondra.learnservice.scheduling;

import jakarta.mail.MessagingException;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
        this.mailSender = mailSender;
    }

    @Async
    public void sendMarkdownEmailAsync(String to, String subject, String markdownBody) {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, "utf-8");
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mdToHtml(markdownBody), true);
        } catch (MessagingException e) {
            throw new RuntimeException("Preparing an email caused an error", e);
        }
        mailSender.send(message);
    }

    private String mdToHtml(String markdown) {
        return renderer.render(
            parser.parse(markdown)
        );
    }

}
