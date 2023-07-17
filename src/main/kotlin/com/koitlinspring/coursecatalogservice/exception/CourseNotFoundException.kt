package com.koitlinspring.coursecatalogservice.exception

import java.lang.RuntimeException

class CourseNotFoundException(message: String) : RuntimeException(message) {

}
