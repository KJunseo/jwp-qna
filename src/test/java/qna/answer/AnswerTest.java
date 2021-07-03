package qna.answer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.question.Question;
import qna.question.QuestionTest;
import qna.user.User;
import qna.user.UserTest;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("질문이 삭제되면 포함되어 있던 답변도 삭제된다.")
    void deleteAnswersIncludedQuestion() {
        // given
        User writer = new User(1L, "air", "1234", "junseo", "air.junseo@gmail.com");
        Question question = new Question("Q1", "content").writeBy(writer);
        Answer writerAnswer1 = new Answer(writer, question, "Answers Contents1");
        Answer writerAnswer2 = new Answer(writer, question, "Answers Contents2");

        // when
        question.addAnswer(writerAnswer1);
        question.addAnswer(writerAnswer2);
        question.delete(writer);

        // then
        assertThat(writerAnswer1.isDeleted()).isTrue();
        assertThat(writerAnswer2.isDeleted()).isTrue();
    }
}
