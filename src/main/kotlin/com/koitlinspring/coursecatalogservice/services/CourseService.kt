package com.koitlinspring.coursecatalogservice.services

import com.koitlinspring.coursecatalogservice.dto.CourseDTO
import com.koitlinspring.coursecatalogservice.entity.Course
import com.koitlinspring.coursecatalogservice.exception.CourseNotFoundException
import com.koitlinspring.coursecatalogservice.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository) {

    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {

        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category)
        }

        courseRepository.save(courseEntity)

        logger.info { "Saved course is: $courseEntity" }

        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category)
        }
    }

    fun retriveAllcourses(): List<CourseDTO> {
        return courseRepository.findAll()
            .map {
                CourseDTO(it.id, it.name, it.category)
            }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        val exisitingCourse = courseRepository.findById(courseId)

        return if (exisitingCourse.isPresent) {
            exisitingCourse.get()
                .let {
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    courseRepository.save(it)
                    CourseDTO(it.id, it.name, it.category)
                }
        }else {
            throw CourseNotFoundException("""
                No course found for the passed in ID: $courseId
            """.trimIndent())
        }

    }

    fun deleteCourse(courseId: Int) {
        val exisitingCourse = courseRepository.findById(courseId)

        return if (exisitingCourse.isPresent) {
            exisitingCourse.get()
                .let {
                    courseRepository.deleteById(courseId)
                }
        }else {
            throw CourseNotFoundException("""
                No course found for the passed in ID: $courseId
             """.trimIndent()
            )
        }

    }
}