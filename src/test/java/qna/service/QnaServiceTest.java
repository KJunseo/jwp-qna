package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    @DisplayName("question id를 통해 질문 찾기")
    void findQuestionByIdExist() {
        // given
        given(questionRepository.findById(question.getId()))
                .willReturn(Optional.of(question));

        // when
        Question question = qnaService.findQuestionById(this.question.getId());

        // then
        assertThat(question).isSameAs(question);
    }

    @Test
    @DisplayName("question id를 통해 이미 deleted 된 질문 찾기")
    void findQuestionByIdAlreadyDeleted() {
        // given
        question.setDeleted(true);
        given(questionRepository.findById(question.getId()))
                .willReturn(Optional.of(question));

        // when
        // then
        assertThatThrownBy(() -> qnaService.findQuestionById(this.question.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 question id를 통해 질문 찾기")
    void findQuestionByIdNonExist() {
        // given
        given(questionRepository.findById(question.getId()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> qnaService.findQuestionById(this.question.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("delete 성공")
    public void deleteSuccess() throws Exception {
        // given
        given(questionRepository.findById(question.getId()))
                .willReturn(Optional.of(question));

        // when
        // then
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    @DisplayName("다른 사람이 쓴 글 삭제하는 경우")
    public void deleteWrittenByOther() {
        // given
        given(questionRepository.findById(question.getId()))
                .willReturn(Optional.of(question));

        // when
        // then
        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문자와 답변자가 같을 때 삭제하는 경우")
    public void deleteSuccessQuestionerSameAnswers() throws Exception {
        // given
        given(questionRepository.findById(question.getId()))
                .willReturn(Optional.of(question));

        // when
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        // then
        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    @DisplayName("답변 중에 다른 사람이 쓴 글이 있을 때 삭제하는 경우")
    public void deleteOtherPersonWriteAnswer() {
        // given
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1");
        question.addAnswer(answer2);
        given(questionRepository.findById(question.getId()))
                .willReturn(Optional.of(question));

        // when
        // then
        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
