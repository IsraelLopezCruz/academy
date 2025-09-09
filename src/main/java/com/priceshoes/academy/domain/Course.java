package com.priceshoes.academy.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Entity
@Table(name = "course")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = true)
public class Course extends PersistableEntity<Long> {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NonNull
    @Column(name = "title", nullable = false)
    private String title;

    @NonNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "url_image", nullable = false)
    private String urlImage;

    @Column(name = "url_resource")
    private String urlResource;

    @Column(name = "chapters")
    private Integer chapters;

    @Column(name = "duration")
    private String duration;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseCategory> courseCategory;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status", columnDefinition = "ENUM('AVAILABLE', 'EXPIRED'")
    private CourseStatus status;

    @ToString.Exclude
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapter;

    @ToString.Exclude
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerCourse> customerCourses;

    @Getter
    @AllArgsConstructor
    public enum CourseStatus {
        AVAILABLE, SOON, EXPIRED;
    }
}