CREATE TABLE department (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255)
);

ALTER TABLE employee
    ADD COLUMN department_id BIGINT;

ALTER TABLE employee
    ADD CONSTRAINT fk_employee_department
    FOREIGN KEY (department_id)
    REFERENCES department (id);
