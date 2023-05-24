package com.dpm.pumping

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PumpingApplication

fun main(args: Array<String>) {
	runApplication<PumpingApplication>(*args)
}
