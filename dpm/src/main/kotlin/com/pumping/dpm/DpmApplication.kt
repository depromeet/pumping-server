package com.pumping.dpm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DpmApplication

fun main(args: Array<String>) {
	runApplication<DpmApplication>(*args)
}
