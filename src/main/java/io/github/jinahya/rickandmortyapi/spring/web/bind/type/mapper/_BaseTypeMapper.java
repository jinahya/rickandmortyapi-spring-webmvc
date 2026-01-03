package io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper;

import io.github.jinahya.rickandmortyapi.persistence._BaseEntity;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type._BaseType;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;

@SuppressWarnings({
        "java:S101", // Class names should comply with a naming convention
        "java:S114", // Interface names should comply with a naming convention
        "java:S119"  // Type parameter names should comply with a naming convention
})
interface _BaseTypeMapper<TYPE extends _BaseType, ENTITY extends _BaseEntity<ID>, ID> {

    // -----------------------------------------------------------------------------------------------------------------
    TYPE fromEntity(ENTITY source, @MappingTarget TYPE target);

    default TYPE fromEntity(ENTITY source) {
        return fromEntity(source, _BaseTypeMapperUtils.newTypeInstance(getClass()));
    }

    @BeforeMapping
    default void beforeMappingFromEntity(final ENTITY source, final TYPE target) {
        // empty
    }

    @AfterMapping
    default void afterMappingFromEntity(final ENTITY source, final TYPE target) {
        // empty
    }

    // -----------------------------------------------------------------------------------------------------------------
    ENTITY toEntity(TYPE source, @MappingTarget ENTITY target);

    default ENTITY toEntity(final TYPE source) {
        return toEntity(source, _BaseTypeMapperUtils.newEntityInstance(getClass()));
    }

    @BeforeMapping
    default void beforeMappingToEntity(final TYPE source, final ENTITY target) {
        // empty
    }

    @AfterMapping
    default void afterMappingToEntity(final TYPE source, final ENTITY target) {
        // empty
    }
}
