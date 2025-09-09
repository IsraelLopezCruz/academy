package com.priceshoes.academy.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Entity
@Table(name = "chapter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = true)
public class Chapter extends PersistableEntity<Long> {
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

    @Column(name = "url_media", nullable = false)
    private String urlMedia;

    @Column(name = "title_resource")
    private String titleResource;

    @Column(name = "url_resource")
    private String urlResource;

    @Column(name = "duration")
    private String duration;

    @Column(name = "main_chapter")
    private boolean mainChapter;

    @ToString.Exclude
    @NonNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false, columnDefinition = "bigint")
    private Course course;

    @ToString.Exclude
    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerCourseChapter> customerCourseChapters;
}