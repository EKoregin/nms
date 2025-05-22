CREATE TABLE IF NOT EXISTS port
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(32) not null,
    speed INT not null,
    duplex VARCHAR(16) not null,
    media_type VARCHAR(16) not null,
    type VARCHAR(16) not null,
    model_device_id BIGINT REFERENCES model_device(id),
    device_id BIGINT REFERENCES device(id)
);

CREATE TABLE IF NOT EXISTS link
(
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(16) not null,
    destination VARCHAR(16) not null,
    port_id BIGINT NOT NULL REFERENCES port(id),
    device_id BIGINT REFERENCES device(id),
    customer_id BIGINT REFERENCES customer(id)
);