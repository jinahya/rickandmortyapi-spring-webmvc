package io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper;

import io.github.jinahya.rickandmortyapi.persistence._BaseEntity;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type._BaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Objects;

@SuppressWarnings({
        "java:S101", // Class names should comply with a naming convention
        "java:S119"  // Type parameter names should comply with a naming convention
})
abstract class _BaseTypeMapperDecorator<
        MAPPER extends _BaseTypeMapper<TYPE, ENTITY, ID>,
        TYPE extends _BaseType,
        ENTITY extends _BaseEntity<ID>,
        ID
        > {

    // -----------------------------------------------------------------------------------------------------------------
    _BaseTypeMapperDecorator(final MAPPER delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    @Qualifier("delegate")
    MAPPER delegate;
}
