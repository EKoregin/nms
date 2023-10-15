CREATE TABLE IF NOT EXISTS tech_parameter  (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    value VARCHAR(255),
    type_tech_parameter_id INT not null
            references type_tech_parameter(id)
);