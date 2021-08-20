# Core Technologies
## IoC Container
Chief among Spring's core technologies is the IoC (inversion of control) container.

"Inversion of control" (also known as "dependency injection") is the technique of feeding the dependencies of a class through its contructor instead of just putting them in as normal class properties. This allows for much more flexibility.
```java
class NoDependencyInjection
{
    public NoDependencyInjection()
    {
        m_util = new InnerUtility(someConfigParameters);
    }


    private InnerUtility m_util;
}

class DependencyInjection
{
    public DependencyInjection(InnerUtility util)
    {
        m_util = util;
    }

    private InnerUtility m_util;
}
```

Spring refers to classes written in this way as "Beans". Beans are no different from any other Java class, except that they are expected to be written a certain way.

A Bean class is any class that has...
* All private member variables that are accessible via public getters and setters.
* A public, zero-argument constructor.
* Is serializable (in Java, this means that it implements the `Serializable` interface).

It's weird that Spring refers to its IoC classes as Beans since the Spring convention is to receive dependencies via constructor arguments, while the actual definition of a Bean class is to have a zero-argument constructor. I suppose it's possible for a class to have both a dependency injection constructor and a zero-argument constructor, but the Spring documentation doesn't mention the actual conventions for Bean classes and just focuses on the the dependency injection constructor.

All necessary Beans and their dependencies are recorded in the "configuration metadata", which is then given to the IoC container so it can instantiate, configure, and assemble any Bean effectively. The IoC container knows what to instantiate/configure/assemble based off of what's in the configuration metadata.

In a nutshell, the Spring framework works by having the IoC container take in Bean classes and the configuration metadata as input, and giving a fully configured system as output.

### Interfaces
The main packages for the IoC container are `org.springframework.beans` and `org.springframework.context`.

The package `org.springframework.beans` contains the `BeanFactory` interface and the package `org.springframework.context` provides the `ApplicationContext` interface. The `BeanFactory` interface provides the configuration framework and basic functionality, while the `ApplicationContext` interface provides more application-specific functionality and represents the IoC container itself.

`ApplicationContext` is actually a sub-interface of `BeanFactory`, so it's accurate to say that the functionality of `ApplicationContext` is a superset of the functionality in `BeanFactory`. Therefore, using `ApplicationContext` is almost always preferable to using `BeanFactory`.

Several implementations of `ApplicationContext` come packaged in Spring (`ClassPathXmlApplicationContext`, `FileSystemXmlApplicationContext`, etc.).

## Configuration Metadata
The configuration metadata is traditionally written in XML, but can also be written as Java code with annotations. The configuration metadata lists the objects (beans) that make up the whole application and the dependencies between them.

A Spring configuration consists of at least one Bean definition that the IoC container must manage. In XML, this is expressed with `<bean>` elements. In annotated Java, this is expressed with `@Bean` annotations on methods within a `@Configuration` annotated class.

Here's an example of an XML Spring configuration metadata file. The `id` attribute of the beans specifies an individul bean definition. The `class` attribute is the fully qualified class type of the bean.
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="SomeIdString" class="fully.qualified.class.Type">  
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <bean id="..." class="...">
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions go here -->
</beans>
```

## Instantiating a Container

