package in.strikes.bookmyshow;

import org.springframework.boot.SpringApplication;

public class TestBookmyshowApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookmyshowApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
