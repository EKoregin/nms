alter table check_type
alter column id TYPE BIGINT;

alter table checks
alter column id TYPE BIGINT;

alter table checks
alter column model_device_id TYPE BIGINT;

alter table customer
alter column id TYPE BIGINT;

alter table customer_device
alter column id TYPE BIGINT;

alter table customer_device
alter column customer_id TYPE BIGINT;

alter table customer_device
alter column device_id TYPE BIGINT;

alter table device
alter column id TYPE BIGINT;

alter table device
alter column mac TYPE VARCHAR(17);

alter table device
alter column model_id TYPE BIGINT;

alter table model_check_type
alter column model_device_id TYPE BIGINT;

alter table model_check_type
alter column check_type_id TYPE BIGINT;

alter table model_device
alter column id TYPE BIGINT;

alter table model_device
alter column type_tech_parameter_id TYPE BIGINT;

alter table model_device_ttp
alter column model_device_id TYPE BIGINT;

alter table model_device_ttp
alter column type_tech_parameter_id TYPE BIGINT;

alter table tech_parameter
alter column id TYPE BIGINT;

alter table tech_parameter
alter column customer_id TYPE BIGINT;

alter table tech_parameter
alter column type_tech_parameter_id TYPE BIGINT;

alter table tech_parameter_device
alter column device_id TYPE BIGINT;

alter table tech_parameter_device
alter column tech_parameter_id TYPE BIGINT;

alter table ttp_checks
alter column check_id TYPE BIGINT;

alter table ttp_checks
alter column type_tech_parameter_id TYPE BIGINT;

alter table type_tech_parameter
alter column id TYPE BIGINT;