package com.mentorlik.mentorlik_backend.domain.mapper;

import com.mentorlik.mentorlik_backend.domain.entity.AdminProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for converting between domain AdminProfile entities and JPA AdminProfile entities.
 * <p>
 * Uses MapStruct to handle the conversion between domain and infrastructure layer entities.
 * This promotes a cleaner separation of concerns and minimizes manual mapping code.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface AdminProfileMapper {

    /**
     * Maps JPA entity to domain entity.
     *
     * @param jpaEntity the JPA entity to map
     * @return the corresponding domain entity
     */
    @Mapping(source = "accessLevel", target = "accessLevel", qualifiedByName = "intToString")
    AdminProfile toDomainEntity(com.mentorlik.mentorlik_backend.model.AdminProfile jpaEntity);

    /**
     * Maps domain entity to JPA entity.
     *
     * @param domainEntity the domain entity to map
     * @return the corresponding JPA entity
     */
    @Mapping(source = "accessLevel", target = "accessLevel", qualifiedByName = "stringToInt")
    com.mentorlik.mentorlik_backend.model.AdminProfile toJpaEntity(AdminProfile domainEntity);

    /**
     * Converts an Integer accessLevel to a String.
     *
     * @param value the integer access level
     * @return the string representation of the access level
     */
    @Named("intToString")
    default String intToString(Integer value) {
        return value == null ? "0" : String.valueOf(value);
    }

    /**
     * Converts a String accessLevel to an Integer.
     *
     * @param value the string access level
     * @return the integer representation of the access level
     */
    @Named("stringToInt")
    default Integer stringToInt(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0; // Default value when parsing fails
        }
    }
} 