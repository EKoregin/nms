CREATE TABLE IF NOT EXISTS model_device (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    type_device VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL DEFAULT 'Generic',
    manufacturer VARCHAR(255) NOT NULL DEFAULT 'Generic'
);

CREATE TABLE IF NOT EXISTS device (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    device_name VARCHAR(255) NOT NULL default '',
    description VARCHAR(255) default '',
    ip inet NOT NULL,
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

CREATE TABLE IF NOT EXISTS checks (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL DEFAULT '',
    model_id INT NOT NULL,
    CONSTRAINT fk_model_device
        FOREIGN KEY (model_id)
            REFERENCES model_device(id)
);

CREATE TABLE IF NOT EXISTS customer_device (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_id INT NOT NULL,
    device_id INT NOT NULL,
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
            REFERENCES customer(id),
    CONSTRAINT fk_device
        FOREIGN KEY (device_id)
            REFERENCES device(id)
);
