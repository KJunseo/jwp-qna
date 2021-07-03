package qna.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.user.User;
import qna.user.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    private User writer;

    @BeforeEach
    void setUp() {
        // given
        writer = new User("air", "1234", "junseo", "air.junseo@gmail.com");

        users.save(writer);
    }

    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Question question = new Question("title1", "contents1").writeBy(writer);

        // when
        // then
        assertThat(question.getCreatedAt()).isNull();
        assertThat(question.getUpdatedAt()).isNull();

        Question result = questions.save(question);

        assertThat(result).isSameAs(question);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreatedAt()).isAfter(now);
        assertThat(result.getUpdatedAt()).isAfter(now);
    }

    @Test
    @DisplayName("수정 테스트")
    void update() {
        // given
        Question question = new Question("title1", "contents1").writeBy(writer);

        // when
        Question result = questions.save(question);
        result.delete(writer);
        questions.flush();

        // then
        assertThat(result.getCreatedAt()).isNotEqualTo(result.getUpdatedAt());
    }
}