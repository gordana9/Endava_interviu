INSERT INTO owner (id, name, email) VALUES (1, 'Ana Pop', 'ana.pop@example.com');
INSERT INTO owner (id, name, email) VALUES (2, 'Bogdan Ionescu', 'bogdan.ionescu@example.com');

INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (1, 'VIN12345', 'Dacia', 'Logan', 2018, 1);
INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (2, 'VIN67890', 'VW', 'Golf', 2021, 2);
INSERT INTO car (id, vin, make, model, year_of_manufacture, owner_id) VALUES (3, 'VIN67892', 'VW', 'Golf', 2022, 2);

INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (1, 1, 'Allianz', DATE '2024-01-01', DATE '2024-12-31');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (2, 1, 'Groupama', DATE '2025-01-01', DATE '2025-12-31');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (3, 2, 'Allianz', DATE '2025-03-01', DATE '2026-02-28');
INSERT INTO insurancepolicy (id, car_id, provider, start_date, end_date) VALUES (4, 3, 'Allianz', DATE '2025-03-05', DATE '2026-03-04');

INSERT INTO claim (id, car_id, claim_date, description, amount) VALUES (1, 1, DATE '2025-05-01', 'Accident minor bara spate', 1200.00);

INSERT INTO claim (id, car_id, claim_date, description, amount) VALUES (2, 2, DATE '2025-06-10', 'Parbriz spart', 800.00);


UPDATE insurancepolicy
SET end_date = DATEADD('YEAR', 1, start_date)
WHERE end_date IS NULL;