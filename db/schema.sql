CREATE TABLE IF NOT EXISTS posts (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT
);

CREATE TABLE IF NOT EXISTS candidates (
   id SERIAL PRIMARY KEY,
   name TEXT,
   photo_id int REFERENCES photos(id)
);

CREATE TABLE IF NOT EXISTS photos (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255),
   email VARCHAR(255),
   password VARCHAR(255)
);