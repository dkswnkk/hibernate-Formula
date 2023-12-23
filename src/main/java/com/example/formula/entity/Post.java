package com.example.formula.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Formula("(select count(*) from comment c where c.post_id = id)")
    private int commentCountFromFormula;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public int getCommentCountFromCollections() {
        return comments.size();
    }

    public int getCommentCountFromFormula() {
        return this.commentCountFromFormula;
    }
}
