package in.strikes.bookmyshow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie extends BaseEntity{

    @Column(nullable = false)
    private String title;

    private String genre;

    private String language;

    private Integer durationMins;

    private LocalDate releaseDate;

    @Column(nullable = false)
    private boolean active;
}
