package com.priceshoes.academy.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "customer_course_chapter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = true)
public class CustomerCourseChapter extends PersistableEntity<Long> {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status", columnDefinition = "ENUM('PROGRESS', 'FINISH'")
    private CustomerCourseChapterStatus status;


    @ToString.Exclude
    @NonNull
    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = false, updatable = false, columnDefinition = "bigint")
    private Chapter chapter;

    @ToString.Exclude
    @NonNull
    @ManyToOne
    @JoinColumn(name = "customer_course_id", nullable = false, updatable = false, columnDefinition = "bigint")
    private CustomerCourse customerCourse;


    @Getter
    @AllArgsConstructor
    public enum CustomerCourseChapterStatus {
        PROGRESS, FINISH;
    }
}