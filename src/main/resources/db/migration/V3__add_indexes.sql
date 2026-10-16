CREATE INDEX idx_show_start_time ON shows(start_time);
CREATE INDEX idx_movie_title ON movies(title);
CREATE INDEX idx_booking_user_id ON bookings(user_id);
CREATE INDEX idx_booking_status ON bookings(status);
CREATE INDEX idx_showseat_show_id ON show_seats(show_id);
CREATE INDEX idx_theatre_city ON theatres(city);