ALTER TABLE customer
    ADD COLUMN latitude double precision;

ALTER TABLE customer
    ADD COLUMN longitude double precision;

ALTER TABLE device
    ADD COLUMN latitude double precision;

ALTER TABLE device
    ADD COLUMN longitude double precision;