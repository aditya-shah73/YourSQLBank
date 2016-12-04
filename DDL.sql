DROP DATABASE IF EXISTS YourSQLBank_DB;
CREATE DATABASE YourSQLBank_DB;
USE YourSQLBank_DB;

DROP TABLE IF EXISTS Account_TB;
CREATE TABLE Account_TB
(
    ACC_ID INT AUTO_INCREMENT NOT NULL,
    USER_ID INT NOT NULL,
    ACC_TYPE VARCHAR(4) NOT NULL,
    BALANCE FLOAT NOT NULL,
    PRIMARY KEY(ACC_ID)
);

DROP TABLE IF EXISTS User_TB;
CREATE TABLE User_TB
(
    USER_ID INT AUTO_INCREMENT NOT NULL,
    USERNAME VARCHAR(50) NOT NULL,
    CHECKING_ACC_ID INT NOT NULL,
    SAVING_ACC_ID INT NOT NULL,
    ADMIN BOOLEAN DEFAULT FALSE NOT NULL,
    ACTIVE BOOLEAN DEFAULT TRUE NOT NULL,
    F_NAME VARCHAR(30) NOT NULL,
    L_NAME VARCHAR(50) NOT NULL,
    PASS VARCHAR(50) NOT NULL,
    CREATED_ON DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY(USER_ID, USERNAME),
    FOREIGN KEY(CHECKING_ACC_ID) REFERENCES Account_TB(ACC_ID),
    FOREIGN KEY(SAVING_ACC_ID) REFERENCES Account_TB(ACC_ID)
);

DROP TABLE IF EXISTS History_TB;
CREATE TABLE History_TB
(
    TRSN_ID INT AUTO_INCREMENT NOT NULL,
    TRSN_TYPE VARCHAR(4) NOT NULL,
    TRSN_AMT FLOAT NOT NULL,
    USER_ID INT NOT NULL,
    ACC_ID INT NOT NULL,
    PRIMARY KEY(TRSN_ID),
    FOREIGN KEY(USER_ID) REFERENCES User_TB(USER_ID),
    FOREIGN KEY(ACC_ID) REFERENCES Account_TB(ACC_ID)
);

DROP TABLE IF EXISTS Account_TB_Archive;
CREATE TABLE Account_TB_Archive
(
    ACC_ID INT AUTO_INCREMENT NOT NULL,
    USER_ID INT NOT NULL,
    ACC_TYPE VARCHAR(4) NOT NULL,
    BALANCE FLOAT NOT NULL,
    PRIMARY KEY(ACC_ID)
);

DROP TABLE IF EXISTS History_TB_Archive;
CREATE TABLE History_TB_Archive
(
    TRSN_ID INT AUTO_INCREMENT NOT NULL,
    TRSN_TYPE VARCHAR(4) NOT NULL,
    TRSN_AMT FLOAT NOT NULL,
    USER_ID INT NOT NULL,
    ACC_ID INT NOT NULL,
    PRIMARY KEY(TRSN_ID),
    FOREIGN KEY(USER_ID) REFERENCES User_TB(USER_ID),
    FOREIGN KEY(ACC_ID) REFERENCES Account_TB_Archive(ACC_ID)
);