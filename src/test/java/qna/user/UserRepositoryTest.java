package qna.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = new User("air", "1234", "junseo", "air.junseo@gmail.com");

        // when
        // then
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();

        User result = users.save(user);

        assertThat(result).isSameAs(user);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreatedAt()).isAfter(now);
        assertThat(result.getUpdatedAt()).isAfter(now);
    }

    @Test
    @DisplayName("수정 테스트")
    void update() {
        // given
        User user = new User("air", "1234", "junseo", "air.junseo@gmail.com");
        User targetUser = new User("air", "1234", "js", "max9106@naver.com");

        // when
        User result = users.save(user);
        result.update(user, targetUser);
        users.flush();

        // then
        assertThat(result.equalsNameAndEmail(targetUser)).isTrue();
        assertThat(result.getCreatedAt()).isNotEqualTo(result.getUpdatedAt());
    }
}