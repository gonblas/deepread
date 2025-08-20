package gblas.books.backend.entity.question;

import gblas.books.backend.dto.answer.AnswerRequest;
import gblas.books.backend.dto.answer.MultipleChoice.MultipleChoiceAnswerRequest;
import gblas.books.backend.entity.OptionEntity;
import gblas.books.backend.entity.answer.AnswerEntity;
import gblas.books.backend.entity.answer.MultipleChoiceAnswerEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Table(name = "multiple_choice_questions")
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChoiceQuestionEntity extends QuestionEntity {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @NotEmpty
    private List<OptionEntity> options = new ArrayList<>();

    @Override
    public Boolean validate(AnswerRequest request) {
        if (!(request instanceof MultipleChoiceAnswerRequest multipleChoiceAnswerRequest)) {
            throw new IllegalArgumentException("Answer must be of type MultipleChoiceAnswerRequest");
        }

        List<UUID> selectedOptionIds = multipleChoiceAnswerRequest.optionIds();

        List<UUID> correctOptionIds = options.stream()
                .filter(OptionEntity::getIsCorrect)
                .map(OptionEntity::getId)
                .toList();

        if (selectedOptionIds.size() != correctOptionIds.size()) {
            return false;
        }

        return new HashSet<>(correctOptionIds).containsAll(selectedOptionIds);
    }

    @Override
    public Boolean validate(AnswerEntity request) {
        if (!(request instanceof MultipleChoiceAnswerEntity multipleChoiceAnswerEntity)) {
            throw new IllegalArgumentException("Answer must be of type MultipleChoiceAnswerRequest");
        }

        List<UUID> selectedOptionIds = multipleChoiceAnswerEntity.getOptionsSelected().stream().map(OptionEntity::getId).toList();

        List<UUID> correctOptionIds = options.stream()
                .filter(OptionEntity::getIsCorrect)
                .map(OptionEntity::getId)
                .toList();

        if (selectedOptionIds.size() != correctOptionIds.size()) {
            return false;
        }

        return new HashSet<>(correctOptionIds).containsAll(selectedOptionIds);
    }

}

