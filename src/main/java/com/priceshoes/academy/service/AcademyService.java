package com.priceshoes.academy.service;

import com.priceshoes.academy.converter.*;
import com.priceshoes.academy.domain.*;
import com.priceshoes.academy.exception.CourseNotFoundException;
import com.priceshoes.academy.exception.ChapterNotFoundException;
import com.priceshoes.academy.repository.*;
import com.priceshoes.academy.service.dto.*;
import com.priceshoes.academy.service.response.CustomerCompliedResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.priceshoes.academy.configuration.RedisCacheManagerConfig.*;

@Slf4j
@Service
@AllArgsConstructor
public class AcademyService {
    private final CourseRepository courseRepository;
    private final CustomerCourseRepository customerCourseRepository;
    private final ChapterRepository chapterRepository;
    private final CategoryRepository categoryRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CustomerCourseChapterRepository customerCourseChapterRepository;

    private final Courses2CoursesForCustomerDTO courses2CoursesForCustomerDTO;
    private final Course2CourseForCustomerDTO course2CourseForCustomerDTO;
    private final CourseRequest2Course courseRequest2Course;
    private final CategoryCourseRequest2CategoryCourse categoryCourseRequest2CategoryCourse;
    private final Category2CategoryDTO category2CategoryDTO;
    private final Course2CourseDTO course2CourseDTO;
    private final Chapter2ChapterDTO chapter2ChapterDTO;

    @Caching(cacheable = @Cacheable(value = CATEGORIES))
    public List<CategoryDTO> getCategoriesCourse(){
        var categories = categoryRepository.findAll();
        return categories.stream().sorted(Comparator.comparing(Category::getPriority)).map(category2CategoryDTO).toList();
    }

    @Caching(evict = {@CacheEvict(value = CATEGORIES, allEntries = true)})
    @Transactional
    public void createCategoryCourse(CategoryCourseRequest categoryCourseRequest){
        var categories = categoryRepository.findByPriorityGreaterThanEqual(categoryCourseRequest.getPriority());
        categories.forEach(c -> c.setPriority(c.getPriority() + 1));
        var categoryCourse = categoryCourseRequest2CategoryCourse.apply(categoryCourseRequest);
        categoryRepository.save(categoryCourse);
    }

    @Caching(evict = {
            @CacheEvict(value = CATEGORIES, allEntries = true),
            @CacheEvict(value = COURSES_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSE_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSES, allEntries = true),
            @CacheEvict(value = COURSES_FROM_CATEGORY, allEntries = true),
    })
    @Transactional
    public void updateCategoryCourse(CategoryCourseRequest categoryCourseRequest){
        var category = categoryRepository.findById(categoryCourseRequest.getId());
        category.ifPresentOrElse(c -> {
            c.setTitle(categoryCourseRequest.getTitle());
            if(!c.getPriority().equals(categoryCourseRequest.getPriority())){
                c.setPriority(categoryCourseRequest.getPriority());
                var categories = categoryRepository.findByPriorityGreaterThanEqualAndIdIsNot(categoryCourseRequest.getPriority(), c.getId());
                categories.forEach(ca -> ca.setPriority(ca.getPriority() + 1));
            }
        }, () -> log.info("CategoryCourse not found to update, categoryId:{}", categoryCourseRequest.getId()) );
    }


    @Caching(cacheable = @Cacheable(value = COURSES))
    @Transactional(readOnly = true)
    public List<CourseDTO> getCourses(){
        var courses = courseRepository.findAll();
        return  courses.stream()
                .sorted(Comparator.comparing(c -> {
                    if(!c.getCourseCategory().isEmpty()){
                        return c.getCourseCategory().getFirst().getPriority();
                    }
                    return 10000;
                }))
                .map(course2CourseDTO).toList();
    }

    @Caching(cacheable = @Cacheable(value = COURSES_FROM_CATEGORY))
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesFromCategory(Long categoryId){
        var coursesCategory = courseCategoryRepository.findByCategory_Id(categoryId);
        return  coursesCategory.stream()
                .sorted(Comparator.comparing(CourseCategory::getPriority))
                .map(cc -> course2CourseDTO.apply(cc.getCourse())).toList();
    }

    @Caching(cacheable = @Cacheable(value = CHAPTERS_FROM_COURSE))
    @Transactional(readOnly = true)
    public List<ChapterDTO> getChaptersFromCourse(Long courseId){
        var chapters = chapterRepository.findByCourse_Id(courseId);
        return chapters.stream().map(chapter2ChapterDTO).toList();
    }

    @Caching(evict = {
            @CacheEvict(value = COURSES_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSE_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSES, allEntries = true),
            @CacheEvict(value = COURSES_FROM_CATEGORY, allEntries = true)
    })
    @Transactional
    public void createCourse(CourseRequest courseRequest){
        if(Objects.nonNull(courseRequest.getCourseCategory())){
            courseRequest.getCourseCategory().forEach(cc -> {
                var coursesCategory = courseCategoryRepository.findByCategory_IdAndPriorityGreaterThanEqual(cc.getCategoryId(), cc.getPriority());
                coursesCategory.forEach(c -> c.setPriority(c.getPriority() + 1));
            });
        }
        var course = courseRequest2Course.apply(courseRequest);
        courseRepository.save(course);
    }

    @Caching(evict = {
            @CacheEvict(value = COURSES_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSE_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSES, allEntries = true),
            @CacheEvict(value = COURSES_FROM_CATEGORY, allEntries = true)
    })
    @Transactional
    public void updateCourseInformation(CourseRequest courseRequest){
        var course = courseRepository.findById(courseRequest.getId());
        course.ifPresentOrElse(c -> {
            courseRequest.getCourseCategory().forEach(cc -> {
                var coursesCategory = courseCategoryRepository.findByCategory_IdAndPriorityGreaterThanEqual(cc.getCategoryId(), cc.getPriority());
                coursesCategory.forEach(coc -> coc.setPriority(coc.getPriority() + 1));
            });

            c.getCourseCategory().clear();
            var courseUpdated = updateCourseFromCourseRequest(c, courseRequest);
            courseRepository.save(courseUpdated);
        },() -> log.info("Course not found to update, courseId:{}", courseRequest.getId()));
    }

    private Course updateCourseFromCourseRequest(Course course, CourseRequest courseRequest){
        List<CourseCategory> courseCategory = courseRequest.getCourseCategory().stream()
                .map(c -> CourseCategory.builder().course(course)
                        .categoryId(c.getCategoryId())
                        .priority(c.getPriority())
                        .build())
                .collect(Collectors.toUnmodifiableList());

        course.getCourseCategory().addAll(courseCategory);
        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setUrlImage(courseRequest.getUrlImage());
        course.setUrlResource(courseRequest.getUrlResource());
        course.setDuration(courseRequest.getDuration());
        course.setStatus(Course.CourseStatus.valueOf(courseRequest.getStatus().name()));
        return course;
    }

    @Caching(evict = {
            @CacheEvict(value = COURSES_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSE_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSES, allEntries = true),
            @CacheEvict(value = COURSES_FROM_CATEGORY, allEntries = true),
            @CacheEvict(value = CHAPTERS_FROM_COURSE, allEntries = true)
    })
    @Transactional
    public void addChapterCourse(CourseChapterRequest courseChapterRequest){
        var course = courseRepository.findById(courseChapterRequest.getId());
        course.ifPresentOrElse(c -> {
            var lstCourseChapters = courseChapterRequest.getChapter();
            List<Chapter> lstChapter = lstCourseChapters.stream().map(ch -> Chapter.builder()
                    .id(ch.getId())
                    .title(ch.getTitle())
                    .description(ch.getDescription())
                    .urlMedia(ch.getUrlMedia())
                    .titleResource(ch.getTitleResource())
                    .urlResource(ch.getUrlResource())
                    .duration(ch.getDuration())
                    .course(c)
                    .mainChapter(ch.isMainChapter())
                    .build()).collect(Collectors.toUnmodifiableList());

            c.getChapter().addAll(lstChapter);
            c.setChapters(c.getChapters() + lstChapter.size());
            courseRepository.save(c);
        }, () -> log.info("Course not found to update, courseId:{}", courseChapterRequest.getId()));

    }

    @Caching(evict = {
            @CacheEvict(value = COURSES_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSE_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSES, allEntries = true),
            @CacheEvict(value = COURSES_FROM_CATEGORY, allEntries = true),
            @CacheEvict(value = CHAPTERS_FROM_COURSE, allEntries = true)
    })
    @Transactional
    public void deleteCourse(CourseDeleteRequest courseDeleteRequest){
        var course = courseRepository.findById(courseDeleteRequest.getId());
        course.ifPresentOrElse(courseRepository::delete,
                () -> log.info("Course not found to delete, courseId:{}", courseDeleteRequest.getId()));
    }

    @Caching(evict = {
            @CacheEvict(value = COURSES_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSE_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSES, allEntries = true),
            @CacheEvict(value = COURSES_FROM_CATEGORY, allEntries = true),
            @CacheEvict(value = CHAPTERS_FROM_COURSE, allEntries = true)
    })
    @Transactional
    public void updateChapter(ChapterRequest chapterRequest){
        var chapter = chapterRepository.findById(chapterRequest.getId());
        chapter.ifPresentOrElse(ch -> {
            var chapterUpdated = updateChapter(ch, chapterRequest);
            chapterRepository.save(chapterUpdated);
        },() -> log.info("Chapter not found to update, chapterId:{}", chapterRequest.getId()));
    }

    private Chapter updateChapter(Chapter chapter, ChapterRequest chapterRequest){
        chapter.setTitle(chapterRequest.getTitle());
        chapter.setDescription(chapterRequest.getDescription());
        chapter.setUrlMedia(chapterRequest.getUrlMedia());
        chapter.setTitleResource(chapterRequest.getTitleResource());
        chapter.setUrlResource(chapterRequest.getUrlResource());
        chapter.setDuration(chapterRequest.getDuration());
        chapter.setMainChapter(chapterRequest.isMainChapter());
        return chapter;
    }

    @Caching(evict = {
            @CacheEvict(value = COURSES_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSE_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSES, allEntries = true),
            @CacheEvict(value = COURSES_FROM_CATEGORY, allEntries = true),
            @CacheEvict(value = CHAPTERS_FROM_COURSE, allEntries = true)
    })
    @Transactional
    public void deleteCourseChapters(CourseDeleteRequest courseDeleteRequest){
        var course = courseRepository.findById(courseDeleteRequest.getId());
        course.ifPresentOrElse(c -> {
            var removeListChapters = c.getChapter().stream()
                    .filter(ch -> courseDeleteRequest.getChapter().stream().anyMatch(chh -> chh.getId().equals(ch.getId())))
                    .toList();

                c.getChapter().removeAll(removeListChapters);
                courseRepository.save(c);
                }, () -> log.info("Course not found to delete chapters, courseId:{}", courseDeleteRequest.getId()));
    }


    @Caching(cacheable = @Cacheable(value = COURSES_FOR_CUSTOMER, key = "#customerId"))
    @Transactional(readOnly = true)
    public List<CoursesForCustomerDTO> getCoursesForCustomer(String customerId){
        List<CustomerCourse> lstCustomerCourseId =  customerCourseRepository.findByCustomerIdAndCourseStatus(customerId, Course.CourseStatus.AVAILABLE);
        List<Long> listIds = lstCustomerCourseId.isEmpty()? List.of(0L) : lstCustomerCourseId.stream().map(c -> c.getCourse().getId()).toList();
        List<Course> lstCourse = courseRepository.findByStatusInAndIdNotIn(List.of(Course.CourseStatus.AVAILABLE, Course.CourseStatus.SOON), listIds);

        return courses2CoursesForCustomerDTO.apply(lstCourse, lstCustomerCourseId, customerId);
    }

    @Caching(cacheable = @Cacheable(value = COURSE_FOR_CUSTOMER, key = "#customerId + #courseId"))
    @Transactional(readOnly = true)
    public CourseForCustomerDTO getCourseForCustomer(Long courseId, Long categoryId, String customerId){
        var courseCustomer= courseCategoryRepository.findByCourse_IdAndCategory_Id(courseId, categoryId).orElseThrow(CourseNotFoundException::new);
        var optCustomerCourse = customerCourseRepository.findByCustomerIdAndCourseId(customerId, courseId);

        return course2CourseForCustomerDTO.apply(courseCustomer, optCustomerCourse);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = COURSES_FOR_CUSTOMER, key = "#customrChapterRequest.customerId"),
            @CacheEvict(value = COURSE_FOR_CUSTOMER, key = "#customrChapterRequest.customerId + #customrChapterRequest.courseId")
    })
    public void createCustomerCourseChapter(CustomrChapterRequest customrChapterRequest){
        var customerCourse = customerCourseRepository.findByCustomerIdAndCourseId(customrChapterRequest.getCustomerId(), customrChapterRequest.getCourseId())
                .orElseGet(() -> CustomerCourse.builder()
                        .customerId(customrChapterRequest.getCustomerId())
                        .status(CustomerCourse.CustomerCourseStatus.PROGRESS)
                        .course(courseRepository.findById(customrChapterRequest.getCourseId()).orElseThrow(CourseNotFoundException::new))
                        .customerCourseChapters(new ArrayList<CustomerCourseChapter>())
                        .build());

        Optional<CustomerCourseChapter> customerChapter = customerCourse.getCustomerCourseChapters().stream()
                .filter(ch -> ch.getChapter().getId().equals(customrChapterRequest.getChapterId())).findFirst();

        customerChapter.ifPresentOrElse(c -> c.setStatus(CustomerCourseChapter.CustomerCourseChapterStatus.valueOf(customrChapterRequest.getStatus().name())),
                () -> {
                    var chapter = chapterRepository.findById(customrChapterRequest.getChapterId()).orElseThrow(ChapterNotFoundException::new);
                    customerCourse.getCustomerCourseChapters().add(                     CustomerCourseChapter.builder()
                            .status(CustomerCourseChapter.CustomerCourseChapterStatus.valueOf(customrChapterRequest.getStatus().name()))
                            .chapter(chapter)
                            .customerCourse(customerCourse)
                            .build());
                });

        long countFinish = customerCourse.getCustomerCourseChapters().stream().filter(c -> c.getStatus().equals(CustomerCourseChapter.CustomerCourseChapterStatus.FINISH)).count();
        if(countFinish == customerCourse.getCourse().getChapters()){
            customerCourse.setStatus(CustomerCourse.CustomerCourseStatus.FINISH);
            customerCourse.setUrlResource(customerCourse.getCourse().getUrlResource());
        }
        customerCourseRepository.save(customerCourse);
    }


    @Caching(evict = {
            @CacheEvict(value = CATEGORIES, allEntries = true),
            @CacheEvict(value = COURSES_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSE_FOR_CUSTOMER, allEntries = true),
            @CacheEvict(value = COURSES, allEntries = true),
            @CacheEvict(value = COURSES_FROM_CATEGORY, allEntries = true),
            @CacheEvict(value = CHAPTERS_FROM_COURSE, allEntries = true)
    })
    @Transactional
    public void deleteCache(){}

    public long getTotalSocias(){
        long totalSocias = customerCourseRepository.countByStatus(CustomerCourse.CustomerCourseStatus.FINISH);
        return totalSocias;
    }
    public long getTotalCourses(Long courseId ){
        long totalPersonas = customerCourseRepository.countByStatusAndCourseId(CustomerCourse.CustomerCourseStatus.FINISH, courseId);
        return totalPersonas;
    }
    public List<CustomerCompliedResponse> getListCourses(String customerId){
        List<CustomerCourse> customerComplete = customerCourseRepository.findByStatusAndCustomerId(CustomerCourse.CustomerCourseStatus.FINISH, customerId);
        List<CustomerCompliedResponse> compliedResponse = new ArrayList<>();
        for (CustomerCourse customerCourse : customerComplete) {
            Course course = customerCourse.getCourse();
            compliedResponse.add(new CustomerCompliedResponse(course.getId(),
                    course.getTitle(),course.getDescription(), course.getStatus(),course.getCreatedAt()));

        }
        return compliedResponse;
    }


    public List<ChapterStatusDTO> getChapterStatuses(Long courseId, String customerId ){
        List<CustomerCourseChapter> chapter = customerCourseChapterRepository.findByStatusAndChapter_Course_id_AndCustomerCourse_CustomerId(CustomerCourseChapter.CustomerCourseChapterStatus.FINISH,courseId,customerId);
        List<ChapterStatusDTO> chapterStatuses = new ArrayList<>();
        for(CustomerCourseChapter chapter1 : chapter){
            ChapterStatusDTO dto = new ChapterStatusDTO();
            dto.setId(chapter1.getChapter().getId());
            dto.setStatus(chapter1.getStatus().name());
            dto.setTitle(chapter1.getChapter().getTitle());
            dto.setCustomerId(customerId);
            dto.setCourseId(courseId);
            chapterStatuses.add(dto);
        }
        return chapterStatuses;
    }
}
