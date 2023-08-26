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
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

/**
 * @author esotericman
 */
public class ProxyClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

  private static final Logger logger =
      LoggerFactory.getLogger(ProxyClassPathBeanDefinitionScanner.class);
  private Class<? extends Annotation> beanAnnotation;
  private Class<? extends FactoryBean<?>> factoryBean;
  private String beanScope;
  private boolean lazyInitialization;

  public ProxyClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
    super(registry, false);
  }

  public void setBeanAnnotation(Class<? extends Annotation> beanAnnotation) {
    this.beanAnnotation = beanAnnotation;
  }

  public void setLazyInitialization(boolean lazyInitialization) {
    this.lazyInitialization = lazyInitialization;
  }

  public void setFactoryBean(Class<? extends FactoryBean<?>> factoryBean) {
    this.factoryBean = factoryBean;
  }

  public void setBeanScope(String beanScope) {
    this.beanScope = beanScope;
  }

  public void registerFilters() {
    boolean acceptAllInterfaces = true;

    if (this.beanAnnotation != null) {
      addIncludeFilter(new AnnotationTypeFilter(this.beanAnnotation));
      acceptAllInterfaces = false;
    }

    if (acceptAllInterfaces) {
      addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
    }

    addExcludeFilter(
        (metadataReader, metadataReaderFactory) -> {
          String className = metadataReader.getClassMetadata().getClassName();
          return className.endsWith("package-info");
        });
  }

  /** {@inheritDoc} */
  @Override
  @Nonnull
  public Set<BeanDefinitionHolder> doScan(@Nonnull String... basePackages) {
    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
    if (!beanDefinitions.isEmpty()) {
      processBeanDefinitions(beanDefinitions);
    }
    return beanDefinitions;
  }

  private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
    AbstractBeanDefinition definition;
    BeanDefinitionRegistry registry = getRegistry();
    if (registry == null) {
      logger.warn("The bean registry is null, no beans would be registered");
      return;
    }
    for (BeanDefinitionHolder holder : beanDefinitions) {
      definition = (AbstractBeanDefinition) holder.getBeanDefinition();
      boolean scopedProxy = false;
      if (ScopedProxyFactoryBean.class.getName().equals(definition.getBeanClassName())) {
        definition =
            (AbstractBeanDefinition)
                Optional.ofNullable(((RootBeanDefinition) definition).getDecoratedDefinition())
                    .map(BeanDefinitionHolder::getBeanDefinition)
                    .orElseThrow(
                        () ->
                            new IllegalStateException(
                                "The target bean definition of scoped proxy bean "
                                    + holder
                                    + " not found."));
        scopedProxy = true;
      }
      String beanClassName = definition.getBeanClassName();
      definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
      definition.setBeanClass(this.factoryBean);
      definition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, beanClassName);
      definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
      definition.setLazyInit(lazyInitialization);

      if (scopedProxy) {
        continue;
      }

      if (definition instanceof AnnotatedBeanDefinition annotatedBeanDefinition) {
        AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
        MergedAnnotations annotations = metadata.getAnnotations();
        MergedAnnotation<Annotation> annotationMergedAnnotation =
            annotations.get(beanAnnotation.getName());
        String scope = annotationMergedAnnotation.getString("beanScope");
        if (StringUtils.hasText(scope)) {
          definition.setScope(scope);
        } else {
          definition.setScope(beanScope);
        }
      } else {
        definition.setScope(beanScope);
      }

      if (!definition.isSingleton()) {
        BeanDefinitionHolder proxyHolder =
            ScopedProxyUtils.createScopedProxy(holder, registry, true);
        if (registry.containsBeanDefinition(proxyHolder.getBeanName())) {
          registry.removeBeanDefinition(proxyHolder.getBeanName());
        }
        registry.registerBeanDefinition(proxyHolder.getBeanName(), proxyHolder.getBeanDefinition());
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
    return beanDefinition.getMetadata().isInterface()
        && beanDefinition.getMetadata().isIndependent();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean checkCandidate(
      @Nonnull String beanName, @Nonnull BeanDefinition beanDefinition) {
    if (super.checkCandidate(beanName, beanDefinition)) {
      return true;
    } else {
      logger.warn(
          "Skipping FactoryBean with name '"
              + beanName
              + " Bean already defined with the same name!");
      return false;
    }
  }
}
