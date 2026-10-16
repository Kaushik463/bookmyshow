CREATE TABLE shows (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       start_time DATETIME NOT NULL,
                       end_time DATETIME NOT NULL,
                       base_price DECIMAL(10,2) NOT NULL,
                       movie_id BIGINT NOT NULL,
                       screen_id BIGINT NOT NULL,
                       created_at DATETIME NOT NULL,
                       updated_at DATETIME,
                       CONSTRAINT fk_show_movie FOREIGN KEY (movie_id) REFERENCES movies(id),
                       CONSTRAINT fk_show_screen FOREIGN KEY (screen_id) REFERENCES screens(id)
);

CREATE TABLE bookings (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          status VARCHAR(20) NOT NULL,
                          total_amount DECIMAL(10,2) NOT NULL,
                          expires_at DATETIME,
                          user_id BIGINT NOT NULL,
                          show_id BIGINT NOT NULL,
                          created_at DATETIME NOT NULL,
                          updated_at DATETIME,
                          CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id),
                          CONSTRAINT fk_booking_show FOREIGN KEY (show_id) REFERENCES shows(id)
);

CREATE TABLE show_seats (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            status VARCHAR(20) NOT NULL,
                            price DECIMAL(10,2) NOT NULL,
                            locked_until DATETIME,
                            version BIGINT NOT NULL DEFAULT 0,
                            show_id BIGINT NOT NULL,
                            seat_id BIGINT NOT NULL,
                            booking_id BIGINT,
                            created_at DATETIME NOT NULL,
                            updated_at DATETIME,
                            CONSTRAINT fk_showseat_show FOREIGN KEY (show_id) REFERENCES shows(id),
                            CONSTRAINT fk_showseat_seat FOREIGN KEY (seat_id) REFERENCES seats(id),
                            CONSTRAINT fk_showseat_booking FOREIGN KEY (booking_id) REFERENCES bookings(id),
                            CONSTRAINT uq_show_seat UNIQUE (show_id, seat_id)
);

CREATE TABLE payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          amount DECIMAL(10,2) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          transaction_ref VARCHAR(255),
                          paid_at DATETIME,
                          booking_id BIGINT NOT NULL UNIQUE,
                          created_at DATETIME NOT NULL,
                          updated_at DATETIME,
                          CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES bookings(id)
);