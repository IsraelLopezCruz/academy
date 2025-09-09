package com.priceshoes.academy.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Entity
@Table(name = "customer_course")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = true)
public class CustomerCourse extends PersistableEntity<Long> {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NonNull
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status", columnDefinition = "ENUM('PROGRESS', 'FINISH'")
    private CustomerCourseStatus status;

    @Column(name = "url_resource")
    private String urlResource;

    @ToString.Exclude
    @NonNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false, updatable = false, columnDefinition = "bigint")
    private Course course;

    @ToString.Exclude
    @OneToMany(mappedBy = "customerCourse", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerCourseChapter> customerCourseChapters;

    @Getter
    @AllArgsConstructor
    public enum CustomerCourseStatus {
        PROGRESS, FINISH;
    }
}