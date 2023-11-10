CREATE TABLE IF NOT EXISTS customer
(
    id            INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_name VARCHAR(255) NOT NULL,
    address       VARCHAR(500) DEFAULT ''
);

INSERT INTO customer (customer_name, address)
values ('default_customer', 'default_address');

CREATE TABLE IF NOT EXISTS tech_parameter
(
    id                     INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    value                  VARCHAR(255),
    customer_id            INT NOT NULL default 1
        REFERENCES customer (id),
    type_tech_parameter_id INT not null
        references type_tech_parameter (id)
);

CREATE TABLE IF NOT EXISTS model_device
(
    id           INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    type_device  VARCHAR(255) NOT NULL,
    name         VARCHAR(255) NOT NULL unique,
    manufacturer VARCHAR(255) NOT NULL DEFAULT 'Generic',
    ports        INT                   default 0,
    type_tech_parameter_id INT not null default 0
        REFERENCES type_tech_parameter(id)
);

CREATE TABLE IF NOT EXISTS device
(
    id              INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    device_name     VARCHAR(255) NOT NULL UNIQUE,
    description     VARCHAR(255)          default '',
    ip              inet         NOT NULL UNIQUE,
    mac             CHAR(17)              UNIQUE,
    login           VARCHAR(255)          DEFAULT 'admin',
    password        VARCHAR(255)          DEFAULT 'admin',
    snmp_community  VARCHAR(255)          DEFAULT 'public',
    snmp_port       INT                   DEFAULT 161,
    manage_protocol VARCHAR(16)           DEFAULT 'telnet',
    manage_port     INT                   DEFAULT 23,
    model_id        INT          NOT NULL,
    CONSTRAINT fk_model_device
        FOREIGN KEY (model_id)
            REFERENCES model_device (id)
);

CREATE TABLE IF NOT EXISTS customer_device
(
    id          INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_id INT NOT NULL,
    device_id   INT NOT NULL,
    port        INT default 0,
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
            REFERENCES customer (id),
    CONSTRAINT fk_device
        FOREIGN KEY (device_id)
            REFERENCES device (id)
);

CREATE TABLE IF NOT EXISTS checks
(
    id              INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    check_name      VARCHAR(255) NOT NULL DEFAULT '',
    check_type      VARCHAR(255) NOT NULL DEFAULT 'SNMP',
    snmp_oid        VARCHAR(255),
    telnet_commands TEXT,
    description     TEXT,
    subst_rules     TEXT,
    json_filter     TEXT,
    regex_filter    TEXT,
    model_device_id INT          NOT NULL,
    check_scope     VARCHAR(32)  NOT NULL DEFAULT '',
    CONSTRAINT fk_model_device
        FOREIGN KEY (model_device_id)
            REFERENCES model_device (id)
);

CREATE TABLE IF NOT EXISTS ttp_checks
(
    id                     INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    type_tech_parameter_id INT NOT NULL,
    check_id               INT NOT NULL,
    CONSTRAINT fk_type_tech_parameter
        FOREIGN KEY (type_tech_parameter_id)
            REFERENCES type_tech_parameter (id),
    CONSTRAINT fk_checks
        FOREIGN KEY (check_id)
            REFERENCES checks (id)
);