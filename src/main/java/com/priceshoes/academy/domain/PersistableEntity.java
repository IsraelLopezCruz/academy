package com.priceshoes.academy.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy .class)
@ToString
@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PersistableEntity<T extends Serializable> {
    public abstract T getId();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) {
            return false;
        }
        var unproxiedO = Hibernate.unproxy(o);
        var unproxiedT = Hibernate.unproxy(this);
        if (unproxiedO.getClass() != unproxiedT.getClass()) {
            return false;
        }
        PersistableEntity<?> that = (PersistableEntity<?>) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
