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
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @Test
    @DisplayName("삭제되지 않은 모든 질문 리스트 조회하기")
    void findByDeletedFalse() {
        // given
        // when
        questions.save(QuestionTest.Q1);
        questions.save(QuestionTest.Q2);
        List<Question> results = questions.findByDeletedFalse();

        // then
        assertThat(results).usingElementComparatorOnFields("id").isNotNull();
        assertThat(results).usingElementComparatorIgnoringFields("id")
                .contains(QuestionTest.Q1, QuestionTest.Q2);
    }

    @Nested
    @DisplayName("question id로 삭제되지 않은 질문 조회하기")
    class FindByIdAndDeletedFalse {

        @Test
        @DisplayName("존재하는 경우")
        void exist() {
            // given
            // when
            Question question = questions.save(QuestionTest.Q1);
            Optional<Question> result = questions.findByIdAndDeletedFalse(question.getId());

            // then
            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 경우")
        void nonExist() {
            // given
            Long questionId = QuestionTest.Q1.getId();

            // when
            Optional<Question> result = questions.findByIdAndDeletedFalse(questionId);

            // then
            assertThat(result).isEmpty();
        }
    }
}
