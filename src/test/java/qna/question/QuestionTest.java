package qna.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.answer.Answer;
import qna.common.exception.CannotDeleteException;
import qna.user.User;
import qna.user.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private User writer;
    private User stranger;
    private Question question;

    @BeforeEach
    void setUp() {
        // given
        writer = new User(1L, "air", "1234", "junseo", "air.junseo@gmail.com");
        stranger = new User(2L, "curry", "1234", "stephen", "curry@gmail.com");
        question = new Question("Q1", "content").writeBy(writer);
    }

    @Test
    @DisplayName("질문 삭제")
    void delete() {
        // when
        // then
        assertThat(question.isDeleted()).isFalse();
        question.delete(writer);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 한 사용자가 자신이 쓴 질문을 삭제하는 경우")
    void deleteByWriter() {
        // when
        question.delete(writer);

        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 한 사용자가 다른 사람이 쓴 질문을 삭제하는 경우")
    void deleteByStranger() {
        // when
        // then
        assertThatThrownBy(() -> question.delete(stranger))
                .isInstanceOf(CannotDeleteException.class);
        assertThat(question.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("답변이 없는 질문을 삭제하는 경우")
    void deleteNoAnswerQuestion() {
        // when
        question.delete(writer);

        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자와 질문에 달린 모든 답변의 작성자가 질문자인 경우")
    void deleteWriterSameAllAnswers() {
        // given
        Answer writerAnswer1 = new Answer(writer, question, "Answers Contents1");
        Answer writerAnswer2 = new Answer(writer, question, "Answers Contents2");

        // when
        question.addAnswer(writerAnswer1);
        question.addAnswer(writerAnswer2);
        question.delete(writer);

        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자와 답변자가 하나라도 다른 경우 답변을 삭제할 수 없다")
    void deleteWriterNotSameAnswers() {
        // given
        Answer writerAnswer = new Answer(writer, question, "Answers Contents1");
        Answer strangerAnswer = new Answer(stranger, question, "Answers Contents2");

        // when
        // then
        question.addAnswer(writerAnswer);
        question.addAnswer(strangerAnswer);

        assertThatThrownBy(() -> question.delete(writer))
                .isInstanceOf(CannotDeleteException.class);
        assertThat(question.isDeleted()).isFalse();
    }
}
