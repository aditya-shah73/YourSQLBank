FLUSH PRIVILEGES;
SET SQL_SAFE_UPDATES = 0;

LOAD DATA LOCAL INFILE '/Users/aditya/Desktop/College Courses/Fall 2016/CS 157A/Project/Synthetic/Users.txt'
INTO TABLE USER_TB
FIELDS TERMINATED BY ','
(USERNAME, CHECKING_ACC_ID, SAVING_ACC_ID, ADMIN, ACTIVE, F_NAME, L_NAME, PASS);

LOAD DATA LOCAL INFILE '/Users/aditya/Desktop/College Courses/Fall 2016/CS 157A/Project/Synthetic/Accounts.txt'
INTO TABLE ACCOUNT_TB
FIELDS TERMINATED BY ','
(USERNAME, ACC_TYPE, BALANCE);

LOAD DATA LOCAL INFILE '/Users/aditya/Desktop/College Courses/Fall 2016/CS 157A/Project/Synthetic/History.txt'
INTO TABLE HISTORY_TB
FIELDS TERMINATED BY ','
(TRSN_TYPE, TRSN_AMT, USERNAME, ACC_ID);