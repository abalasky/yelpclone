CREATE TABLE users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password CHAR(72) NOT NULL,
    email VARCHAR(100) NOT NULL
);


CREATE TABLE favorites(
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    favorite VARCHAR(100) NOT NULL,
    FOREIGN KEY (username)
        REFERENCES users(username)
        ON DELETE CASCADE
);


-- Queries
INSERT INTO users (username, password, email) VALUES("abalasky", "2520", "abalasky@gmail.com");

INSERT INTO users (username, password, email) VALUES("admin", "admin", "abalasky@gmail.com");

INSERT INTO favorites (username, favorite) VALUES("abalasky", "12354");

SELECT * FROM favorites WHERE username='Alexander Balasky';
"SELECT favorite FROM favorites WHERE username='Alexander Balasky'"

-- user csci, database saleats
mysql -u csci -h 165.227.63.76 -p
spring.datasource.url=jdbc:mysql://165.227.63.76:3306/saleats
spring.datasource.username=csci
spring.datasource.password=Csci201!
