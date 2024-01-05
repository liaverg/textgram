CREATE SCHEMA IF NOT EXISTS textgram;
SET SCHEMA 'textgram';

DROP TABLE IF EXISTS users;

CREATE TABLE users(
                      user_id  SERIAL PRIMARY KEY,
                      username VARCHAR(50),
                      password VARCHAR(50),
                      role    VARCHAR(50)
);