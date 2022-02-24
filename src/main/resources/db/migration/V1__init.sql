create table `medical_staff`( `id` int,
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
`age` bigint NOT NULL,
`owner_name` nvarchar(45) NOT NULL,
`owner_surname` nvarchar(45) NOT NULL,
`email` nvarchar(45) NOT NULL,
`current_customer` tinyint NOT NULL);


CREATE TABLE `visit` (
`id` int AUTO_INCREMENT,
`doctor_id` int,
`patient_id` int,
`meeting_date` datetime,
`token_id` int,
`status` nvarchar(45));

insert into medical_staff(name,surname,medical_specialization, animal_specialization,rate,nip, hired)
VALUES('Tomasz','Kot','kardiolog','pies','100.00','54366732',1);


