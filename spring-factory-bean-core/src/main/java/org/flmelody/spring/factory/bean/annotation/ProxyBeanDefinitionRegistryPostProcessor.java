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

import jakarta.annotation.Nonnull;
import java.lang.annotation.Annotation;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * @author esotericman
 */
public class ProxyBeanDefinitionRegistryPostProcessor
    implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
  private Class<? extends FactoryBean<?>> factoryBean;
  private String basePackages;
  private Class<? extends Annotation> beanAnnotation;
  private String beanScope;
  private boolean lazyInitialization;
  private ApplicationContext applicationContext;

  /**
   * set factoryBean.
   *
   * @param factoryBean factoryBean
   */
  public void setFactoryBean(Class<? extends FactoryBean<?>> factoryBean) {
    this.factoryBean = factoryBean;
  }

  /**
   * set factoryBean.
   *
   * @param basePackages basePackages
   */
  public void setBasePackages(String basePackages) {
    this.basePackages = basePackages;
  }

  /**
   * set beanAnnotation.
   *
   * @param beanAnnotation beanAnnotation
   */
  public void setBeanAnnotation(Class<? extends Annotation> beanAnnotation) {
    this.beanAnnotation = beanAnnotation;
  }

  /**
   * set beanScope.
   *
   * @param beanScope beanScope
   */
  public void setBeanScope(String beanScope) {
    this.beanScope = beanScope;
  }

  /**
   * set lazyInitialization .
   *
   * @param lazyInitialization lazyInitialization
   */
  public void setLazyInitialization(boolean lazyInitialization) {
    this.lazyInitialization = lazyInitialization;
  }

  /** {@inheritDoc} */
  @Override
  public void setApplicationContext(@Nonnull ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /** {@inheritDoc} */
  @Override
  public void postProcessBeanFactory(@Nonnull ConfigurableListableBeanFactory beanFactory) {}

  /** {@inheritDoc} */
  @Override
  public void postProcessBeanDefinitionRegistry(@Nonnull BeanDefinitionRegistry registry) {
    ProxyClassPathBeanDefinitionScanner scanner = new ProxyClassPathBeanDefinitionScanner(registry);
    scanner.setBeanAnnotation(this.beanAnnotation);
    scanner.setResourceLoader(this.applicationContext);
    scanner.setFactoryBean(this.factoryBean);
    scanner.setLazyInitialization(this.lazyInitialization);
    scanner.setBeanScope(this.beanScope);
    scanner.registerFilters();
    scanner.scan(
        StringUtils.tokenizeToStringArray(
            this.basePackages, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
  }
}
