CREATE TABLE departmentPositions
(
	ID serial PRIMARY KEY,
	DepCode varchar(20),
	DepJob varchar(100),
	Description varchar(255),
	CONSTRAINT depcode_depjob_unique UNIQUE (DepCode, DepJob)
);

INSERT INTO departmentPositions (depcode, depjob, description)
VALUES ('№101', 'Junior Java developer', 'junior'),
('№101', 'Trainee Java developer', 'trainee'),
('№102', 'Middle C++ developer', 'middle'),
('№100', 'Junior C# developer', 'junior');


