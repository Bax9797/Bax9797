create table `medical_staff`( `id` int NOT NULL AUTO_INCREMENT,
`name` nvarchar(255) NOT NULL,
 `surname` nvarchar(255) NOT NULL,
 `medical_specialization` nvarchar(255) NOT NULL,
 `animal_specialization` nvarchar (255) NOT NULL,
 `rate` nvarchar (255) NOT NULL,
 `nip` nvarchar(255) NOT NULL UNIQUE,
 `hired` bool,
  `version`int NOT NULL);

create table `patient_list` (
`id` int NOT NULL AUTO_INCREMENT,
`animal_name` nvarchar(45) NOT NULL,
`animal_species` nvarchar(45) NOT NULL,
`animal_breed` nvarchar(45) NOT NULL,
`age` int NOT NULL,
`owner_name` nvarchar(45) NOT NULL,
`owner_surname` nvarchar(45) NOT NULL,
`email` nvarchar(45) NOT NULL UNIQUE,
`current_customer` bool,
`version`int NOT NULL);


CREATE TABLE `visit` (
`id` int AUTO_INCREMENT,
`doctor_id` int,
`patient_id` int,
`start_visit` datetime,
`end_visit` datetime,
`token_id` int,
`status` nvarchar(45),
`version`int NOT NULL);

CREATE TABLE `token_generator` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` nvarchar(100) NOT NULL,
  `expire_date` datetime NOT NULL,
  `version`int NOT NULL);


