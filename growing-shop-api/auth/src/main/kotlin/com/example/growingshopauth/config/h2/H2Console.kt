package com.example.growingshopauth.config.h2

import mu.KLogging
import org.h2.tools.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@Profile("local")
class H2Console(
    @Value("\${spring.h2.console.port}")
    val h2ConsolePort: Int
) {
    lateinit var webServer: Server

    @EventListener(ContextRefreshedEvent::class)
    fun start() {
        this.webServer = Server.createWebServer("-webPort", "$h2ConsolePort", "-tcpAllowOthers").start()
        logger.info { "h2 console run on $h2ConsolePort port" }
    }

    @EventListener(ContextClosedEvent::class)
    fun stop() {
        this.webServer.stop()
        logger.info { "h2 console close" }
    }

    companion object : KLogging()
}
