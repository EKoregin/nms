CREATE TABLE IF NOT EXISTS tech_parameter_device (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    tech_parameter_id INT NOT NULL,
    device_id INT NOT NULL,
    CONSTRAINT fk_tech_parameter
        FOREIGN KEY (tech_parameter_id)
            REFERENCES tech_parameter (id),
    CONSTRAINT fk_device
        FOREIGN KEY (device_id)
            REFERENCES device (id)
);

CREATE TABLE IF NOT EXISTS model_device_ttp (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    model_device_id INT NOT NULL,
    type_tech_parameter_id INT NOT NULL,
    CONSTRAINT fk_model_device
        FOREIGN KEY (model_device_id)
            REFERENCES model_device (id),
    CONSTRAINT fk_type_tech_parameter
        FOREIGN KEY (type_tech_parameter_id)
            REFERENCES type_tech_parameter (id)
);