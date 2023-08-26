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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * @author esotericman
 */
@Documented
@Inherited
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SpringBean {

  /**
   * Alias for beanScope. Intended to be used instead of beanScope when annotation is not declared.
   * for example: @AliasFor("value") instead of @AliasFor(beanScope = "value").
   *
   * @return beanScope
   */
  @AliasFor("beanScope")
  String value() default "";

  /**
   * Scope of bean,higher priority than global scan {@link SpringBeanScan#beanScope()}
   *
   * @return beans scope
   */
  String beanScope() default "";
}
