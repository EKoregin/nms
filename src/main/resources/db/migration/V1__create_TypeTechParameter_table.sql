CREATE TABLE IF NOT EXISTS type_tech_parameter (
    id INTEGER PRIMARY KEY generated always as IDENTITY,
    name VARCHAR(255) not null,
    description varchar(255)
);