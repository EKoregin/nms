CREATE TABLE IF NOT EXISTS type_tech_parameter (
    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL ,
    description VARCHAR(255),
    regex_rule VARCHAR(255)
);