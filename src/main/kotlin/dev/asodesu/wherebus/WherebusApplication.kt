package dev.asodesu.wherebus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WherebusApplication

fun main(args: Array<String>) {
    runApplication<WherebusApplication>(*args)
}
