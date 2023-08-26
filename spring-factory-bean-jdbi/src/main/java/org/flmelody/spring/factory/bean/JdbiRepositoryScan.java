/*
 * Copyright (C) 2023 Flmelody.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flmelody.spring.factory.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.flmelody.spring.factory.bean.annotation.SpringBeanScan;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.AliasFor;

/**
 * @author esotericman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@SpringBeanScan(factoryBean = JdbiRepositoryFactoryBean.class)
public @interface JdbiRepositoryScan {

  /**
   * Alias for {@link SpringBeanScan#basePackages()}
   *
   * @return basePackages
   */
  @AliasFor(annotation = SpringBeanScan.class, attribute = "basePackages")
  String[] basePackages() default {};

  /**
   * Alias for {@link SpringBeanScan#beanScope()}
   *
   * @return basePackages
   */
  @AliasFor(annotation = SpringBeanScan.class, attribute = "beanScope")
  String beanScope() default BeanDefinition.SCOPE_SINGLETON;

  /**
   * Alias for {@link SpringBeanScan#lazyInitialization()}
   *
   * @return basePackages
   */
  @AliasFor(annotation = SpringBeanScan.class, attribute = "lazyInitialization")
  boolean lazyInitialization() default false;
}
