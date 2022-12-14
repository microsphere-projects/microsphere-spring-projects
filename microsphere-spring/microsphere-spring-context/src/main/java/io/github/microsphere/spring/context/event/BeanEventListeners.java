/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.microsphere.spring.context.event;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.function.Consumer;

/**
 * The composite {@link BeanEventListener}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
class BeanEventListeners {

    private final ObjectProvider<BeanEventListener> listeners;

    public BeanEventListeners(ConfigurableApplicationContext context) {
        this.listeners = context.getBeanProvider(BeanEventListener.class);
    }

    public void onBeanDefinitionReady(BeanDefinition beanDefinition, String beanName) {
        iterate(listener -> listener.onBeanDefinitionReady(beanDefinition, beanName));
    }

    public void onBeforeInstantiation(Class<?> beanClass, String beanName) {
        iterate(listener -> listener.onBeforeInstantiation(beanClass, beanName));
    }

    public void onInstantiating(Object bean, String beanName) {
        iterate(listener -> listener.onInstantiating(bean, beanName));
    }

    public void onBeanInstantiated(Object bean, String beanName) {
        iterate(listener -> listener.onInstantiated(bean, beanName));
    }

    public void onPropertyValuesReady(PropertyValues pvs, Object bean, String beanName) {
        iterate(listener -> listener.onPropertyValuesReady(pvs, bean, beanName));
    }

    public void onBeforeInitialization(Object bean, String beanName) {
        iterate(listener -> listener.onBeforeInitialization(bean, beanName));
    }

    public void onInitialized(Object bean, String beanName) {
        iterate(listener -> listener.onInitialized(bean, beanName));
    }

    private void iterate(Consumer<BeanEventListener> listenerConsumer) {
        try {
            listeners.orderedStream().forEach(listenerConsumer);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
