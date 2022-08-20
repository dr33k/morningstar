CREATE OR REPLACE FUNCTION create_ticket()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$FUNCTION$
BEGIN
INSERT INTO ticket(id,booking_no,expiry_date,status)
VALUES(nextval('ticket_sequence'),NEW.booking_no,NEW.expiry_date,'VALID');
RETURN NEW;
END
$FUNCTION$;

CREATE TRIGGER ticket_trigger AFTER INSERT ON booking
FOR EACH ROW EXECUTE PROCEDURE create_ticket();

INSERT INTO r_user(id,date_birth,date_reg,email,first_name,is_account_non_expired,is_account_non_locked,is_credentials_non_expired,is_enabled,last_name,password,phone_no,role)
VALUES(nextval('r_user_sequence'),'2000-11-22',current_timestamp,'admin@safuser.com','John',true,true,true,true,'Doe','$2a$10$rtCR854YRZgX5n2v4MisiubYLHay2Yz/kOJC.9okOfoXc2jjiWYyq','+2341234567890','ADMIN');