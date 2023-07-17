package com.koitlinspring.coursecatalogservice.controller

import com.koitlinspring.coursecatalogservice.services.GreetingService
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/greetings")
class GreetingController(val greetingService: GreetingService) {

    // Acces to login class
    companion object: KLogging()

    @GetMapping("/{name}")
    fun retriveGreeting(@PathVariable("name") name: String): String {

        logger.info("Name is $name")
        return greetingService.retriveGreeting(name);
    }

}