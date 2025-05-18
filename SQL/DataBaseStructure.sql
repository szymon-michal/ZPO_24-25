-- Create database schema for the Fines Management System

-- Drop tables if they exist (for clean reinstallation)
DROP TABLE IF EXISTS fine_offenses CASCADE;
DROP TABLE IF EXISTS fines CASCADE;
DROP TABLE IF EXISTS offenses CASCADE;
DROP TABLE IF EXISTS drivers CASCADE;
DROP TABLE IF EXISTS police_officers CASCADE;

-- Create police_officers table
CREATE TABLE police_officers (
    id BIGSERIAL PRIMARY KEY,
    service_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    badge_number VARCHAR(20) NOT NULL,
    department VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create drivers table
CREATE TABLE drivers (
    id BIGSERIAL PRIMARY KEY,
    pesel VARCHAR(11) UNIQUE NOT NULL,
    license_number VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    license_issue_date DATE NOT NULL,
    license_expiry_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create offenses table
CREATE TABLE offenses (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    description TEXT NOT NULL,
    penalty_points INT NOT NULL,
    min_fine_amount DECIMAL(10, 2) NOT NULL,
    max_fine_amount DECIMAL(10, 2) NOT NULL,
    repeatable_offense BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create fines table
CREATE TABLE fines (
    id BIGSERIAL PRIMARY KEY,
    fine_number VARCHAR(20) UNIQUE NOT NULL,
    police_officer_id BIGINT NOT NULL REFERENCES police_officers(id),
    driver_id BIGINT NOT NULL REFERENCES drivers(id),
    issue_date TIMESTAMP NOT NULL,
    location VARCHAR(255) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    total_points INT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ISSUED', 'ACCEPTED', 'CANCELED')),
    acceptance_date TIMESTAMP,
    cancellation_date TIMESTAMP,
    cancellation_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_police_officer FOREIGN KEY (police_officer_id) REFERENCES police_officers(id),
    CONSTRAINT fk_driver FOREIGN KEY (driver_id) REFERENCES drivers(id)
);

-- Create fine_offenses junction table (for many-to-many relationship)
CREATE TABLE fine_offenses (
    id BIGSERIAL PRIMARY KEY,
    fine_id BIGINT NOT NULL,
    offense_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    points INT NOT NULL,
    is_repeat BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_fine FOREIGN KEY (fine_id) REFERENCES fines(id) ON DELETE CASCADE,
    CONSTRAINT fk_offense FOREIGN KEY (offense_id) REFERENCES offenses(id),
    CONSTRAINT unique_fine_offense UNIQUE (fine_id, offense_id)
);

-- Function to update timestamp
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for updated_at fields
-- CREATE TRIGGER update_police_officers_timestamp
-- BEFORE UPDATE ON police_officers
-- FOR EACH ROW EXECUTE FUNCTION update_timestamp();

-- CREATE TRIGGER update_drivers_timestamp
-- BEFORE UPDATE ON drivers
-- FOR EACH ROW EXECUTE FUNCTION update_timestamp();

-- CREATE TRIGGER update_offenses_timestamp
-- BEFORE UPDATE ON offenses
-- FOR EACH ROW EXECUTE FUNCTION update_timestamp();

-- CREATE TRIGGER update_fines_timestamp
-- BEFORE UPDATE ON fines
-- FOR EACH ROW EXECUTE FUNCTION update_timestamp();

-- Create index for common queries
CREATE INDEX idx_fines_driver_id ON fines(driver_id);
CREATE INDEX idx_fines_police_officer_id ON fines(police_officer_id);
CREATE INDEX idx_fines_status ON fines(status);
CREATE INDEX idx_drivers_pesel ON drivers(pesel);
CREATE INDEX idx_police_officers_service_id ON police_officers(service_id);

-- View for police officers to see issued fines
CREATE OR REPLACE VIEW police_officer_fines AS
SELECT 
    f.id AS fine_id,
    f.fine_number,
    po.service_id AS officer_service_id,
    po.first_name AS officer_first_name,
    po.last_name AS officer_last_name,
    d.license_number AS driver_license,
    d.pesel AS driver_pesel,
    d.first_name AS driver_first_name,
    d.last_name AS driver_last_name,
    f.issue_date,
    f.location,
    f.total_amount,
    f.total_points,
    f.status
FROM fines f
JOIN police_officers po ON f.police_officer_id = po.id
JOIN drivers d ON f.driver_id = d.id;

-- View for drivers to see their fines and points
CREATE OR REPLACE VIEW driver_fines AS
SELECT 
    d.pesel,
    d.license_number,
    d.first_name,
    d.last_name,
    f.fine_number,
    f.issue_date,
    f.location,
    f.total_amount,
    f.total_points,
    f.status,
    po.service_id AS officer_service_id,
    po.first_name AS officer_first_name,
    po.last_name AS officer_last_name
FROM drivers d
JOIN fines f ON d.id = f.driver_id
JOIN police_officers po ON f.police_officer_id = po.id;

-- View for total driver points
CREATE OR REPLACE VIEW driver_total_points AS
SELECT 
    d.pesel,
    d.license_number,
    d.first_name,
    d.last_name,
    SUM(CASE WHEN f.status = 'ACCEPTED' THEN f.total_points ELSE 0 END) AS total_points
FROM drivers d
LEFT JOIN fines f ON d.id = f.driver_id
GROUP BY d.id, d.pesel, d.license_number, d.first_name, d.last_name;