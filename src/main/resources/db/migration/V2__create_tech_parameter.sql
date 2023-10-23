CREATE TABLE IF NOT EXISTS customer (
                                        id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                        customer_name VARCHAR(255) NOT NULL,
                                        address VARCHAR(500) DEFAULT ''
);

INSERT INTO customer (customer_name, address) values ('default_customer', 'default_address');

CREATE TABLE IF NOT EXISTS tech_parameter  (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    value VARCHAR(255),
    customer_id INT NOT NULL default 1
        REFERENCES customer(id),
    type_tech_parameter_id INT not null
            references type_tech_parameter(id)
);