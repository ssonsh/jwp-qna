package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Question 도메인 생성 테스트")
    void generate01() {
        // given && when
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);

        em.flush();
        em.clear();

        // then
        Optional<Question> findQ1 = questionRepository.findById(q1.getId());
        Optional<Question> findQ2 = questionRepository.findById(q2.getId());

        assertAll(
            () -> assertTrue(findQ1.isPresent()),
            () -> assertEquals(q1, findQ1.get()),
            () -> assertTrue(findQ2.isPresent())
        );
    }

    @Test
    @DisplayName("삭제되지 않은 Question 도메인 목록을 조회한다.")
    void find01() {
        // given && when
        Q1.delete();
        questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);

        em.flush();
        em.clear();

        // then
        List<Question> questionsByDeleteFalse = questionRepository.findByDeletedFalse();
        assertAll(
            () -> assertThat(questionsByDeleteFalse).hasSize(1),
            () -> assertThat(questionsByDeleteFalse).contains(q2)
        );
    }
}
