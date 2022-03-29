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
@Table(name = "token_generator")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private LocalDateTime expireDate;
    @Version
    private int version;

    public Token(String code, LocalDateTime expireDate) {
        this.code = code;
        this.expireDate = expireDate;
    }
}
