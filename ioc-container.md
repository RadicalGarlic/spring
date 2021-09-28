# IoC Container
The central entity responsible for configuring/instantiating/managing beans is IoC (inversion of control) container.

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

With this, Spring can easily instantiate its beans with a high degree of configurability.

All necessary beans and their dependencies are recorded in the "configuration metadata", which is then given to the IoC container so it can instantiate, configure, and assemble any bean effectively. The IoC container knows what to instantiate/configure/assemble based off of what's in the configuration metadata.

A bean's dependencies are also sometimes called "collaborators".

In a nutshell, the Spring framework works by having the IoC container take in bean classes and the configuration metadata as input, and then giving a fully configured system as output.

## Java Interfaces
The main packages for the IoC container are `org.springframework.beans` and `org.springframework.context`.

The package `org.springframework.beans` contains the `BeanFactory` interface and the package `org.springframework.context` provides the `ApplicationContext` interface. The `BeanFactory` interface provides the configuration framework and basic functionality, while the `ApplicationContext` interface provides more application-specific functionality and represents the IoC container itself.

`ApplicationContext` is actually a sub-interface of `BeanFactory`, so it's accurate to say that the functionality of `ApplicationContext` is a superset of the functionality in `BeanFactory`. Therefore, using `ApplicationContext` is almost always preferable to using `BeanFactory`.

Several implementations of `ApplicationContext` come packaged in Spring (`ClassPathXmlApplicationContext`, `FileSystemXmlApplicationContext`, etc.).

## Configuration Metadata
The configuration metadata is traditionally written in XML, but can also be written as Java code with annotations. The configuration metadata lists the objects (beans) that make up the whole application and the dependencies between them.

A Spring configuration consists of at least one Bean definition that the IoC container must manage. In XML, this is expressed with `<bean>` elements. In annotated Java, this is expressed with `@Bean` annotations on methods within a `@Configuration` annotated class.

Here's an example of an XML Spring configuration metadata file. The `id` attribute of the beans specifies an individul bean definition. The `class` attribute is the fully qualified class type of the bean.
```xml
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

    <!-- more bean definitions can go here -->
</beans>
```

## Instantiating a Container
To instantiate a container, create an instance of `ApplicationContext` and provide it a `Resource` containing configuration metadata. The configuration metadata can be provided in files, InputStreams, the Java CLASSPATH, etc.
```java
// Configuration metadata provided in XML files for this example.
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
```

Here are the example contents of `services.xml`.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="petStore" class="org.springframework.samples.jpetstore.services.PetStoreServiceImpl">
        <property name="accountDao" ref="accountDao"/>
        <property name="itemDao" ref="itemDao"/>
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>
</beans>
```

Here are the example contents of `daos.xml`.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="accountDao" class="org.springframework.samples.jpetstore.dao.jpa.JpaAccountDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <bean id="itemDao" class="org.springframework.samples.jpetstore.dao.jpa.JpaItemDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>
</beans>
```

Looking at these two files, we can see that the Bean `petStore` has two properties named `accountDao` and `itemDao` that make references (using `ref`) to the Beans `accountDao` and `itemDao` respectively. This is basically equivalent to assigning the Beans `accountDao` and `itemDao` to the properties of `petStore`. This expresses a dependency between `petStore` and `accountDao`/`itemDao`. Beans are typically singleton instances of the class definition, so the assigned property becomes a reference to the singleton.

Configuration metadata files written in XML can also import other configuration metadata XML files. This way, fewer files need to be given to `ClassPathXmlApplicationContext`.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">

    <import resource="file.xml"/>
    <import resource="path/to/file.xml"/>

    <!-- Leading slashes are ignored -->
    <import resource="/relative/path/to/file.xml"/>

    <!-- Legal, but avoid if possible -->
    <import resource="../outside/path/to/file.xml"/>

    <!-- Beans -->
    <bean id="someBean" class="package.SomeClass"/>
</beans>
```

Configuration metadata files can also be written in Groovy.

## Using the Container
Once the container has been initialized and configured, it can be used.
```java
// create and configure beans
ApplicationContext context = new ClassPathXmlApplicationContext("configMetadata.xml");

// retrieve configured instance
// Ideally though, getBean() should never be used.
MyBeanType myBean = context.getBean("myBeanId", MyBeanType.class);

// use configured instance
myBean.do();
```

The most flexible variant of `ApplicationContext` is `GenericApplicationContext`. Using this allows for multiple formats of configuration metadata to be used at once.
```java
GenericApplicationContext context = new GenericApplicationContext();
new XmlBeanDefinitionReader(context).loadBeanDefinitions("configMetadata.xml");
new GroovyBeanDefinitionReader(context).loadBeanDefinitions("otherConfigMetadata.groovy");
context.refresh();
```
