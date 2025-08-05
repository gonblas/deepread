package gblas.books.backend.mapper.answer;

import gblas.books.backend.dto.answer.MultipleChoice.MultipleChoiceAnswerRequest;
import gblas.books.backend.dto.answer.MultipleChoice.MultipleChoiceAnswerResponse;
import gblas.books.backend.entity.OptionEntity;
import gblas.books.backend.entity.QuizAttemptEntity;
import gblas.books.backend.entity.answer.MultipleChoiceAnswerEntity;
import gblas.books.backend.entity.question.QuestionEntity;
import gblas.books.backend.mapper.question.QuestionMapperFactory;
import gblas.books.backend.repository.OptionRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", config = AnswerConfig.class)
public abstract class MultipleChoiceAnswerMapper implements TypedAnswerMapper<MultipleChoiceAnswerRequest, MultipleChoiceAnswerEntity, MultipleChoiceAnswerResponse> {

    @Autowired protected OptionRepository optionRepository;

    @Mapping(target = "optionsSelected", source = "entity.optionsSelected", qualifiedByName = "mapOptionIds")
    public abstract MultipleChoiceAnswerResponse toDto(MultipleChoiceAnswerEntity entity, @Context QuestionMapperFactory factory);

    @Named("mapOptionIds")
    protected List<UUID> mapOptionIds(List<OptionEntity> options) {
        return options.stream()
                .map(OptionEntity::getId)
                .collect(Collectors.toList());
    }

    @Mapping(target = "optionsSelected", source = "request.optionIds", qualifiedByName = "mapOption")
    @Mapping(target = "quizAttempt", source = "quizAttempt")
    @Mapping(target = "question", source = "question")
    public abstract MultipleChoiceAnswerEntity toEntity(MultipleChoiceAnswerRequest request, QuizAttemptEntity quizAttempt, QuestionEntity question);

    @Named("mapOption")
    protected List<OptionEntity> mapOption(List<UUID> optionUUIDs) {
        List<OptionEntity> found = (List<OptionEntity>) optionRepository.findAllById(optionUUIDs);
        if (found.size() != optionUUIDs.size()) {
            throw new IllegalArgumentException("Some Option IDs are invalid");
        }
        return found;
    }


    @Override
    public QuestionEntity.QuestionType getAnswerType() {
        return QuestionEntity.QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public Class<MultipleChoiceAnswerRequest> getRequestClass() {
        return MultipleChoiceAnswerRequest.class;
    }

    @Override
    public Class<MultipleChoiceAnswerEntity> getEntityClass() {
        return MultipleChoiceAnswerEntity.class;
    }
}
