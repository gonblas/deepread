package gblas.books.backend.mapper;

import gblas.books.backend.dto.RegisterRequest;
import gblas.books.backend.dto.UserResponse;
import gblas.books.backend.entity.UserEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source="id", target="id")
    UserResponse toDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(source="password", target="hashedPassword", qualifiedByName = "encode")
    UserEntity toEntity(RegisterRequest request, @Context PasswordEncoder passwordEncoder);

    @Named("encode")
    default String encodePassword(String rawPassword, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(rawPassword);
    }
}


