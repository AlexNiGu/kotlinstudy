package com.koitlinspring.coursecatalogservice

import com.koitlinspring.coursecatalogservice.dto.CourseDTO
import com.koitlinspring.coursecatalogservice.entity.Course
import com.koitlinspring.coursecatalogservice.repository.CourseRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import util.courseEntityList

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var  courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun retriveAllCourses() {
        val coursesDTO = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("INFO")
        println(coursesDTO)
        Assertions.assertEquals(3, coursesDTO!!.size)
    }

    @Test
    fun updateCourse() {
        // existing course
        val course = Course(
            null,
            "Build RestFulAPI",
            "Development"
        )

        courseRepository.save(course)

        // course ID

        // Updated Course
        val courseUpdated = CourseDTO(
            null,
            "Build RestFulAPI4",
            "Development"
        )

        val updatedCourseRequest = webTestClient
            .put()
            .uri("/v1/courses/{course_id}", course.id)
            .bodyValue(courseUpdated)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build RestFulAPI4", updatedCourseRequest!!.name)
    }

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(
            null,
            "Build Restful APIs using SpringBoot and Kotlin",
            "Dilip Sundarraj2"
        )

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("INFOOOOOO")
        println(savedCourseDTO)


        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }


    @Test
    fun deleteCourse() {
        // existing course
        val course = Course(
            null,
            "Build RestFulAPI",
            "Development"
        )

        courseRepository.save(course)

        val updatedCourseRequest = webTestClient
            .delete()
            .uri("/v1/courses/{course_id}", course.id)
            .exchange()
            .expectStatus().isNoContent

    }
}