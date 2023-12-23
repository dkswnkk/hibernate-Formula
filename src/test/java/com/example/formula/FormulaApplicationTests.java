package com.example.formula;

import com.example.formula.entity.Post;
import com.example.formula.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FormulaApplicationTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long postId = 1L;

    @BeforeEach
    void setUp() {
        // Post 테이블에 레코드 추가
        jdbcTemplate.update("INSERT INTO post (id, title, content) VALUES (?, ?, ?)", 1L, "글 제목", "글 내용");

        // Comment 테이블에 레코드 추가
        jdbcTemplate.update("INSERT INTO comment (id, content, post_id) VALUES (?, ?, ?)", 2L, "첫 번째 댓글", 1);
        jdbcTemplate.update("INSERT INTO comment (id, content, post_id) VALUES (?, ?, ?)", 3L, "두 번째 댓글", 1);
    }

    @Test
    void shouldReturnCorrectCommentCount() {
        // when
        Post post = postRepository.findById(postId).orElseThrow(() -> new AssertionError("Post not found"));

        // then
        Assertions.assertThat(post.getCommentCountFromCollections()).isEqualTo(2);
        Assertions.assertThat(post.getCommentCountFromFormula()).isEqualTo(2);
    }

    @DisplayName("Colletctions.size() 쿼리 확인")
    @Test
    void shouldQueryWhenGettingCountFromCollections() {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AssertionError("Post not found"));

        post.getCommentCountFromCollections();

        /**
         *
         * Hibernate:
         *     select
         *         post0_.id as id1_1_0_,
         *         post0_.content as content2_1_0_,
         *         post0_.title as title3_1_0_,
         *         (select
         *             count(*)
         *         from
         *             comment c
         *         where
         *             c.post_id = post0_.id) as formula1_0_
         *     from
         *         post post0_
         *     where
         *         post0_.id=?
         * Hibernate:
         *     select
         *         comments0_.post_id as post_id3_0_0_,
         *         comments0_.id as id1_0_0_,
         *         comments0_.id as id1_0_1_,
         *         comments0_.content as content2_0_1_,
         *         comments0_.post_id as post_id3_0_1_
         *     from
         *         comment comments0_
         *     where
         *         comments0_.post_id=?
         */
    }

    @DisplayName("Formula 쿼리 확인")
    @Test
    void shouldQueryWhenGettingCountFromFormula() {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AssertionError("Post not found"));

        post.getCommentCountFromFormula();

        /**
         *
         * Hibernate:
         *     select
         *         post0_.id as id1_1_0_,
         *         post0_.content as content2_1_0_,
         *         post0_.title as title3_1_0_,
         *         (select
         *             count(*)
         *         from
         *             comment c
         *         where
         *             c.post_id = post0_.id) as formula1_0_
         *     from
         *         post post0_
         *     where
         *         post0_.id=?
         */
    }
}
