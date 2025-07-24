package gblas.books.backend.mapper;

import gblas.books.backend.dto.BookRequest;
import gblas.books.backend.dto.BookResponse;
import gblas.books.backend.dto.BookUpdateRequest;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.UserEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "id", source = "id")
    BookResponse toDto(BookEntity entity);

    @Mapping(target="id", ignore = true)
    @Mapping(target="chapters", ignore = true)
    @Mapping(target = "owner", source="user")
    BookEntity toEntity(BookRequest request, UserEntity user);

    @Mapping(target="id", ignore = true)
    @Mapping(target="chapters", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void changeEntity(BookRequest request, @MappingTarget BookEntity entity);

    @InheritConfiguration(name = "changeEntity")
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(BookUpdateRequest request, @MappingTarget BookEntity entity);
}
