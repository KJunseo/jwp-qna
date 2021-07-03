package qna.answer;

import qna.common.BaseEntity;
import qna.common.exception.NotFoundException;
import qna.common.exception.UnAuthorizedException;
import qna.question.Question;
import qna.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity {
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        super(id);

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void delete() {
        this.deleted = true;
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + super.getId() +
                ", contents='" + contents + '\'' +
                ", createdAt=" + super.getCreatedAt() +
                ", deleted=" + deleted +
                ", question=" + question +
                ", updatedAt=" + super.getUpdatedAt() +
                ", writer=" + writer +
                '}';
    }
}
