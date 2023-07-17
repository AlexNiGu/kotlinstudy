package com.koitlinspring.coursecatalogservice.controller

import com.koitlinspring.coursecatalogservice.dto.CourseDTO
//import com.koitlinspring.coursecatalogservice.entity.Course
import com.koitlinspring.coursecatalogservice.services.CourseService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import util.courseDTO
import java.lang.RuntimeException


@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService


    @Test
    fun addCourse_validation() {
        val courseDTO = CourseDTO(
            null,
            "",
            ""
        )

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val responseError = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        // Is the $errors that is put it in  logger.info("ERRORS: $errors") in GlobalErrorHandler
        Assertions.assertEquals("courseDTO.name must not be blank", responseError)
    }

    @Test
    fun addCourse_runtimeException() {
        val courseDTO = CourseDTO(
            null,
            "Ndsadsa",
            "Develo"
        )

        val errorMessage = "Unexpected Error ocurred"
        every { courseServiceMock.addCourse(any()) } throws RuntimeException(errorMessage)

        val responseError = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(errorMessage, responseError)
    }

    @Test
    fun retriveAllCourses() {

        every { courseServiceMock.retriveAllcourses() }.returnsMany(
            listOf(
                courseDTO(id= 1),
                courseDTO(
                    id = 2,
                    name = "Build reactive Microservices using spring webFlux"
                )
            )
        )

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
        Assertions.assertEquals(2, coursesDTO!!.size)
    }

    @Test
    fun updateCourse() {
        // existing course
//        val course = Course(
//            null,
//            "Build RestFulAPI",
//            "Development"
//        )

        every { courseServiceMock.updateCourse(any(), any()) } returns CourseDTO(
            100,
            "Build Restful Api2",
            "Development"
        )

        val courseUpdated = CourseDTO(
            null,
            "Build RestFulAPI4",
            "Development"
        )

        val updatedCourseRequest = webTestClient
            .put()
            .uri("/v1/courses/{course_id}", 100)
            .bodyValue(courseUpdated)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build Restful Api2", updatedCourseRequest!!.name)
    }

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(
            null,
            "Build Restful APIs using SpringBoot and Kotlin",
            "Dilip Sundarraj2"
        )

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

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

        every { courseServiceMock.deleteCourse(any()) } just runs

        webTestClient
            .delete()
            .uri("/v1/courses/{course_id}", 100)
            .exchange()
            .expectStatus().isNoContent

    }
}