package io.github.jinahya.rickandmortyapi.spring.data.jpa.repository;

import io.github.jinahya.rickandmortyapi.persistence._BaseEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

import java.util.Objects;
import java.util.function.Function;

@EntityScan(basePackageClasses = {_BaseEntity.class})
@DataJpaTest
@SuppressWarnings({
        "java:S119" // Type parameter names should comply with a naming convention
})
abstract class _BaseEntityRepository_DataJpaTest<
        REPOSITORY extends _BaseEntityRepository<ENTITY, ID>,
        ENTITY extends _BaseEntity<ID>,
        ID
        > {

    // -----------------------------------------------------------------------------------------------------------------
    _BaseEntityRepository_DataJpaTest(final Class<REPOSITORY> repositoryClass, final Class<ENTITY> entityClass,
                                      final Class<ID> idClass) {
        super();
        this.repositoryClass = Objects.requireNonNull(repositoryClass, "repositoryClass is null");
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass is null");
        this.idClass = Objects.requireNonNull(idClass, "idClass is null");
    }

    _BaseEntityRepository_DataJpaTest(final Class<REPOSITORY> repositoryClass, final Class<ENTITY> entityClass) {
        this(repositoryClass,
             entityClass,
             _BaseEntityRepository_TestUtils.resolveIdClass(entityClass)
        );
    }

    _BaseEntityRepository_DataJpaTest(final Class<REPOSITORY> repositoryClass) {
        this(repositoryClass,
             _BaseEntityRepository_TestUtils.resolveEntityClass(repositoryClass)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void __removeMe() {
    }

    // ---------------------------------------------------------------------------------------------- repositoryInstance

    /**
     * Returns the result of the specified function applied an auto-wired instance of {@link REPOSITORY}.
     *
     * @param function the function to apply to the repository instance.
     * @param <R>      result type parameter.
     * @return the result of the {@code function} applied to the repository instance.
     */
    <R> R applyRepositoryInstance(final Function<? super REPOSITORY, ? extends R> function) {
        return Objects.requireNonNull(function, "function is null")
                .apply(repositoryInstance);
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<REPOSITORY> repositoryClass;

    final Class<ENTITY> entityClass;

    final Class<ID> idClass;

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private REPOSITORY repositoryInstance;
}
