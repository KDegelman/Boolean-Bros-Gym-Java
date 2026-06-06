CREATE DATABASE IF NOT EXISTS sys;
USE sys;

CREATE TABLE IF NOT EXISTS members (
    MemberID INT PRIMARY KEY,
    First_Name VARCHAR(50) NOT NULL,
    Last_Name VARCHAR(50) NOT NULL,
    Date_of_Birth VARCHAR(20) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Phone_Number VARCHAR(25) NOT NULL,
    Gender VARCHAR(20) NOT NULL,
    City_of_Residence VARCHAR(100) NOT NULL
);

INSERT IGNORE INTO members
VALUES
(42069, 'Fake', 'User', '2000-01-01', 'FakeEmail@email.com', '306 123 4567', 'Male', 'Regina');