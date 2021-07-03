package qna.answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.question.Question;
import qna.question.QuestionRepository;
import qna.user.User;
import qna.user.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        // given
        writer = new User("air", "1234", "junseo", "air.junseo@gmail.com");
        question = new Question("Q1", "content").writeBy(writer);

        users.save(writer);
        questions.save(question);
    }

    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Answer answer = new Answer(writer, question, "Answers Contents1");

        // when
        // then
        assertThat(answer.getCreatedAt()).isNull();
        assertThat(answer.getUpdatedAt()).isNull();

        Answer result = answers.save(answer);

        assertThat(result).isSameAs(answer);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreatedAt()).isAfter(now);
        assertThat(result.getUpdatedAt()).isAfter(now);
    }

    @Test
    @DisplayName("수정 테스트")
    void update() {
        // given
        Answer answer = new Answer(writer, question, "Answers Contents1");

        // when
        Answer result = answers.save(answer);
        result.delete();
        answers.flush();

        // then
        assertThat(result.getCreatedAt()).isNotEqualTo(result.getUpdatedAt());
    }
}
