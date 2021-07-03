package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @Nested
    @DisplayName("question id로 지워지지 않은 답변 조회")
    class FindByQuestionIdAndDeletedFalse {

        @Test
        @DisplayName("존재하는 경우")
        void exist() {
            // given
            Question question = questions.save(QuestionTest.Q1);
            AnswerTest.A1.setQuestion(question);
            AnswerTest.A2.setQuestion(question);

            // when
            answers.save(AnswerTest.A1);
            answers.save(AnswerTest.A2);
            List<Answer> results = answers.findByQuestionIdAndDeletedFalse(question.getId());

            // then
            assertThat(results).usingElementComparatorOnFields("id").isNotNull();
            assertThat(results).usingElementComparatorIgnoringFields("id")
                    .contains(AnswerTest.A1, AnswerTest.A2);
        }

        @Test
        @DisplayName("존재하지 않는 경우")
        void nonExist() {
            // given
            Long questionId = AnswerTest.A1.getQuestion().getId();

            // when
            List<Answer> results = answers.findByQuestionIdAndDeletedFalse(questionId);

            // then
            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("answer id로 지워지지 않은 답변 조회")
    class FindByIdAndDeletedFalse {

        @Test
        @DisplayName("존재하는 경우")
        void exist() {
            // given
            // when
            Answer answer = answers.save(AnswerTest.A1);
            Optional<Answer> result = answers.findByIdAndDeletedFalse(answer.getId());

            // then
            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 경우")
        void nonExist() {
            // given
            Long questionId = AnswerTest.A1.getQuestion().getId();

            // when
            Optional<Answer> result = answers.findByIdAndDeletedFalse(questionId);

            // then
            assertThat(result).isEmpty();
        }
    }
}
