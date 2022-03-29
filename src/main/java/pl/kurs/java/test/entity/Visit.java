package pl.kurs.java.test.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Accessors(chain = true)
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;
    private LocalDateTime startVisit;
    private LocalDateTime endVisit;
    private int tokenId;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Version
    private int version;

    public Visit(Doctor doctor, Patient patient, LocalDateTime startVisit, LocalDateTime endVisit, int tokenId, Status status) {
        this.doctor = doctor;
        this.patient = patient;
        this.startVisit = startVisit;
        this.endVisit = endVisit;
        this.tokenId = tokenId;
        this.status = status;
    }
}
