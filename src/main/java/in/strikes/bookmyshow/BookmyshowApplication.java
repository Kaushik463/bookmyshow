package in.strikes.bookmyshow;

import in.strikes.bookmyshow.config.JwtProperties;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
@EnableScheduling
public class BookmyshowApplication {

    public static void main(String[] args) {
        migrateDatabase();
        SpringApplication.run(BookmyshowApplication.class, args);
    }

    private static void migrateDatabase() {
        String url = System.getenv().getOrDefault("SPRING_DATASOURCE_URL",
                "jdbc:mysql://localhost:3306/bookmyshow");
        String username = System.getenv().getOrDefault("SPRING_DATASOURCE_USERNAME", "root");
        String password = System.getenv().getOrDefault("SPRING_DATASOURCE_PASSWORD", "kaushik463");

        Flyway.configure()
                .dataSource(url, username, password)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }
}