package com.priceshoes.academy.service;

import com.priceshoes.academy.converter.*;
import com.priceshoes.academy.domain.*;
import com.priceshoes.academy.exception.ChapterNotFoundException;
import com.priceshoes.academy.exception.CourseNotFoundException;
import com.priceshoes.academy.repository.*;
import com.priceshoes.academy.service.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademyServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CustomerCourseRepository customerCourseRepository;
    @Mock
    private ChapterRepository chapterRepository;
    @Mock
    private CourseCategoryRepository courseCategoryRepository;
    @Mock
    private Courses2CoursesForCustomerDTO courses2CoursesForCustomerDTO;
    @Mock
    private Course2CourseForCustomerDTO course2CourseForCustomerDTO;
    @Mock
    private CourseRequest2Course courseRequest2Course;
    @Mock
    private Category2CategoryDTO category2CategoryDTO;
    @Mock
    private Course2CourseDTO course2CourseDTO;
    @Mock
    private Chapter2ChapterDTO chapter2ChapterDTO;

    @InjectMocks
    private AcademyService subject;

    @Test
    void getCategoriesCourse() {
        var categorie = Category.builder()
                .id(1L)
                .title("Title")
                .priority(1)
                .build();
        List<Category> lstCategory = List.of(categorie);
        when(categoryRepository.findAll()).thenReturn(lstCategory);
        when(category2CategoryDTO.apply(categorie)).thenReturn(CategoryDTO.builder().categoryId(1L).build());

        var result = subject.getCategoriesCourse();
        assertEquals(result.getFirst().getCategoryId(), categorie.getId());
    }

    @Test
    void updateCategoryCourse() {
        var request = CategoryCourseRequest.builder()
                .id(1L)
                .title("Titile")
                .priority(1)
                .build();
        var category = Category.builder()
                .id(1L)
                .title("Titile")
                .priority(2)
                .build();
        var lstCategoriesPriority = List.of(Category.builder().id(3L).title("Titile").priority(5).build(),
                Category.builder().id(4L).title("Titile").priority(6).build());
        when(categoryRepository.findById(request.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.findByPriorityGreaterThanEqualAndIdIsNot(request.getPriority(), request.getId())).thenReturn(lstCategoriesPriority);

        subject.updateCategoryCourse(request);
        verify(categoryRepository).findByPriorityGreaterThanEqualAndIdIsNot(request.getPriority(), request.getId());
    }

    @Test
    void getCourses() {
        var course = Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        List<Course> lstCourse = List.of(course);
        when(courseRepository.findAll()).thenReturn(lstCourse);
        when(course2CourseDTO.apply(course)).thenReturn(CourseDTO.builder().id(1L).build());

        var result = subject.getCourses();
        assertEquals(result.getFirst().getId(), course.getId());
    }

    @Test
    void getCoursesFromCategory() {
        var categoryId = 1L;
        var course = Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        var courseCategory = CourseCategory.builder()
                .id(1L)
                .course(course)
                .priority(1)
                .build();
        List<CourseCategory> lstCourseCategory = List.of(courseCategory);
        when(courseCategoryRepository.findByCategory_Id(categoryId)).thenReturn(lstCourseCategory);
        when(course2CourseDTO.apply(course)).thenReturn(CourseDTO.builder().id(1L).build());

        var result = subject.getCoursesFromCategory(categoryId);
        assertEquals(result.getFirst().getId(), course.getId());
    }

    @Test
    void getChaptersFromCourse() {
        var courseId = 1L;
        var course = Course.builder()
                .id(courseId)
                .title("Title")
                .description("Description")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        var chapter = Chapter.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .course(course)
                .build();
        List<Chapter> lstChapter = List.of(chapter);
        when(chapterRepository.findByCourse_Id(courseId)).thenReturn(lstChapter);
        when(chapter2ChapterDTO.apply(chapter)).thenReturn(ChapterDTO.builder().chapterId(1L).build());

        var result = subject.getChaptersFromCourse(courseId);
        assertEquals(result.getFirst().getChapterId(), chapter.getId());
    }

    @Test
    void createCourse() {
        var request = CourseRequest.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("urlImage")
                .duration("12min")
                .status(CourseRequest.CourseRequesStatus.AVAILABLE)
                .courseCategory(List.of(CourseCategoryRequest.builder().courseId(1L).categoryId(1L).priority(1).build()))
                .chapter(List.of(ChapterRequest.builder()
                        .title("Title")
                        .description("Description")
                        .urlMedia("urlMedia")
                        .duration("12 min").build())).build();
        var course =Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("url")
                .chapters(1)
                .urlResource("url")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        var chapter = Chapter.builder()
                .id(2L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build();
        course.setChapter(List.of(chapter));
        List<CourseCategory> lstCourseCategory = List.of(CourseCategory.builder().course(course).categoryId(1L).priority(1).build());
        when(courseCategoryRepository.findByCategory_IdAndPriorityGreaterThanEqual(1L, 1)).thenReturn(lstCourseCategory);
        when(courseRequest2Course.apply(request)).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        subject.createCourse(request);
        verify(courseCategoryRepository).findByCategory_IdAndPriorityGreaterThanEqual(1L, 1);
        verify(courseRepository).save(course);
    }

    @Test
    void updateCourseInformation() {
        var request = CourseRequest.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("urlImage")
                .duration("12min")
                .status(CourseRequest.CourseRequesStatus.AVAILABLE)
                .courseCategory(List.of(CourseCategoryRequest.builder().courseId(1L).categoryId(1L).priority(1).build()))
                .chapter(List.of(ChapterRequest.builder()
                        .title("Title")
                        .description("Description")
                        .urlMedia("urlMedia")
                        .duration("12 min").build())).build();
        var course =Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("url")
                .chapters(1)
                .urlResource("url")
                .status(Course.CourseStatus.AVAILABLE)
                .courseCategory(new ArrayList<>())
                .build();
        var chapter = Chapter.builder()
                .id(2L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build();
        course.setChapter(List.of(chapter));
        List<CourseCategory> lstCourseCategory = List.of(CourseCategory.builder().course(course).categoryId(1L).priority(1).build());
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseCategoryRepository.findByCategory_IdAndPriorityGreaterThanEqual(1L, 1)).thenReturn(lstCourseCategory);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        subject.updateCourseInformation(request);
        verify(courseCategoryRepository).findByCategory_IdAndPriorityGreaterThanEqual(1L, 1);
        verify(courseRepository).save(course);
    }

    @Test
    void addChapterCourse() {
        var courseId = 1L;
        var request = CourseChapterRequest.builder()
                .id(1L)
                .chapter(List.of(ChapterRequest.builder()
                        .title("Title")
                        .description("Description")
                        .urlMedia("urlMedia")
                        .duration("12 min").build()))
                .build();
        var course = Course.builder()
                .id(courseId)
                .title("Title")
                .description("Description")
                .status(Course.CourseStatus.AVAILABLE)
                .chapter(new ArrayList<>())
                .chapters(0)
                .build();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        subject.addChapterCourse(request);
        verify(courseRepository).save(course);
    }

    @Test
    void deleteCourse() {
        var request = CourseDeleteRequest.builder()
                .id(1L)
                .build();
        var course = Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .status(Course.CourseStatus.AVAILABLE)
                .chapter(new ArrayList<>())
                .chapters(0)
                .build();
        when(courseRepository.findById(request.getId())).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(any());

        subject.deleteCourse(request);
        verify(courseRepository).delete(any());
    }

    @Test
    void updateChapter() {
        var chapterId = 1L;
        var request = ChapterRequest.builder()
                .id(chapterId)
                .title("Title")
                .description("Description")
                .urlMedia("urlMedia")
                .duration("12 min").build();
        var course = Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .status(Course.CourseStatus.AVAILABLE)
                .chapter(new ArrayList<>())
                .chapters(0)
                .build();
        var chapter = Chapter.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build();
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(chapterRepository.save(chapter)).thenReturn(chapter);

        subject.updateChapter(request);
        verify(chapterRepository).save(chapter);
    }

    @Test
    void deleteCourseChapters() {
        var request = CourseDeleteRequest.builder()
                .id(1L)
                .chapter(List.of(ChapterDeleteRequest.builder().id(1L).build()))
                .build();
        var course = Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .status(Course.CourseStatus.AVAILABLE)
                .chapter(new ArrayList<>())
                .chapters(0)
                .build();
        var chapter = Chapter.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build();
        course.getChapter().add(chapter);
        when(courseRepository.findById(request.getId())).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        subject.deleteCourseChapters(request);
        verify(courseRepository).save(course);
    }

    @Test
    void getCoursesForCustomer() {
        var customerId = "11200004849";
        var course = Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("url")
                .chapters(1)
                .urlResource("url")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        List<Chapter> chapters = List.of(Chapter.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build());
        course.setChapter(chapters);
        List<CustomerCourse> lstCustomerCourse = List.of(CustomerCourse.builder()
                .id(1L)
                .customerId(customerId)
                .status(CustomerCourse.CustomerCourseStatus.PROGRESS)
                .urlResource("url")
                .course(course)
                .build());
        List<Course> lstCourse = List.of(course);
        when(customerCourseRepository.findByCustomerIdAndCourseStatus(customerId, Course.CourseStatus.AVAILABLE)).thenReturn(lstCustomerCourse);
        when(courseRepository.findByStatusInAndIdNotIn(List.of(Course.CourseStatus.AVAILABLE, Course.CourseStatus.SOON), List.of(1L))).thenReturn(lstCourse);
        when(courses2CoursesForCustomerDTO.apply(lstCourse, lstCustomerCourse, customerId))
                .thenReturn(List.of(CoursesForCustomerDTO.builder().customerId(customerId).category(CategoryDTO.builder().categoryId(1L).build()).build()));

        var result = subject.getCoursesForCustomer(customerId);
        assertEquals(result.getFirst().getCustomerId(), customerId);
    }

    @Test
    void getCourseForCustomer() {
        var customerId = "11200004849";
        var courseId = 1L;
        var course = Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("url")
                .chapters(1)
                .urlResource("url")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        List<Chapter> chapters = List.of(Chapter.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build());
        course.setChapter(chapters);
        var customerCourse = CustomerCourse.builder()
                .id(1L)
                .customerId(customerId)
                .status(CustomerCourse.CustomerCourseStatus.PROGRESS)
                .urlResource("url")
                .course(course)
                .build();
        var courseCategory = CourseCategory.builder().id(1L).categoryId(1L).course(course).priority(1).build();
        when(courseCategoryRepository.findByCourse_IdAndCategory_Id(courseId, courseCategory.getCategoryId())).thenReturn(Optional.of(courseCategory));
        when(customerCourseRepository.findByCustomerIdAndCourseId(customerId, courseId)).thenReturn(Optional.of(customerCourse));
        when(course2CourseForCustomerDTO.apply(courseCategory, Optional.of(customerCourse))).thenReturn(CourseForCustomerDTO.builder().courseId(1L).build());

        var result = subject.getCourseForCustomer(courseId, 1L, customerId);
        assertEquals(result.getCourseId(), customerCourse.getId());
    }

    @Test
    void createCustomerCourseChapter_createNewCustomerCourse() {
        var request = CustomrChapterRequest.builder()
                .customerId("11200004849")
                .courseId(1L)
                .chapterId(2L)
                .status(CustomrChapterRequest.CustomrChapterStatusRequest.FINISH)
                .build();
        var course =Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("url")
                .chapters(1)
                .urlResource("url")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        var chapter = Chapter.builder()
                .id(2L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build();
        when(customerCourseRepository.findByCustomerIdAndCourseId(request.getCustomerId(), request.getCourseId())).thenReturn(Optional.empty());
        when(courseRepository.findById(request.getCourseId())).thenReturn(Optional.of(course));
        when(chapterRepository.findById(request.getChapterId())).thenReturn(Optional.of(chapter));


        subject.createCustomerCourseChapter(request);
        verify(customerCourseRepository).save(any(CustomerCourse.class));
    }

    @Test
    void createCustomerCourseChapter_courseNotFound() {
        var request = CustomrChapterRequest.builder()
                .customerId("11200004849")
                .courseId(1L)
                .chapterId(2L)
                .status(CustomrChapterRequest.CustomrChapterStatusRequest.FINISH)
                .build();
        when(customerCourseRepository.findByCustomerIdAndCourseId(request.getCustomerId(), request.getCourseId())).thenReturn(Optional.empty());
        when(courseRepository.findById(request.getCourseId())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> subject.createCustomerCourseChapter(request));
    }

    @Test
    void createCustomerCourseChapter_chapterNotFound() {
        var request = CustomrChapterRequest.builder()
                .customerId("11200004849")
                .courseId(1L)
                .chapterId(2L)
                .status(CustomrChapterRequest.CustomrChapterStatusRequest.FINISH)
                .build();
        var course =Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("url")
                .chapters(1)
                .urlResource("url")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        when(customerCourseRepository.findByCustomerIdAndCourseId(request.getCustomerId(), request.getCourseId())).thenReturn(Optional.empty());
        when(courseRepository.findById(request.getCourseId())).thenReturn(Optional.of(course));
        when(chapterRepository.findById(request.getChapterId())).thenReturn(Optional.empty());

        assertThrows(ChapterNotFoundException.class, () -> subject.createCustomerCourseChapter(request));
    }

    @Test
    void createCustomerCourseChapter_existCustomerCourse() {
        var request = CustomrChapterRequest.builder()
                .customerId("11200004849")
                .courseId(1L)
                .chapterId(2L)
                .status(CustomrChapterRequest.CustomrChapterStatusRequest.FINISH)
                .build();
        var course =Course.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .urlImage("url")
                .chapters(1)
                .urlResource("url")
                .status(Course.CourseStatus.AVAILABLE)
                .build();
        var chapter = Chapter.builder()
                .id(2L)
                .title("Title")
                .description("Description")
                .urlMedia("url")
                .duration("12 min")
                .course(course)
                .build();

        var customerCourse = CustomerCourse.builder()
                .id(1L)
                .customerId("11200004849")
                .status(CustomerCourse.CustomerCourseStatus.PROGRESS)
                .urlResource("urlResource")
                .course(course)
                .build();
        List<CustomerCourseChapter> customerCourseChapter = List.of(CustomerCourseChapter.builder()
                        .id(2L)
                        .chapter(chapter)
                        .status(CustomerCourseChapter.CustomerCourseChapterStatus.PROGRESS)
                        .customerCourse(customerCourse).build());
        customerCourse.setCustomerCourseChapters(customerCourseChapter);
        when(customerCourseRepository.findByCustomerIdAndCourseId(request.getCustomerId(), request.getCourseId())).thenReturn(Optional.of(customerCourse));

        subject.createCustomerCourseChapter(request);
        verify(customerCourseRepository).save(any(CustomerCourse.class));
    }


}
