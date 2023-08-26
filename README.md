# Spring-factory-bean-bridge

This project is aim at registering own proxy objects that with complex step within initialization in spring boot
container.
Using `FactoryBean` implementation, We can register them automatically.

## Preface

**minimal Java version 17**

## Supported default

- JDBI repository (use `@JdbiRepositoryScan` and `@SpringBean` annotations)

## Custom yourself

first of all, you must define your `FactoryBean`, then use `@SpringBeanScan` with `factoryBean()`, and `@SpringBean` for
you interface.
Base dependency

```xml

<dependency>
    <groupId>org.flmelody</groupId>
    <artifactId>spring-factory-bean-core</artifactId>
    <version>1.0.0</version>
</dependency>

```

```java
public class YourFactoryBean<T> implements FactoryBean<T> {
    // ... some codes
}

```

ADD `@SpringBeanScan(factoryBean = YourFactoryBean.class,basePackages = "packages")` in your configuration class
and `@SpringBean` in your proxy class

## JDBI quickstart

- Dependency

Maven dependency

```xml

<dependency>
    <groupId>org.flmelody</groupId>
    <artifactId>spring-factory-bean-jdbi</artifactId>
    <version>1.0.0</version>
</dependency>

```

or Gradle dependency

```groovy
implementation 'org.flmelody:spring-factory-bean-jdbi:1.0.0'
```

- Configuration

ADD `@JdbiRepositoryScan(basePackages = "packages")` in your configuration class and `@SpringBean` in your proxy class