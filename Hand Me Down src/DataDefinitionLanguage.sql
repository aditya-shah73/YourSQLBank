DROP DATABASE IF EXISTS Bank;
CREATE DATABASE Bank;
USE Bank; 

DROP TABLE IF EXISTS T_CUSTOMER;
CREATE TABLE T_CUSTOMER
(
	 ACCOUNT_ID INT AUTO_INCREMENT Not Null,
	 ADMIN BOOLEAN DEFAULT FALSE NOT NULL,
	 EMAIL VARCHAR(50),
	 F_NAME VARCHAR(30) NOT NULL,
	 L_NAME VARCHAR(50) NOT NULL,
	 USERNAME VARCHAR(50) Not Null,
	 PASS VARCHAR(50) Not Null,
	 ACTIVE BOOLEAN DEFAULT TRUE NOT NUll,
	 CREATED_ON DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	 LAST_LOGIN_DATE DATETIME,
	 PRIMARY KEY(ACCOUNT_ID, USERNAME)
);

DROP TABLE IF EXISTS T_CODES;
CREATE TABLE T_CODES
(
	CODE_ID INT AUTO_INCREMENT Not Null,
    CODE_KEY VARCHAR(5) Not null,
	CODE_TYPE VARCHAR(10) Not Null,
	CODE_DESC VARCHAR(1000) Not Null,
	CREATED_ON DATETIME DEFAULT current_timestamp Not Null,
	PRIMARY KEY (CODE_ID)
) ;

DROP TABLE IF EXISTS T_ACCOUNT;
CREATE TABLE T_ACCOUNT
(
	ACCOUNT_ID INT Not Null,
    ACCOUNT_TYPE INT not Null,
 	AMOUNT DOUBLE DEFAULT 0,
 	LAST_UPDATED timestamp DEFAULT current_timestamp,
 	PRIMARY KEY (ACCOUNT_ID, ACCOUNT_TYPE),
 	FOREIGN KEY (ACCOUNT_ID) references T_CUSTOMER (ACCOUNT_ID)
 	ON UPDATE CASCADE,
    FOREIGN KEY (ACCOUNT_TYPE) references T_CODES (CODE_ID)
 	ON UPDATE CASCADE
) ;


DROP TABLE IF EXISTS T_TRANSANCTION_HISTORY;
CREATE TABLE T_TRANSANCTION_HISTORY
(
	ACCOUNT_ID INT Not Null,
	TRANSANCTION_TYPE INT NOT NULL,
	ACCOUNT_TYPE INT,
	AMOUNT DOUBLE,
	TRANSANCTION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	UPDATED_AT DATETIME,
	PRIMARY KEY (ACCOUNT_ID, TRANSANCTION_DATE,TRANSANCTION_TYPE),
    FOREIGN KEY (TRANSANCTION_TYPE) references T_CODES (CODE_ID)
	ON UPDATE CASCADE,
	FOREIGN KEY (ACCOUNT_TYPE) references T_CODES (CODE_ID)
	ON UPDATE CASCADE,
	FOREIGN KEY (ACCOUNT_ID) references T_CUSTOMER (ACCOUNT_ID)
 	ON UPDATE CASCADE
	
	
) ;

DROP TABLE IF EXISTS T_TRANSANCTION_HISTORY_ARCHIVE;
CREATE TABLE T_TRANSANCTION_HISTORY_ARCHIVE
(
	ACCOUNT_ID INT Not Null,
	TRANSANCTION_TYPE INT NOT NULL,
	ACCOUNT_TYPE INT,
	AMOUNT DOUBLE,
	TRANSANCTION_DATE DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	UPDATED_AT DATETIME,
	PRIMARY KEY (ACCOUNT_ID, TRANSANCTION_DATE,TRANSANCTION_TYPE),
    FOREIGN KEY (TRANSANCTION_TYPE) references T_CODES (CODE_ID)
	ON UPDATE CASCADE,
	FOREIGN KEY (ACCOUNT_TYPE) references T_CODES (CODE_ID)
	ON UPDATE CASCADE,
	FOREIGN KEY (ACCOUNT_ID) references T_CUSTOMER (ACCOUNT_ID)
 	ON UPDATE CASCADE
	
	
) ;

#Used for check history
#Contains the Outer join requirement
DROP VIEW IF EXISTS V_USER_TRANS_HISTORY;
CREATE VIEW V_USER_TRANS_HISTORY AS 
SELECT ACCOUNT_ID, A.CODE_DESC AS TRANS_TYPE, B.CODE_DESC AS ACCOUNT_TYPE, AMOUNT, TRANSANCTION_DATE FROM T_TRANSANCTION_HISTORY
LEFT OUTER JOIN T_CODES AS A
ON TRANSANCTION_TYPE = A.CODE_ID
LEFT OUTER JOIN T_CODES AS B
ON ACCOUNT_TYPE = B.CODE_ID;


#Contains the Correlated subquery requirement and the Group By requirement
DROP VIEW IF EXISTS V_PREMIUM_MEMBERS;
CREATE VIEW V_PREMIUM_MEMBERS AS 
SELECT ACCOUNT_ID FROM T_ACCOUNT 
WHERE AMOUNT > (
	SELECT AVG(AMOUNT)/2 + AVG(AMOUNT)
	FROM T_ACCOUNT
	WHERE ACCOUNT_TYPE = 1)
GROUP BY ACCOUNT_ID;



LOAD DATA LOCAL INFILE 'Codes.txt' 
INTO TABLE T_CODES 
FIELDS TERMINATED BY ','
(CODE_KEY, CODE_TYPE, CODE_DESC)
SET CODE_ID = NULL AND CREATED_ON = NULL;

LOAD DATA LOCAL INFILE 'Customers.txt' 
INTO TABLE T_CUSTOMER 
FIELDS TERMINATED BY ','
(F_NAME, L_NAME, EMAIL, USERNAME, PASS)
SET ACCOUNT_ID = NULL AND ADMIN = NULL AND ACTIVE = NULL AND CREATED_ON = NULL AND LAST_LOGIN_DATE = NULL;

#Sets the first user, admin to an admin account
UPDATE T_CUSTOMER SET ADMIN = 1 WHERE ACCOUNT_ID = 1;

LOAD DATA LOCAL INFILE 'Account.txt' 
INTO TABLE T_ACCOUNT 
FIELDS TERMINATED BY ','
(ACCOUNT_ID, ACCOUNT_TYPE)
SET LAST_UPDATED = NULL AND AMOUNT = 0;