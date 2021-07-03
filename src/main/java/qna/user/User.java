package qna.user;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qna.common.BaseEntity;
import qna.common.exception.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "userId")
})
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Column(length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, length = 20)
    private String userId;

    protected User() {
    }

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        super(id);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void update(User loginUser, User target) {
        if (!matchUserId(loginUser.userId)) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.password)) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;
    }

    private boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    public boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.name) &&
                email.equals(target.email);
    }

    public boolean isGuestUser() {
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + super.getId() +
                ", createdAt=" + super.getCreatedAt() +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", updatedAt=" + super.getUpdatedAt() +
                ", userId='" + userId + '\'' +
                '}';
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
