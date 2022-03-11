
insert into token_generator(code,expire_date)
VALUES('token12345','2022-07-15 10:00:00');
insert into token_generator(code,expire_date)
VALUES('token1','2022-07-15 10:00:00');

insert into medical_staff(name,surname,medical_specialization, animal_specialization,rate,nip, hired)
VALUES('Tomasz','Kot','kardiolog','pies','100.00','54366732',1);

insert into patient_list(animal_name,animal_species,animal_breed,age,owner_name,owner_surname,email,current_customer)
VALUES('Kapsel','pies','dog',5,'Igor','Kot','mts@gmail.com',1);

insert into visit(doctor_id,patient_id,start_visit,end_visit,token_id,status)
VALUES(1,1,'2022-07-15 16:00:00','2022-07-15 17:00:00',1,'BOOKED');
insert into visit(doctor_id,patient_id,start_visit,end_visit,token_id,status)
VALUES(1,1,'2022-07-15 18:00:00','2022-07-15 19:00:00',2,'BOOKED');






