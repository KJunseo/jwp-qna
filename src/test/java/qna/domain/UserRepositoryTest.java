package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Nested
    @DisplayName("user 아이디를 통한 유저 조회")
    class FindByUserId {

        @Test
        @DisplayName("존재하는 유저 조회하기")
        void exist() {
            // given
            String userId = UserTest.JAVAJIGI.getUserId();

            // when
            users.save(UserTest.JAVAJIGI);
            Optional<User> findUser = users.findByUserId(userId);

            // then
            assertThat(findUser).isNotEmpty();
        }

        @Test
        @DisplayName("존재하는 않는 유저 조회하기")
        void nonExist() {
            // given
            String userId = UserTest.JAVAJIGI.getUserId();

            // when
            Optional<User> findUser = users.findByUserId(userId);

            // then
            assertThat(findUser).isEmpty();
        }
    }

}