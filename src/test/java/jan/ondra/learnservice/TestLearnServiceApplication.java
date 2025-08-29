package jan.ondra.learnservice;

import org.springframework.boot.SpringApplication;

public class TestLearnServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(LearnServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
