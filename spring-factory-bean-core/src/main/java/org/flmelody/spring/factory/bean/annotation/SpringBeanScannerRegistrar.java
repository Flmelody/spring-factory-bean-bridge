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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @author esotericman
 */
public class SpringBeanScannerRegistrar implements ImportBeanDefinitionRegistrar {
  private static final Logger logger = LoggerFactory.getLogger(SpringBeanScannerRegistrar.class);

  /** {@inheritDoc} */
  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata importingClassMetadata, @Nonnull BeanDefinitionRegistry registry) {
    AnnotationAttributes annotationAttributes =
        AnnotationAttributes.fromMap(
            importingClassMetadata.getAnnotationAttributes(SpringBeanScan.class.getName()));
    if (annotationAttributes != null) {
      registerBeanDefinitions(
          importingClassMetadata,
          annotationAttributes,
          registry,
          importingClassMetadata.getClassName()
              + "#"
              + SpringBeanScannerRegistrar.class.getSimpleName());
    }
  }

  private void registerBeanDefinitions(
      AnnotationMetadata annoMeta,
      AnnotationAttributes annoAttrs,
      BeanDefinitionRegistry registry,
      String beanName) {
    BeanDefinitionBuilder builder =
        BeanDefinitionBuilder.genericBeanDefinition(ProxyBeanDefinitionRegistryPostProcessor.class);

    Class<? extends FactoryBean<?>> factoryBean = annoAttrs.getClass("factoryBean");
    if (FactoryBean.class.equals(factoryBean)) {
      logger.warn("No factoryBean provided, Gave up to registered beans");
      return;
    }
    builder.addPropertyValue("factoryBean", factoryBean);

    Class<? extends Annotation> beanAnnotation = annoAttrs.getClass("beanAnnotation");
    builder.addPropertyValue("beanAnnotation", beanAnnotation);

    String beanScope = annoAttrs.getString("beanScope");
    if (StringUtils.hasText(beanScope)) {
      builder.addPropertyValue("beanScope", beanScope);
    } else {
      builder.addPropertyValue("beanScope", BeanDefinition.SCOPE_SINGLETON);
    }

    List<String> basePackages =
        new ArrayList<>(
            Arrays.stream(annoAttrs.getStringArray("basePackages"))
                .filter(StringUtils::hasText)
                .toList());

    if (basePackages.isEmpty()) {
      basePackages.add(getDefaultBasePackage(annoMeta));
    }
    builder.addPropertyValue(
        "basePackages", StringUtils.collectionToCommaDelimitedString(basePackages));

    boolean lazyInitialization = annoAttrs.getBoolean("lazyInitialization");
    builder.addPropertyValue("lazyInitialization", lazyInitialization);
    registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
  }

  private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
    return ClassUtils.getPackageName(importingClassMetadata.getClassName());
  }
}
