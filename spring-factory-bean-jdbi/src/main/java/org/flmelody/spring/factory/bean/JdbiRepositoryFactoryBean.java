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

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author esotericman
 */
public class JdbiRepositoryFactoryBean<T> implements FactoryBean<T> {
  private final Class<T> beanInterface;
  private Jdbi jdbi;

  public JdbiRepositoryFactoryBean(Class<T> beanInterface) {
    this.beanInterface = beanInterface;
  }

  /** {@inheritDoc} */
  @Override
  public T getObject() throws Exception {
    return getJdbi().onDemand(beanInterface);
  }

  /** {@inheritDoc} */
  @Override
  public Class<T> getObjectType() {
    return this.beanInterface;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSingleton() {
    return true;
  }

  // ------------- mutators --------------
  public Jdbi getJdbi() {
    return jdbi;
  }

  public void setJdbi(Jdbi jdbi) {
    this.jdbi = jdbi;
  }
}
