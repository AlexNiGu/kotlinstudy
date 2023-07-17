package com.koitlinspring.coursecatalogservice.repository

import com.koitlinspring.coursecatalogservice.entity.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int>{
}