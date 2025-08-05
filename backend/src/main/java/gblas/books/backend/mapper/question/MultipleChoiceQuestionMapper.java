package gblas.books.backend.mapper.question;

import gblas.books.backend.dto.OptionRequest;
import gblas.books.backend.dto.OptionResponse;
import gblas.books.backend.dto.question.MultipleChoice.MultipleChoiceQuestionRequest;
import gblas.books.backend.dto.question.MultipleChoice.MultipleChoiceQuestionResponse;
import gblas.books.backend.entity.OptionEntity;
import gblas.books.backend.entity.question.MultipleChoiceQuestionEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.mapper.OptionMapper;
import gblas.books.backend.repository.OptionRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", config = QuestionConfig.class)
public abstract class MultipleChoiceQuestionMapper implements TypedQuestionMapper<MultipleChoiceQuestionRequest, MultipleChoiceQuestionEntity, MultipleChoiceQuestionResponse> {

    @Autowired
    protected OptionRepository optionRepository;

    @Mapping(target = "options", source = "options", qualifiedByName = "mapOptionToResponse")
    public abstract MultipleChoiceQuestionResponse toDto(MultipleChoiceQuestionEntity entity);

    @Named("mapOptionToResponse")
    List<OptionResponse> optionToDto(List<OptionEntity> options) {
        return options.stream()
                .map(OptionMapper.INSTANCE::toDto)
                .toList();
    }

    @Mapping(target = "options", source = "request.options", qualifiedByName = "mapOptionToEntity")
    @Mapping(target = "quiz", ignore = true)
    public abstract MultipleChoiceQuestionEntity toEntity(MultipleChoiceQuestionRequest request);

    @Named("mapOptionToEntity")
    public List<OptionEntity> optionToEntity(List<OptionRequest> options) {
        return options.stream()
                .map(OptionMapper.INSTANCE::toEntity)
                .toList(); // no seteamos la question acá
    }

    @AfterMapping
    protected void linkOptionsToQuestion(@MappingTarget MultipleChoiceQuestionEntity question) {
        if (question.getOptions() != null) {
            for (OptionEntity option : question.getOptions()) {
                option.setQuestion(question); // se hace al final del mapeo
            }
        }
    }

    public QuestionEntity.QuestionType getQuestionType() {
        return QuestionEntity.QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public Class<MultipleChoiceQuestionRequest> getRequestClass() {
        return MultipleChoiceQuestionRequest.class;
    }

    @Override
    public Class<MultipleChoiceQuestionEntity> getEntityClass() {
        return MultipleChoiceQuestionEntity.class;
    }
}
