
insert into token_generator(code,expire_date,version)
VALUES('token12345','2022-07-15 10:00:00',0);
insert into token_generator(code,expire_date,version)
VALUES('token1','2022-07-15 10:00:00',0);
insert into token_generator(code,expire_date,version)
VALUES('token12','2022-07-15 10:00:00',0);
insert into token_generator(code,expire_date,version)
VALUES('token113','2022-07-15 10:00:00',0);

insert into medical_staff(name,surname,medical_specialization, animal_specialization,rate,nip, hired,version)
VALUES('Tomasz','Kot','kardiolog','pies','100.00','54366732',1,0);
insert into medical_staff(name,surname,medical_specialization, animal_specialization,rate,nip, hired,version)
VALUES('Michal','Kowalski','kardiolog','pies','100.00','543667321',1,0);
insert into medical_staff(name,surname,medical_specialization, animal_specialization,rate,nip, hired,version)
VALUES('Piotr','Kowal','neurolog','kot','150.00','543667321221',1,0);

insert into patient_list(animal_name,animal_species,animal_breed,age,owner_name,owner_surname,email,current_customer,version)
VALUES('Kapsel','pies','dog',5,'Igor','Kot','15@gmail.com',1,0);
insert into patient_list(animal_name,animal_species,animal_breed,age,owner_name,owner_surname,email,current_customer,version)
VALUES('Kapsel','pies','dog',5,'Tomek','Kat','mts@gmail.com',1,0);
insert into patient_list(animal_name,animal_species,animal_breed,age,owner_name,owner_surname,email,current_customer,version)
VALUES('Kapsel','pies','dog',5,'Michal','Kowal','11mts@gmail.com',1,0);

insert into visit(doctor_id,patient_id,start_visit,end_visit,token_id,status,version)
VALUES(1,1,'2022-07-15 16:00:00','2022-07-15 17:00:00',1,'BOOKED',0);
insert into visit(doctor_id,patient_id,start_visit,end_visit,token_id,status,version)
VALUES(1,1,'2022-07-15 18:00:00','2022-07-15 19:00:00',2,'BOOKED',0);
insert into visit(doctor_id,patient_id,start_visit,end_visit,token_id,status,version)
VALUES(3,3,'2022-07-15 16:00:00','2022-07-15 17:00:00',3,'BOOKED',0);
insert into visit(doctor_id,patient_id,start_visit,end_visit,token_id,status,version)
VALUES(3,3,'2022-07-15 17:00:00','2022-07-15 18:00:00',4,'BOOKED',0);






