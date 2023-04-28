package com.floney.floney.book.entity;

import com.floney.floney.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Category extends BaseEntity {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    @Builder
    public Category(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, Category parent) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.parent = parent;
    }

    public static Category rootParent() {
        return new Category();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) && Objects.equals(parent, category.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parent);
    }
}
