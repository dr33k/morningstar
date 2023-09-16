CREATE FUNCTION create_enums()
returns BOOL
language plpgsql
as
$$
DECLARE
 result BOOL;
BEGIN
     SELECT TRUE FROM pg_type WHERE typname = 'booking_status' INTO result;
        IF (result IS NOT TRUE)
        THEN CREATE TYPE booking_status AS ENUM('VALID','USED','EXPIRED','CANCELLED');
        END IF;

     SELECT TRUE FROM pg_type WHERE typname = 'seat_type' INTO result;
          IF (result IS NOT TRUE)
          THEN  CREATE TYPE seat_type AS ENUM('FIRST_CLASS','ECONOMY');
          END IF;

      SELECT TRUE FROM pg_type WHERE typname = 'user_role' INTO result;
               IF (result IS NOT TRUE)
               THEN  CREATE TYPE user_role AS ENUM('PASSENGER','OFFICER','ADMIN');
               END IF;

      SELECT TRUE FROM pg_type WHERE typname = 'voyage_status' INTO result;
               IF (result IS NOT TRUE)
               THEN  CREATE TYPE voyage_status AS ENUM('PENDING','IN_TRANSIT','COMPLETED','CANCELLED');
               END IF;

      SELECT TRUE FROM pg_type WHERE typname = 'location_status' INTO result;
                     IF (result IS NOT TRUE)
                     THEN  CREATE TYPE location_status AS ENUM('UNUSED','USED','DISABLED_UNUSED','DISABLED_USED');
                     END IF;

   RETURN result;
END;
$$;

SELECT create_enums();
