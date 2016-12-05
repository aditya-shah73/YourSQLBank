DROP TRIGGER IF EXISTS Handle_Transactions;
DELIMITER //
CREATE TRIGGER Handle_Transactions
AFTER INSERT ON History_TB
FOR EACH ROW
BEGIN
	IF New.TRSN_TYPE = "WTDW" THEN
		UPDATE Account_TB set Balance = Balance - new.TRSN_AMT where Account_TB.ACC_ID = new.ACC_ID;
	ELSE IF New.TRSN_TYPE = "DPST" THEN
		UPDATE Account_TB set Balance = Balance + new.TRSN_AMT where Account_TB.ACC_ID = new.ACC_ID;
	END IF;
	END IF;
END//
DELIMITER ;

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

DROP PROCEDURE IF EXISTS Archive_History;
DELIMITER //
CREATE PROCEDURE Archive_History(IN Expected_Date timestamp)
BEGIN
	UPDATE History_TB_Archived SET TRSN_DATE=CURRENT_TIMESTAMP WHERE TRSN_DATE <= Expected_Date;
	DELETE FROM History_TB WHERE TRSN_DATE <= Expected_Date;
END//
DELIMITER ;