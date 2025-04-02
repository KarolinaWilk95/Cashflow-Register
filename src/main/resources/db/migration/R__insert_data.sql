DELETE FROM payables;
DELETE FROM receivables;
DELETE FROM documents;

INSERT INTO documents (document_group, document_type, document_number, sale_date, issue_date, due_date, contractor_name, contractor_vat_number, amount, tax_amount, total_amount, total_amount_in_pln, currency_code, order_number) VALUES
('COST', 'INVOICE', '1/3/2025', '2025-03-01', '2025-03-01', '2025-03-01', 'EL-GRAW JACEK NAWRACAŁA', 9291304139, 100.00, 2.3, 123, 123, 'PLN', 'UMOWA nr 71'),
('COST', 'INVOICE', '1/Z/3/2025', '2025-03-05', '2025-03-03', '2025-03-17', 'EL-GRAW JACEK NAWRACAŁA', 9291304139, 10.00, 0.23, 10.23, 10.23, 'PLN', null),
('COST', 'INVOICE', '2025/11/M1/0000001', '2025-03-05', '2025-03-05', '2025-03-26', 'Merida', 8990024020, 1503.04, 448.96, 1952 ,1952, 'PLN', null),
('COST', 'INVOICE', '013/14', '2025-03-14', '2025-03-14', '2025-03-21', 'SUKCES', 1232224455, 100.00, 2.3, 102.3,102.3, 'PLN', null),
('COST', 'INVOICE', '1560', '2025-03-03', '2025-03-01', '2025-03-10', 'BEAUTY BRANDS', 1231417982, 105.72, 24.31, 130.03,130.03, 'PLN', null),
('COST', 'INVOICE', '52067/MAG/2025', '2025-03-07', '2025-03-07', '2025-03-07', 'Assarion Wojciech Kobeszko', 5422526603, 151.38, 34.82, 186.20,186.20, 'PLN', null),
('COST', 'INVOICE', 'FS-390396/24/MEPL1/06', '2025-03-16', '2025-03-16', '2025-03-23', 'TERG S.A.', 7671004218, 92.65, 27.67, 120.32,120.32, 'PLN', null),
('COST', 'DEBIT_NOTE', 'CDN-00000144', '2025-03-07', '2025-03-07', '2025-05-07', 'China Manufacture', NULL, 100, 3, 103,103, 'USD', null),
('SALE', 'INVOICE', 'F/241142/02/25', '2025-02-25', '2025-02-25', '2025-02-26', 'EMPIK FOTO', 5252754103, 66.59, 15.32, 81.92,81.92, 'PLN', null),
('SALE', 'INVOICE', 'F/241143/02/25', '2025-02-01', '2025-02-25', '2025-03-23', 'EMPIK FOTO', 5252754103, 100, 2.3, 102.30,102.30, 'PLN', null),
('SALE', 'INTEREST_NOTE', '11/02/2025', '2025-03-02', '2025-03-02', '2025-03-16', 'JOHN DOG GROUP', 7792463741, 218.50, 19, 237.50,237.50, 'PLN', '14602/02/2025'),
('SALE', 'INVOICE', 'F/241144/02/25', '2025-03-02', '2025-03-11', '2025-04-11', 'JOHN DOG GROUP', 7792463741, 300.19, 24.01, 324.20, 324.20, 'PLN', '14602/02/2025')
ON CONFLICT DO NOTHING;

INSERT INTO payables(document_id)
SELECT d.id FROM documents d
WHERE d.document_group = 'COST'
ON CONFLICT DO NOTHING;

INSERT INTO receivables(document_id)
SELECT d.id FROM documents d
WHERE d.document_group = 'SALE'
ON CONFLICT DO NOTHING;

