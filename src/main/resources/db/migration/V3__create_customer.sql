CREATE TABLE IF NOT EXISTS customer (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_name VARCHAR(255) NOT NULL,
    address VARCHAR(500) DEFAULT ''
);

INSERT INTO customer (customer_name, address) values ('default_customer', 'default_address');

ALTER TABLE tech_parameter
    ADD COLUMN customer_id INT NOT NULL default 1,
    ADD CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id);