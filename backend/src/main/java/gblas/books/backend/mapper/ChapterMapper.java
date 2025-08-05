package gblas.books.backend.mapper;

import gblas.books.backend.dto.ChapterRequest;
import gblas.books.backend.dto.ChapterResponse;
import gblas.books.backend.dto.UpdateChapterRequest;
import gblas.books.backend.entity.BookEntity;
import gblas.books.backend.entity.ChapterEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChapterMapper {

    ChapterMapper INSTANCE = Mappers.getMapper(ChapterMapper.class);

    @Mapping(target = "id", source = "id")
    ChapterResponse toDto(ChapterEntity chapter);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "book", source = "book")
    @Mapping(target = "title", source = "request.title")
    @Mapping(target = "quizBidirectional", ignore = true)
    ChapterEntity toEntity(ChapterRequest request, BookEntity book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "quizBidirectional", ignore = true)
    void changeEntity(ChapterRequest request, @MappingTarget ChapterEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "quizBidirectional", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateChapterRequest request, @MappingTarget ChapterEntity entity);

}
