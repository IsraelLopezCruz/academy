package com.priceshoes.academy.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Entity
@Table(name = "course_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = true)
public class CourseCategory extends PersistableEntity<Long> {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "category_id", nullable = false, updatable = false, columnDefinition = "bigint")
    Long categoryId;

    @ManyToOne
    @JoinColumn(name = "course_id", insertable = true, nullable = false, updatable = false, columnDefinition = "bigint")
    Course course;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false, columnDefinition = "bigint")
    Category category;

    @NonNull
    @Column(name = "priority", nullable = false)
    private Integer priority;
}