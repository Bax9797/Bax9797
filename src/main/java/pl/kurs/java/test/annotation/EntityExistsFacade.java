package pl.kurs.java.test.annotation;

import org.springframework.core.GenericTypeResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EntityExistsFacade {

    private Map<String, JpaRepository> map;

    public EntityExistsFacade(Set<JpaRepository> repositories) {
        this.map = repositories.stream().collect(Collectors.toMap(jpaRepository ->
                Arrays.stream(GenericTypeResolver.resolveTypeArguments(jpaRepository.getClass(), JpaRepository.class))
                        .findFirst().get().getSimpleName(), Function.identity()));
    }

    public boolean validation(int id, Class type) {
        return map.get(type.getSimpleName()).existsById(id);
    }
}
