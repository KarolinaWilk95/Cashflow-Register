CREATE TYPE doc_group AS ENUM('COST', 'SALE');
CREATE TYPE cost AS ENUM('OPERATING' , 'INVESTMENT' , 'FINANCIAL', 'PERSONNEL', 'MATERIAL', 'MARKETING', 'TRANSPORT', 'ADMINISTRATIVE', 'TAX');
CREATE TYPE doc_type AS ENUM('INVOICE', 'BILL', 'DEBIT_NOTE', 'INTEREST_NOTE');


CREATE CAST (varchar AS doc_group) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS cost) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS doc_type) WITH INOUT AS IMPLICIT;

CREATE TABLE documents(
    id BIGSERIAL  PRIMARY KEY,
    document_group doc_group,
    document_type doc_type,
    document_number VARCHAR(255),
    sale_date DATE,
    issue_date DATE,
    due_date DATE,
    contractor_name VARCHAR(255),
    contractor_vat_number BIGINT DEFAULT NULL,
    amount DECIMAL,
    tax_amount DECIMAL,
    total_amount DECIMAL,
    total_amount_in_pln DECIMAL,
    currency_code VARCHAR(5),
    order_number VARCHAR(255) DEFAULT NULL,
    payment_due_date DATE DEFAULT NULL,
    payment_amount DECIMAL DEFAULT NULL,
    amount_due DECIMAL DEFAULT NULL
);

CREATE TABLE payables(
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT REFERENCES documents(id) ON DELETE SET NULL
);

CREATE TABLE receivables(
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT REFERENCES documents(id) ON DELETE SET NULL,
    reminder_number VARCHAR(255),
    demand_for_payment_number VARCHAR(255)
);
