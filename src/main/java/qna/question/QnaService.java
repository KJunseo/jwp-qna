package qna.question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.common.exception.CannotDeleteException;
import qna.common.exception.NotFoundException;
import qna.deletehistory.DeleteHistory;
import qna.deletehistory.DeleteHistoryService;
import qna.user.User;

import java.util.List;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private final QuestionRepository questionRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        if (question.isDeleted()) {
            throw new NotFoundException();
        }

        return question;
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        question.delete(loginUser);
        List<DeleteHistory> deleteHistories = DeleteHistory.createDeleteHistories(question);
        deleteHistoryService.saveAll(deleteHistories);
    }
}
