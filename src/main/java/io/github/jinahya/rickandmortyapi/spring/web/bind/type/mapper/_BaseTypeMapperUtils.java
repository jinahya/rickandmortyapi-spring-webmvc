package io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper;

import io.github.jinahya.rickandmortyapi.persistence._BaseEntity;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type._BaseType;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;

import java.util.Objects;

@SuppressWarnings({
        "java:S101", // Class names should comply with a naming convention
        "java:S119"  // Type parameter names should comply with a naming convention
})
final class _BaseTypeMapperUtils {

    static <TYPE extends _BaseType> Class<TYPE> resolveTypeClass(final Class<?> mapperClass) {
        @SuppressWarnings("unchecked")
        final var typeClass = (Class<TYPE>) ResolvableType.forClass(mapperClass)
                .as(_BaseTypeMapper.class)
                .resolveGeneric(0);
        return Objects.requireNonNull(typeClass, "no <TYPE> class resolved from " + mapperClass);
    }

    static <TYPE extends _BaseType> TYPE newTypeInstance(final Class<?> mapperClass) {
        final var typeClass = resolveTypeClass(mapperClass);
        try {
            @SuppressWarnings("unchecked")
            final var typeInsgtance = (TYPE) ReflectionUtils.accessibleConstructor(typeClass).newInstance();
            return typeInsgtance;
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to instantiate " + typeClass, roe);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <ENTITY extends _BaseEntity<?>> Class<ENTITY> resolveEntityClass(final Class<?> mapperClass) {
        @SuppressWarnings("unchecked")
        final var entityClass = (Class<ENTITY>) ResolvableType.forClass(mapperClass)
                .as(_BaseTypeMapper.class)
                .resolveGeneric(0);
        return Objects.requireNonNull(entityClass, "no <ENTITY> class resolved from " + mapperClass);
    }

    static <ENTITY extends _BaseEntity<?>> ENTITY newEntityInstance(final Class<?> mapperClass) {
        final var entityClass = resolveEntityClass(mapperClass);
        try {
            @SuppressWarnings("unchecked")
            final var entityInstance = (ENTITY) ReflectionUtils.accessibleConstructor(entityClass).newInstance();
            return entityInstance;
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException("failed to instantiate " + entityClass, roe);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    _BaseTypeMapperUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
