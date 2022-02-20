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
    @Column(name = "doctor_id")
    private int doctorId;
    @Column(name = "patient_id")
    private int patientId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "meeting_date")
    private LocalDateTime meetingDate;
    @Column(name = "token_id")
    private int tokenId;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Visit(int doctorId, int patientId, LocalDateTime date, int tokenId, Status status) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.meetingDate = date;
        this.tokenId = tokenId;
        this.status = status;
    }
}
