package com.koitlinspring.coursecatalogservice.controller

import com.koitlinspring.coursecatalogservice.dto.CourseDTO
import com.koitlinspring.coursecatalogservice.services.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController(val courseService: CourseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Valid courseDTO: CourseDTO): CourseDTO {
        return courseService.addCourse(courseDTO)
    }

    @GetMapping
    fun retrieveAllCourses(): List<CourseDTO>
    = courseService.retriveAllcourses()

    @PutMapping("/{course_id}")
    fun updatedCourse(
        @RequestBody courseDTO: CourseDTO,
        @PathVariable("course_id") courseId : Int
    ): CourseDTO = courseService.updateCourse(courseId, courseDTO)

    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletedCourse(@PathVariable("course_id") courseId: Int)
    = courseService.deleteCourse(courseId)

}