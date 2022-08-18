CREATE OR REPLACE FUNCTION create_ticket()
RETURNS TRIGGER
LANGUAGE plpgql
AS
$FUNCTION$
BEGIN
INSERT INTO ticket(id,booking_no,expiry_date,status)
VALUES(nextval('ticket_sequence'),NEW.booking_no,NEW.expiry_date,'VALID')
END
RETURN NEW
$FUNCTION$;

CREATE TRIGGER AFTER INSERT ON booking
FOR EACH ROW EXECUTE PROCEDURE create_ticket();