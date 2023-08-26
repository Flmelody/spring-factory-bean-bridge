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

package org.flmelody.spring.factory.bean.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

/**
 * @author esotericman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(SpringBeanScannerRegistrar.class)
public @interface SpringBeanScan {

  /**
   * Alias for basePackages. Intended to be used instead of basePackages when annotation is not
   * declared. for example: @AliasFor("value") instead of @AliasFor(basePackages = "value").
   *
   * @return basePackages
   */
  @AliasFor("basePackages")
  String[] value() default {};
  /**
   * Base packages for scanning the bean candidate. If empty, scanner will scan through annotated
   * class package.
   *
   * @return basePackages
   */
  String[] basePackages() default {};

  /**
   * Factory for creating bean.
   *
   * @return factoryBean
   */
  Class<? extends FactoryBean> factoryBean() default FactoryBean.class;

  /**
   * Annotation for filtering the bean candidate.
   *
   * @return annotation
   */
  Class<? extends Annotation> beanAnnotation() default SpringBean.class;

  /**
   * Scope of bean, default is singleton.
   *
   * @return beans scope
   */
  String beanScope() default BeanDefinition.SCOPE_SINGLETON;

  /**
   * Whether if bean is lazy.
   *
   * @return lazy bean or not
   */
  boolean lazyInitialization() default false;
}
