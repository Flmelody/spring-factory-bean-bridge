# Spring-factory-bean-bridge

![GitHub](https://img.shields.io/github/license/Flmelody/spring-factory-bean-bridge)
![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Forg%2Fflmelody%2Fspring-factory-bean-core%2Fmaven-metadata.xml&query=%2F%2Fmetadata%2Fversioning%2Flatest&logo=apachemaven&logoColor=%23a34b08&label=spring-factory-bean-core&labelColor=%2308a31a)
![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Forg%2Fflmelody%2Fspring-factory-bean-jdbi%2Fmaven-metadata.xml&query=%2F%2Fmetadata%2Fversioning%2Flatest&logo=apachemaven&logoColor=%23a34b08&label=spring-factory-bean-jdbi&labelColor=%2308a31a)

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
    <version>1.0.0-RELEASE</version>
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
    <version>1.0.0-RELEASE</version>
</dependency>

```

or Gradle dependency

```groovy
implementation 'org.flmelody:spring-factory-bean-jdbi:1.0.0-RELEASE'
```

- Configuration

ADD `@JdbiRepositoryScan(basePackages = "packages")` in your configuration class and `@SpringBean` in your proxy class
