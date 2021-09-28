# Beans
Beans are Java class instances (usually singletons) that are managed by containers.

A container's internal representation of beans is defined in the `BeanDefinition` class. This class includes things like the bean's autowiring mode, lazy initialization mode, initialization callbacks, and destruction callbacks.

To add external objects as beans, modify the bean factory in `ApplicationContext`.
```java
ApplicationContext context = new ClassPathXmlApplicationContext("configMetadata.xml");
DefaultListableBeanFactory beanFactory =  context.getBeanFactory();
beanFactory.registerSingleton(something);
beanFactory.registerBeanDefinition(somethingElse);
```

## Naming Beans
A bean must have a name/ID that is unique across the container's beans. If a bean name is not provided, a unique one is generated for it.

A bean can have more than one name. The other names aside from the first are called "aliases".

The convention for bean names is lower camel-case.

In XML, beans are named with the `id` attribute.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="someBean" class="package.SomeClass"/>

    <!-- Both commas and semicolons can be used to specify aliases -->
    <bean id="otherBean,otherBeanAlias" class="package.SomeClass"/>
    <bean id="moreBean;moreBeanAlias" class="package.SomeClass"/>
</beans>
```

### Aliasing a Bean outside the Bean Definition
In large systems, sometimes it may be useful for subsystems to give their own aliases for other beans.

This can be done with the `<alias>` element.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="someBean" class="package.SomeClass"/>

    <alias name="originalBeanName" alias="newBeanNameAlias" />
</beans>
```

## Instantiating Beans
When the context needs to create a bean, either the bean class's constructor or a static factory method is used.

If the `class` attribute of the `bean` element is a nested class, either a `$` or `.` can be used as a separator for the enclosing class.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--
        public class SomeClass
        {
            public static SomeNestedClass
            {}
        }
    -->
    <bean id="someBean" class="package.SomeClass.SomeNestedClass"/>
    <bean id="someOtherBean" class="package.SomeClass$SomeNestedClass"/>
</beans>
```

Beans instantiated with just the class type and no other parameters will use the class's no-argument constructor to instantiate the bean. If the class has no no-argument constructor, then Spring probably crashes with some error.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="myBean" class="package.SomeClassNoArgCtorUsed"/>
</beans>
```

Beans instantiated with parameters use dependency injection. TODO: finish this once you get to the relevant documentation.

### Instantiation with a Static Factory
To use a static factory method to instantiate a bean rather than one of its constructors.

TODO: Describe how to provide parameters.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="myBean" class="package.StaticFactoryClass" factory-method="someFactoryMethod"/>
</beans>
```

### Instantiation with an Bean Instance Factory
Beans can also be instantiated using a method of some existing bean's method.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="myBean" factory-bean="someFactoryBean" factory-method="someFactoryBeanMethod"/>
</beans>
```

## Determining a Bean's Runtime Type
A bean's runtime type is non-trivial to determine, so use the `org.springframework.beans.factory.BeanFactory.getType` method to do so.

Remember that `ApplicationContext`, which represents the IoC container, is a sub-interface of `BeanFactory`. Therefore, you should be using the `getType` call on the `ApplicationContext` object.
