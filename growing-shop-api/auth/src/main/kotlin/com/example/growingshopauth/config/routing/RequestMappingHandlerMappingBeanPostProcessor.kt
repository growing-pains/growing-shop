package com.example.growingshopauth.config.routing

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping

@Component
class RequestMappingHandlerMappingBeanPostProcessor: BeanPostProcessor {
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        if (bean is RequestMappingHandlerMapping) {
            bean.order = 2
        }

        return bean
    }
}
