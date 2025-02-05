package easy.market.entity;


import easy.market.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Test
    @Commit
    public void UserSaveTest(){
        User user = new User("jeu", "1234", "sd");
        userRepository.save(user);

        User findUser = userRepository.findById(user.getId()).get();
        assertThat(user.getId()).isEqualTo(findUser.getId());
        System.out.println("date= " + user.getCreatedDate());
    }

}