package com.koitlinspring.coursecatalogservice.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GreetingService {

    //	Put the profile. Is in the application.yml that we configured
    @Value("\${message}") // pop up the value of the yml
    lateinit var message: String

    fun retriveGreeting(name: String) = "Hello $name, $message";
}