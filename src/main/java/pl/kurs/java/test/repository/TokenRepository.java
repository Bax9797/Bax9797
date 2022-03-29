package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.java.test.entity.Token;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> getByCode(String code);

    @Transactional
    @Query("select id from Token where code=?1")
    int findIdByCode(String code);
}
