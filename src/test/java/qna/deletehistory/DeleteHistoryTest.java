package qna.deletehistory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.answer.Answer;
import qna.question.Question;
import qna.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryTest {

    @Test
    @DisplayName("질문으로 DeleteHistory 리스트 생성")
    void createDeleteHistories() {
        // given
        User writer = new User(1L, "air", "1234", "junseo", "air.junseo@gmail.com");
        Question question = new Question("Q1", "content").writeBy(writer);
        Answer writerAnswer1 = new Answer(writer, question, "Answers Contents1");
        Answer writerAnswer2 = new Answer(writer, question, "Answers Contents2");

        // when
        question.addAnswer(writerAnswer1);
        question.addAnswer(writerAnswer2);
        question.delete(writer);
        List<DeleteHistory> deleteHistories = DeleteHistory.createDeleteHistories(question);

        // then
        assertThat(deleteHistories).hasSize(3);
    }

}