package pl.kurs.java.test.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visit")
@Accessors(chain = true)
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "doctor")
    private Doctor doctor;
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "patient")
    private Patient patient;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "meeting_date")
    private LocalDateTime meetingDate;
    @Column(name = "token_id")
    private int tokenId;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Visit(Doctor doctor, Patient patient, LocalDateTime meetingDate, int tokenId, Status status) {
        this.doctor = doctor;
        this.patient = patient;
        this.meetingDate = meetingDate;
        this.tokenId = tokenId;
        this.status = status;
    }

}
