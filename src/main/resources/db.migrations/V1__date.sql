 CREATE TABLE `patient_list` (
    `id` int NOT NULL AUTO_INCREMENT,
    `animal_name` varchar(45) NOT NULL,
    `animal_species` varchar(45) NOT NULL,
    `animal_breed` varchar(45) NOT NULL,
    `age` int NOT NULL,
    `owner_name` varchar(45) NOT NULL,
    `owner_surname` varchar(45) NOT NULL,
    `email` varchar(45) NOT NULL,
    `current_customer` tinyint NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email_UNIQUE` (`email`)

CREATE TABLE `medical_staff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `medical_specialization` varchar(45) NOT NULL,
  `animal_specialization` varchar(45) NOT NULL,
  `rate` decimal(10,2) NOT NULL,
  `nip` varchar(45) NOT NULL,
  `hired` tinyint NOT NULL,
  PRIMARY KEY (`id`)

    CREATE TABLE `visit` (
      `id` int NOT NULL AUTO_INCREMENT,
      `doctor_id` int NOT NULL,
      `patient_id` int NOT NULL,
      `meeting_date` datetime NOT NULL,
      `token_id` int NOT NULL,
      `status` varchar(45) NOT NULL,
      PRIMARY KEY (`id`)

insert into medical_staff(name,surname,medical_specialization, animal_specialization,rate,nip, hired)
VALUES('Tomasz','Kot','kardiolog','pies','100.00','54366732',1);

insert into patient_list(animal_name,animal_species,animal_breed,age,owner_name,owner_surname,email,current_customer)
value ('Kapsel','pies','pitbull',3,'test1','test1','mkrolak9797@gmail.com',1)