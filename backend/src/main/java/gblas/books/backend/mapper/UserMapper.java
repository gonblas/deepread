package gblas.books.backend.mapper;

import gblas.books.backend.dto.UserResponse;
import gblas.books.backend.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source="id", target="id")
    UserResponse toDto(UserEntity entity);

}


