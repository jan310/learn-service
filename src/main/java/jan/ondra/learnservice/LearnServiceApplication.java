package jan.ondra.learnservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LearnServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnServiceApplication.class, args);
    }

}
