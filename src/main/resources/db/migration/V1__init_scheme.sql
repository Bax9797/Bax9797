create table `medical_staff`( `id` int NOT NULL AUTO_INCREMENT,
`name` nvarchar(255),
 `surname` nvarchar(255),
 `medical_specialization` nvarchar(255),
 `animal_specialization` nvarchar (255),
 `rate` nvarchar (255),
 `nip` nvarchar(255),
 `hired` bool);

create table `patient_list` (
`id` int NOT NULL AUTO_INCREMENT,
`animal_name` nvarchar(45) NOT NULL,
`animal_species` nvarchar(45) NOT NULL,
`animal_breed` nvarchar(45) NOT NULL,
`age` int NOT NULL,
`owner_name` nvarchar(45) NOT NULL,
`owner_surname` nvarchar(45) NOT NULL,
`email` nvarchar(45) NOT NULL,
`current_customer` bool);


CREATE TABLE `visit` (
`id` int AUTO_INCREMENT,
`doctor_id` int,
`patient_id` int,
`start_visit` datetime,
`end_visit` datetime,
`token_id` int,
`status` nvarchar(45));

CREATE TABLE `token_generator` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` nvarchar(100) NOT NULL,
  `expire_date` datetime NOT NULL);


