package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kurs.java.test.entity.Token;

import javax.transaction.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token getByCode(String code);

    @Transactional
    @Query("select id from Token where code=?1")
    int findIdByCode(String code);

    boolean existsByCode(String code);
}
