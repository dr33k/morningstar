CREATE TABLE r_user (
        id BIGSERIAL NOT NULL PRIMARY KEY,
        first_name VARCHAR(50) NOT NULL,
        last_name VARCHAR(50)  NOT NULL,
        phone_no VARCHAR(15) NOT NULL CONSTRAINT r_user_phone_no_check CHECK(LENGTH(phone_no) >= 11 AND LENGTH(phone_no) <= 15),
        email VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(512) NOT NULL,
        date_birth DATE NOT NULL,
        date_reg TIMESTAMP NOT NULL,
        date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        role user_role DEFAULT 'PASSENGER',
        is_account_non_expired BOOL DEFAULT TRUE,
        is_account_non_locked BOOL DEFAULT TRUE,
        is_credentials_non_expired BOOL DEFAULT TRUE,
        is_enabled BOOL DEFAULT TRUE
);

CREATE TABLE location(
        state_code CHAR(6) NOT NULL,
        station_no VARCHAR(2) NOT NULL,
        station_name VARCHAR(80) NOT NULL,
        state_name VARCHAR(50) NOT NULL,
        status location_status DEFAULT 'UNUSED',
        PRIMARY KEY(state_code, station_no)
);

CREATE TABLE voyage(
        voyage_no UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
        departure_location_state_code CHAR(6) NOT NULL,
        departure_location_station_no VARCHAR(2) NOT NULL,
        arrival_location_state_code CHAR(6) NOT NULL,
        arrival_location_station_no VARCHAR(2) NOT NULL,
        departure_date_time TIMESTAMP NOT NULL,
        arrival_date_time TIMESTAMP,
        status voyage_status DEFAULT 'PENDING',
        published BOOL DEFAULT FALSE,

        CONSTRAINT departure_location_fk FOREIGN KEY(departure_location_state_code, departure_location_station_no)
        REFERENCES location(state_code, station_no) ON DELETE RESTRICT,

        CONSTRAINT arrival_location_fk FOREIGN KEY(arrival_location_state_code, arrival_location_station_no)
        REFERENCES location(state_code, station_no) ON DELETE RESTRICT
);

CREATE TABLE booking(
        booking_no UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
        voyage_no UUID NOT NULL ,
        passenger BIGINT NOT NULL ,
        booking_date_time TIMESTAMP NOT NULL,
        seat_type seat_type NOT NULL,
        status booking_status DEFAULT 'VALID',
        is_paid BOOL DEFAULT FALSE,

        CONSTRAINT voyage_no_fk FOREIGN KEY(voyage_no) REFERENCES voyage(voyage_no) ON DELETE RESTRICT,
        CONSTRAINT passenger_fk FOREIGN KEY(passenger) REFERENCES r_user(id) ON DELETE CASCADE
);

CREATE TABLE ticket (
        id BIGSERIAL NOT NULL PRIMARY KEY,
        booking_no UUID NOT NULL,
        creation_date_time TIMESTAMP NOT NULL,
        expiry_date_time TIMESTAMP NOT NULL,

        CONSTRAINT booking_no_fk FOREIGN KEY(booking_no) REFERENCES booking(booking_no) ON DELETE CASCADE
);