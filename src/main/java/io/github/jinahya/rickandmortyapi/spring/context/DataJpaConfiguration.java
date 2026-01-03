package io.github.jinahya.rickandmortyapi.spring.context;

import io.github.jinahya.rickandmortyapi.persistence._BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;

@EntityScan(basePackageClasses = {_BaseEntity.class})
@Configuration
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class DataJpaConfiguration {

}
