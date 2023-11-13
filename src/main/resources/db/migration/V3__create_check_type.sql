CREATE TABLE IF NOT EXISTS check_type (
        id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
        type VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS model_check_type (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    model_device_id INT NOT NULL,
    check_type_id INT NOT NULL,
    CONSTRAINT fk_model_device
        FOREIGN KEY (model_device_id)
            REFERENCES model_device(id),
    CONSTRAINT fk_check_type
        FOREIGN KEY (check_type_id)
            REFERENCES check_type(id)
);

INSERT INTO check_type (type) VALUES ('SNMP'), ('TELNET'), ('REST_API');