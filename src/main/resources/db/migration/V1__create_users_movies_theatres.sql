CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       created_at DATETIME NOT NULL,
                       updated_at DATETIME
);

CREATE TABLE movies (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        genre VARCHAR(100),
                        language VARCHAR(50),
                        duration_mins INT,
                        release_date DATE,
                        active BOOLEAN NOT NULL DEFAULT TRUE,
                        created_at DATETIME NOT NULL,
                        updated_at DATETIME
);

CREATE TABLE theatres (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          city VARCHAR(100) NOT NULL,
                          address VARCHAR(500),
                          created_at DATETIME NOT NULL,
                          updated_at DATETIME
);

CREATE TABLE screens (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         total_seats INT NOT NULL,
                         theatre_id BIGINT NOT NULL,
                         created_at DATETIME NOT NULL,
                         updated_at DATETIME,
                         CONSTRAINT fk_screen_theatre FOREIGN KEY (theatre_id) REFERENCES theatres(id)
);

CREATE TABLE seats (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       row_label VARCHAR(5) NOT NULL,
                       seat_number INT NOT NULL,
                       seat_type VARCHAR(20) NOT NULL,
                       screen_id BIGINT NOT NULL,
                       created_at DATETIME NOT NULL,
                       updated_at DATETIME,
                       CONSTRAINT fk_seat_screen FOREIGN KEY (screen_id) REFERENCES screens(id)
);