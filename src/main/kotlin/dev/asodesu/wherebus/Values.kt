package dev.asodesu.wherebus

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

object Values {
    lateinit var baseUrl: String

    @Component
    class ValueBean {
        @Value("\${wtfimb.base-url}")
        lateinit var baseUrl: String

        @PostConstruct
        fun postConstruct() {
            Values.baseUrl = baseUrl.trimEnd('/')
        }

    }
}