CREATE TABLE IF NOT EXISTS ttp_checks (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    type_tech_parameter_id INT NOT NULL,
    check_id INT NOT NULL ,
    CONSTRAINT fk_type_tech_parameter
            FOREIGN KEY (type_tech_parameter_id)
                REFERENCES type_tech_parameter(id),
    CONSTRAINT fk_checks
            FOREIGN KEY (check_id)
                REFERENCES checks(id)
);