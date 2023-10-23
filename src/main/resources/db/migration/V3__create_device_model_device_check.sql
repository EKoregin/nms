CREATE TABLE IF NOT EXISTS model_device (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    type_device VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL DEFAULT 'Generic',
    manufacturer VARCHAR(255) NOT NULL DEFAULT 'Generic',
    ports INT default 0
);

CREATE TABLE IF NOT EXISTS device (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    device_name VARCHAR(255) NOT NULL default '',
    description VARCHAR(255) default '',
    ip inet NOT NULL UNIQUE,
    login VARCHAR(255) DEFAULT 'admin',
    password VARCHAR(255) DEFAULT 'admin',
    snmp_community VARCHAR(255) DEFAULT 'public',
    snmp_port INT DEFAULT 161,
    manage_protocol VARCHAR(16) DEFAULT 'telnet',
    manage_port INT DEFAULT 23,
    model_id INT NOT NULL,
    CONSTRAINT fk_model_device
        FOREIGN KEY (model_id)
            REFERENCES model_device(id)
);

CREATE TABLE IF NOT EXISTS customer_device (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_id INT NOT NULL,
    device_id INT NOT NULL,
    port INT default 0,
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
            REFERENCES customer(id),
    CONSTRAINT fk_device
        FOREIGN KEY (device_id)
            REFERENCES device(id)
);

CREATE TABLE IF NOT EXISTS checks (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    check_name VARCHAR(255) NOT NULL DEFAULT '',
    check_type VARCHAR(255) NOT NULL DEFAULT 'SNMP',
    snmp_oid VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS model_checks (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    model_id INT NOT NULL,
    check_id INT NOT NULL ,
    CONSTRAINT fk_model_device
        FOREIGN KEY (model_id)
            REFERENCES model_device(id),
    CONSTRAINT fk_checks
        FOREIGN KEY (check_id)
            REFERENCES checks(id)
);