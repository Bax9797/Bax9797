create table `medical_staff`(`name` nvarchar(255),
 `surname` nvarchar(255),
 `medical_specialization` nvarchar(255),
 `animal_specialization` nvarchar (255),
 `rate` nvarchar (255),
 `nip` nvarchar(255),
 `hired` bool);

insert into medical_staff(name,surname,medical_specialization, animal_specialization,rate,nip, hired)
VALUES('Tomasz','Kot','kardiolog','pies','100.00','54366732',1);