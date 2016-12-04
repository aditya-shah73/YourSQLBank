FLUSH PRIVILEGES;
SET SQL_SAFE_UPDATES = 0;

INSERT INTO Account_TB (USERNAME, ACC_TYPE, BALANCE) values ("groot", "CHKG", 100);
INSERT INTO Account_TB (USERNAME, ACC_TYPE, BALANCE) values ("groot", "SVNG", 100);
INSERT INTO User_TB (USERNAME, CHECKING_ACC_ID, SAVING_ACC_ID, ADMIN, ACTIVE, F_NAME, L_NAME, PASS) values ("groot", 1, 2, TRUE, TRUE, "I AM", "GROOT", "groot");

INSERT INTO Account_TB (USERNAME, ACC_TYPE, BALANCE) values ("soham", "CHKG", 100);
INSERT INTO Account_TB (USERNAME, ACC_TYPE, BALANCE) values ("soham", "SVNG", 100);
INSERT INTO User_TB (USERNAME, CHECKING_ACC_ID, SAVING_ACC_ID, ADMIN, ACTIVE, F_NAME, L_NAME, PASS) values ("soham", 3, 4, FALSE, TRUE, "Soham", "Shah", "pass");

INSERT INTO history_tb (TRSN_TYPE, TRSN_AMT, USERNAME, ACC_ID) VALUES ('WTDW', 10, "groot", 1);
INSERT INTO history_tb (TRSN_TYPE, TRSN_AMT, USERNAME, ACC_ID) VALUES ('DPST', 100, "groot", 2);

INSERT INTO history_tb (TRSN_TYPE, TRSN_AMT, USERNAME, ACC_ID) VALUES ('WTDW', 20, "soham", 3);
INSERT INTO history_tb (TRSN_TYPE, TRSN_AMT, USERNAME, ACC_ID) VALUES ('DPST', 200, "soham", 4);

SELECT * FROM Account_TB;
SELECT * FROM User_TB;
SELECT * FROM History_TB;
SELECT * FROM Account_TB_Archive;
SELECT * FROM History_TB_Archive;

SELECT * FROM User_TB join Account_TB ON User_TB.USERNAME = Account_TB.USERNAME;
SELECT * FROM User_TB JOIN Account_TB ON User_TB.USERNAME = Account_TB.USERNAME JOIN History_TB ON User_TB.USERNAME = History_TB.USERNAME;

DROP TRIGGER IF EXISTS Account_Closing;
DELIMITER //
CREATE TRIGGER Account_Closing
AFTER UPDATE ON User_TB
FOR EACH ROW
BEGIN
IF New.Active = FALSE and Old.Active = TRUE THEN
INSERT INTO Account_TB_Archive (SELECT * FROM Account_TB where Account_TB.USERNAME = NEW.USERNAME);
DELETE FROM Account_TB where Account_TB.USERNAME = NEW.USERNAME;
INSERT INTO History_TB_Archive (SELECT * FROM History_TB where History_TB.USERNAME = NEW.USERNAME);
DELETE FROM History_TB where History_TB.USERNAME = NEW.USERNAME;
END IF;
END//
DELIMITER ;

--==================================================================================================================================================

-- SELECT * FROM Account_TB WHERE Account_TB.USERNAME = "soham"; -- Soham is due to be closed
-- SELECT * FROM History_TB WHERE Account_TB.USERNAME = "soham"; -- Soham is due to be closed