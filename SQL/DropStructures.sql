-- Cleanup script to reset the database
-- This will drop all tables and recreate the schema without dummy data

-- Drop all tables if they exist
DROP TABLE IF EXISTS fine_offenses CASCADE;
DROP TABLE IF EXISTS fines CASCADE;
DROP TABLE IF EXISTS offenses CASCADE;
DROP TABLE IF EXISTS drivers CASCADE;
DROP TABLE IF EXISTS police_officers CASCADE;

-- Drop views if they exist
DROP VIEW IF EXISTS police_officer_fines;
DROP VIEW IF EXISTS driver_fines;
DROP VIEW IF EXISTS driver_total_points;

-- Drop functions and triggers
DROP FUNCTION IF EXISTS update_timestamp() CASCADE;