TRUNCATE TABLE
 fine_offenses ,
 fines ,
 offenses ,
 drivers ,
 police_officers 
 RESTART IDENTITY ;

DROP VIEW IF EXISTS police_officer_fines;
DROP VIEW IF EXISTS driver_fines;
DROP VIEW IF EXISTS driver_total_points;

