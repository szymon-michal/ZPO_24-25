-- Insert dummy data for testing

-- Police Officers
INSERT INTO police_officers (service_id, first_name, last_name, badge_number, department, email, phone)
VALUES
    ('PO12345', 'Jan', 'Kowalski', 'BDG001', 'Traffic Department', 'jan.kowalski@police.gov.pl', '+48123456789'),
    ('PO23456', 'Anna', 'Nowak', 'BDG002', 'Traffic Department', 'anna.nowak@police.gov.pl', '+48234567890'),
    ('PO34567', 'Piotr', 'Wiśniewski', 'BDG003', 'Traffic Department', 'piotr.wisniewski@police.gov.pl', '+48345678901'),
    ('PO45678', 'Maria', 'Dąbrowska', 'BDG004', 'Highway Patrol', 'maria.dabrowska@police.gov.pl', '+48456789012'),
    ('PO56789', 'Tomasz', 'Lewandowski', 'BDG005', 'Highway Patrol', 'tomasz.lewandowski@police.gov.pl', '+48567890123');

-- Drivers
INSERT INTO drivers (pesel, license_number, first_name, last_name, date_of_birth, address, email, phone, license_issue_date, license_expiry_date)
VALUES
    ('83020512345', 'DRV001PL', 'Adam', 'Mickiewicz', '1983-02-05', 'ul. Poetycka 12, 00-001 Warszawa', 'adam.mickiewicz@example.com', '+48601234567', '2001-03-15', '2031-03-15'),
    ('75112398765', 'DRV002PL', 'Ewa', 'Słowacka', '1975-11-23', 'ul. Literacka 45, 30-001 Kraków', 'ewa.slowacka@example.com', '+48612345678', '1995-05-20', '2025-05-20'),
    ('91080876543', 'DRV003PL', 'Jakub', 'Norwid', '1991-08-08', 'ul. Romantyczna 67, 50-001 Wrocław', 'jakub.norwid@example.com', '+48623456789', '2010-09-10', '2030-09-10'),
    ('88040554321', 'DRV004PL', 'Alicja', 'Sienkiewicz', '1988-04-05', 'ul. Powieściowa 89, 61-001 Poznań', 'alicja.sienkiewicz@example.com', '+48634567890', '2007-06-12', '2027-06-12'),
    ('95092112345', 'DRV005PL', 'Marek', 'Prus', '1995-09-21', 'ul. Nowelowa 32, 80-001 Gdańsk', 'marek.prus@example.com', '+48645678901', '2014-11-05', '2034-11-05'),
    ('86071067890', 'DRV006PL', 'Karolina', 'Orzeszkowa', '1986-07-10', 'ul. Prozaiczna 54, 71-001 Szczecin', 'karolina.orzeszkowa@example.com', '+48656789012', '2005-08-15', '2025-08-15'),
    ('93031234567', 'DRV007PL', 'Michał', 'Reymont', '1993-03-12', 'ul. Chłopska 76, 20-001 Lublin', 'michal.reymont@example.com', '+48667890123', '2012-04-20', '2032-04-20'),
    ('79122345678', 'DRV008PL', 'Natalia', 'Konopnicka', '1979-12-23', 'ul. Wierszowa 98, 10-001 Białystok', 'natalia.konopnicka@example.com', '+48678901234', '1998-07-25', '2028-07-25');

-- Offenses
INSERT INTO offenses (code, description, penalty_points, min_fine_amount, max_fine_amount, repeatable_offense)
VALUES
    ('SPD-01', 'Exceeding the speed limit by 11-20 km/h', 2, 50.00, 100.00, true),
    ('SPD-02', 'Exceeding the speed limit by 21-30 km/h', 4, 100.00, 200.00, true),
    ('SPD-03', 'Exceeding the speed limit by 31-40 km/h', 6, 200.00, 400.00, true),
    ('SPD-04', 'Exceeding the speed limit by 41-50 km/h', 8, 400.00, 800.00, true),
    ('SPD-05', 'Exceeding the speed limit by more than 50 km/h', 10, 800.00, 1500.00, true),
    ('DWI-01', 'Driving under the influence of alcohol (0.2‰ - 0.5‰)', 6, 500.00, 2500.00, true),
    ('DWI-02', 'Driving under the influence of alcohol (above 0.5‰)', 10, 2500.00, 5000.00, true),
    ('SIG-01', 'Running a red light', 6, 300.00, 500.00, true),
    ('SIG-02', 'Ignoring a stop sign', 5, 200.00, 400.00, true),
    ('BLT-01', 'Not wearing a seatbelt', 2, 100.00, 150.00, false),
    ('HLM-01', 'Not wearing a helmet (motorcycle)', 2, 100.00, 150.00, false),
    ('PHN-01', 'Using a phone while driving', 5, 200.00, 300.00, true),
    ('LNE-01', 'Illegal lane change', 3, 150.00, 250.00, false),
    ('OVT-01', 'Dangerous overtaking', 5, 300.00, 500.00, true),
    ('PED-01', 'Failure to yield to pedestrians', 8, 350.00, 700.00, true),
    ('DOC-01', 'Driving without required documents', 1, 50.00, 100.00, false),
    ('LIC-01', 'Driving without a valid license', 8, 300.00, 600.00, true),
    ('INS-01', 'Driving without insurance', 8, 500.00, 1000.00, true),
    ('VHC-01', 'Driving a vehicle in poor technical condition', 3, 200.00, 400.00, false),
    ('PKG-01', 'Illegal parking', 1, 100.00, 200.00, false);

-- Fines
INSERT INTO fines (fine_number, police_officer_id, driver_id, issue_date, location, total_amount, total_points, status, acceptance_date)
VALUES
    ('F20230001', 1, 1, '2023-01-15 08:30:00', 'ul. Marszałkowska 10, Warszawa', 200.00, 4, 'ACCEPTED', '2023-01-15 08:45:00'),
    ('F20230002', 2, 3, '2023-01-20 15:45:00', 'al. Jerozolimskie 45, Warszawa', 150.00, 3, 'ACCEPTED', '2023-01-20 16:00:00'),
    ('F20230003', 3, 2, '2023-02-05 10:15:00', 'ul. Piotrkowska 76, Łódź', 600.00, 8, 'ACCEPTED', '2023-02-05 10:30:00'),
    ('F20230004', 4, 5, '2023-02-18 12:30:00', 'ul. Długa 15, Gdańsk', 300.00, 6, 'ACCEPTED', '2023-02-18 12:45:00'),
    ('F20230005', 5, 4, '2023-03-10 17:20:00', 'ul. Kościuszki 22, Kraków', 450.00, 5, 'ACCEPTED', '2023-03-10 17:35:00'),
    ('F20230006', 1, 7, '2023-03-25 09:40:00', 'ul. Wojska Polskiego 33, Poznań', 700.00, 10, 'ACCEPTED', '2023-03-25 10:00:00'),
    ('F20230007', 2, 6, '2023-04-08 14:15:00', 'ul. Zwycięstwa 44, Gdynia', 250.00, 5, 'ACCEPTED', '2023-04-08 14:30:00'),
    ('F20230008', 3, 8, '2023-04-22 11:30:00', 'ul. Piłsudskiego 55, Wrocław', 400.00, 8, 'ACCEPTED', '2023-04-22 11:45:00'),
    ('F20230009', 4, 1, '2023-05-12 16:45:00', 'ul. Mickiewicza 66, Katowice', 350.00, 6, 'ACCEPTED', '2023-05-12 17:00:00'),
    ('F20230010', 5, 3, '2023-05-27 13:10:00', 'ul. Słowackiego 77, Szczecin', 180.00, 3, 'ACCEPTED', '2023-05-27 13:25:00'),
    ('F20230011', 1, 2, '2023-06-15 10:20:00', 'ul. Kopernika 88, Białystok', 900.00, 10, 'CANCELED', NULL),
    ('F20230012', 2, 5, '2023-06-30 15:35:00', 'ul. Sienkiewicza 99, Lublin', 320.00, 4, 'ISSUED', NULL),
    ('F20230013', 3, 4, '2023-07-18 09:50:00', 'ul. Opolska 111, Kraków', 550.00, 8, 'ISSUED', NULL),
    ('F20230014', 4, 7, '2023-08-05 14:25:00', 'ul. Grunwaldzka 122, Gdańsk', 150.00, 2, 'ACCEPTED', '2023-08-05 14:40:00'),
    ('F20230015', 5, 6, '2023-08-20 11:40:00', 'ul. Staszica 133, Rzeszów', 480.00, 6, 'ACCEPTED', '2023-08-20 11:55:00');

-- Fine Offenses
INSERT INTO fine_offenses (fine_id, offense_id, amount, points, is_repeat)
VALUES
    (1, 2, 200.00, 4, false),                      -- Adam Mickiewicz, speeding 21-30 km/h
    (2, 13, 150.00, 3, false),                     -- Jakub Norwid, illegal lane change
    (3, 4, 400.00, 8, false),                      -- Ewa Słowacka, speeding 41-50 km/h
    (4, 8, 300.00, 6, false),                      -- Marek Prus, running a red light
    (5, 14, 450.00, 5, false),                     -- Alicja Sienkiewicz, dangerous overtaking
    (6, 7, 700.00, 10, true),                      -- Michał Reymont, driving under influence (>0.5‰)
    (7, 9, 250.00, 5, false),                      -- Karolina Orzeszkowa, ignoring a stop sign
    (8, 15, 400.00, 8, false),                     -- Natalia Konopnicka, failure to yield to pedestrians
    (9, 3, 350.00, 6, false),                      -- Adam Mickiewicz, speeding 31-40 km/h
    (10, 12, 180.00, 3, false),                    -- Jakub Norwid, using phone while driving
    (11, 5, 900.00, 10, false),                    -- Ewa Słowacka, speeding >50 km/h (canceled)
    (12, 10, 100.00, 2, false),                    -- Marek Prus, not wearing seatbelt
    (12, 16, 220.00, 2, false),                    -- Marek Prus, driving without documents
    (13, 18, 550.00, 8, false),                    -- Alicja Sienkiewicz, driving without insurance
    (14, 1, 150.00, 2, true),                      -- Michał Reymont, speeding 11-20 km/h
    (15, 3, 300.00, 6, false),                     -- Karolina Orzeszkowa, speeding 31-40 km/h
    (15, 10, 180.00, 0, true);                     -- Karolina Orzeszkowa, not wearing seatbelt (repeat offense)