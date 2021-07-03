package qna.deletehistory;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qna.answer.Answer;
import qna.common.ContentType;
import qna.question.Question;
import qna.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public static List<DeleteHistory> createDeleteHistories(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        addQuestionDeleteHistory(question, deleteHistories);
        addAnswerDeleteHistories(question.getAnswers(), deleteHistories);
        return deleteHistories;
    }

    private static void addQuestionDeleteHistory(Question question, List<DeleteHistory> deleteHistories) {
        deleteHistories.add(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
    }

    private static void addAnswerDeleteHistories(List<Answer> deletedAnswers, List<DeleteHistory> deleteHistories) {
        deletedAnswers.forEach(answer ->
                deleteHistories.add(
                        new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedBy);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", contentType=" + contentType +
                ", createDate=" + createDate +
                ", deletedBy=" + deletedBy +
                '}';
    }
}
