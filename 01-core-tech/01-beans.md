# Beans
Beans are Java class instances (usually singletons) that are managed by containers.

A container's internal representation of beans is done with the `BeanDefinition` class. This class includes things like the bean's autowiring mode, lazy initialization mode, initialization callbacks, and destruction callbacks.

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
