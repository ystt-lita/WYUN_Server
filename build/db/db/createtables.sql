Create Database If Not Exists userservice;
Use userservice;
Create Table If Not Exists users (
    userID Int,
    name VarChar(128),
    Primary Key (userID)
)Default CharSet=utf8 Collate=utf8_bin;