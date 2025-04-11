CREATE TYPE doc_group AS ENUM('COST', 'SALE');
CREATE TYPE cost AS ENUM('OPERATING' , 'INVESTMENT' , 'FINANCIAL', 'PERSONNEL', 'MATERIAL', 'MARKETING', 'TRANSPORT', 'ADMINISTRATIVE', 'TAX');
CREATE TYPE doc_type AS ENUM('INVOICE', 'BILL', 'DEBIT_NOTE', 'INTEREST_NOTE');


CREATE CAST (varchar AS doc_group) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS cost) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS doc_type) WITH INOUT AS IMPLICIT;

CREATE TABLE documents(
    id BIGSERIAL  PRIMARY KEY,
    document_group doc_group NOT NULL,
    document_type doc_type NOT NULL,
    document_number VARCHAR(255) NOT NULL,
    sale_date DATE NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE,
    contractor_name VARCHAR(255),
    contractor_vat_number BIGINT DEFAULT 0 NOT NULL,
    amount DECIMAL DEFAULT 0 NOT NULL,
    tax_amount DECIMAL DEFAULT 0 NOT NULL,
    total_amount DECIMAL DEFAULT 0 NOT NULL,
    total_amount_in_pln DECIMAL DEFAULT 0 NOT NULL,
    currency_code VARCHAR(5) NOT NULL,
    order_number VARCHAR(255) DEFAULT '' NOT NULL,
    payment_due_date DATE DEFAULT NULL,
    payment_amount DECIMAL DEFAULT 0 NOT NULL);

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
