package qna.question;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qna.answer.Answer;
import qna.common.BaseEntity;
import qna.common.exception.CannotDeleteException;
import qna.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Question extends BaseEntity {
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this(id, false, title, contents);
    }

    public Question(Long id, boolean deleted, String title, String contents) {
        super(id);
        this.deleted = deleted;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
    }

    public void delete(User loginUser) {
        checkAuthority(loginUser);
        this.deleted = true;
        deleteIncludedAnswer();
    }

    private void checkAuthority(User loginUser) {
        validatesQuestionOwner(loginUser);
        answers.forEach(answer -> validatesAnswerOwner(loginUser, answer));
    }

    private void validatesQuestionOwner(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private void validatesAnswerOwner(User loginUser, Answer answer) {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private void deleteIncludedAnswer() {
        answers.forEach(Answer::delete);
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(answers);
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + super.getId() +
                ", contents='" + contents + '\'' +
                ", createdAt=" + super.getCreatedAt() +
                ", deleted=" + deleted +
                ", title='" + title + '\'' +
                ", updatedAt=" + super.getUpdatedAt() +
                ", writer=" + writer +
                ", answers=" + answers +
                '}';
    }
}
