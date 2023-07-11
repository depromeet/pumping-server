package com.dpm.pumping.common

import com.dpm.pumping.common.exception.CustomAsyncExceptionHandler
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor


@Configuration
@EnableAsync
class AsyncConfig : AsyncConfigurer {

    @Bean(name = ["deleteUserThreadPoolTaskExecutor"])
    fun threadPoolTaskExecutor(): Executor? {
        return ThreadPoolTaskExecutor().apply {
            this.corePoolSize = 5
            this.maxPoolSize = 20
            initialize()
        }
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return CustomAsyncExceptionHandler()
    }
}
