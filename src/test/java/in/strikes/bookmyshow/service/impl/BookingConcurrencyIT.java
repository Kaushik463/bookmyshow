package in.strikes.bookmyshow.service.impl;

import in.strikes.bookmyshow.TestcontainersConfiguration;
import in.strikes.bookmyshow.dto.request.BookingRequest;
import in.strikes.bookmyshow.entity.*;
import in.strikes.bookmyshow.enums.Role;
import in.strikes.bookmyshow.enums.SeatStatus;
import in.strikes.bookmyshow.enums.SeatType;
import in.strikes.bookmyshow.repository.*;
import in.strikes.bookmyshow.service.BookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class BookingConcurrencyIT {

    @Autowired private BookingService bookingService;
    @Autowired private UserRepository userRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private TheatreRepository theatreRepository;
    @Autowired private ScreenRepository screenRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private ShowRepository showRepository;
    @Autowired private ShowSeatRepository showSeatRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private Long showId;
    private Long seatId;

    @BeforeEach
    void setUp() {
        Theatre theatre = theatreRepository.save(
                Theatre.builder().name("Test Theatre").city("Patna").build());

        Screen screen = screenRepository.save(
                Screen.builder().name("Screen 1").totalSeats(1).theatre(theatre).build());

        Seat seat = seatRepository.save(
                Seat.builder().rowLabel("A").seatNumber(1).seatType(SeatType.REGULAR).screen(screen).build());
        seatId = seat.getId();

        Movie movie = movieRepository.save(
                Movie.builder().title("Concurrency Test Movie").active(true).build());

        Show show = showRepository.save(
                Show.builder()
                        .movie(movie)
                        .screen(screen)
                        .startTime(LocalDateTime.now().plusHours(1))
                        .endTime(LocalDateTime.now().plusHours(3))
                        .basePrice(BigDecimal.valueOf(250))
                        .build());
        showId = show.getId();

        showSeatRepository.save(
                ShowSeat.builder()
                        .show(show)
                        .seat(seat)
                        .status(SeatStatus.AVAILABLE)
                        .price(BigDecimal.valueOf(250))
                        .build());
    }

    @Test
    void onlyOneThreadShouldSuccessfullyBookTheSameSeat() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int userIndex = i;
            executor.submit(() -> {
                try {
                    String email = "concurrency-user-" + userIndex + "@test.com";
                    userRepository.save(User.builder()
                            .name("User " + userIndex)
                            .email(email)
                            .password(passwordEncoder.encode("test1234"))
                            .role(Role.USER)
                            .build());

                    readyLatch.countDown();
                    startLatch.await();

                    bookingService.holdSeats(new BookingRequest(showId, List.of(seatId)), email);
                    successCount.incrementAndGet();
                } catch (Exception ex) {
                    failureCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failureCount.get()).isEqualTo(threadCount - 1);

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(1);
    }

    @AfterEach
    void cleanUp() {
        showSeatRepository.deleteAll();
        bookingRepository.deleteAll();
        showRepository.deleteAll();
        seatRepository.deleteAll();
        screenRepository.deleteAll();
        theatreRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();
    }
}