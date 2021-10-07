# Dependencies
TODO: Find out if the `xmlns` for the beans XML is necessary.

Spring primarily handles dependencies between beans with dependency injection (explained in the IoC container notes).

The exact way a bean's dependencies get injected is either through one of its constructors or through it's setter methods.

## Constructor-based Dependency Injection
A bean that supports constructor-based dependency injection would look similar to the following.
```java
package my.package;

public class MyBean
{
    public MyBean(MyDependency inDep)
    {
        this.dep = inDep;
    }

    private MyDependency dep;
}
```
When the bean is created via its constructor, it can set its member variable `dep` to the constructor argument.

### Configuration Metadata (XML)
If a constructor's arguments all have different types that arent' related by inheritance, then they can be specified in XML with just `constructor-arg`.
```java
package some.package;

public class MyBean
{
    public MyBean(SomeDep inSomeDep, SomeOtherDep inSomeOtherDep)
    {
        this.someDep = inSomeDep;
        this.someOtherDep = inSomeOtherDep;
    }

    private SomeDep someDep;
    private SomeOtherDep inSomeOtherDep;
}
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="myBean" class="some.package.MyBean"/>
        <constructor-arg ref="someDepBean"/>
        <constructor-arg ref="someOtherDepBean"/>
    </bean>

    <bean id="someDepBean" class="some.package.SomeDep"/>
    <bean id="someOtherDepBean" class="some.package.SomeOtherDep"/>
</beans>
```

If the constructor argument types are primitives/strings, the type will need to be provided to `constructor-arg`.
```java
package some.package;

public class MyBean
{
    public MyBean(int inDepInt, String inDepString)
    {
        this.depInt = inDepInt;
        this.depString = inDepString;
    }

    private int depInt;
    private String depString;
}
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="myBean" class="some.package.MyBean"/>
        <constructor-arg type="int" value="20"/>
        <constructor-arg type="java.lang.String" value="asdf"/>
    </bean>
</beans>
```

Constructor arguments can also be specified by a numerical index.
```java
package some.package;

public class MyBean
{
    public MyBean(int inDepInt, String inDepString)
    {
        this.depInt = inDepInt;
        this.depString = inDepString;
    }

    private int depInt;
    private String depString;
}
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="myBean" class="some.package.MyBean"/>
        <constructor-arg index="0" value="20"/>
        <constructor-arg index="1" value="asdf"/>
    </bean>
</beans>
```

Constructor arguments can also be specified by the argument variable name, but only if debug symbols are compiled or if the `@ConstructorProperties` JDK annotation is used (https://docs.oracle.com/javase/8/docs/api/java/beans/ConstructorProperties.html).
```java
package some.package;

public class MyBean
{
    @ConstructorProperties({"inSomeDep", "inSomeOtherDep"})
    public MyBean(SomeDep inSomeDep, SomeOtherDep inSomeOtherDep)
    {
        this.someDep = inSomeDep;
        this.someOtherDep = inSomeOtherDep;
    }

    private SomeDep someDep;
    private SomeOtherDep inSomeOtherDep;
}
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="myBean" class="some.package.MyBean"/>
        <constructor-arg name="inSomeDep" ref="someDepBean"/>
        <constructor-arg name="inSomeOtherDep" ref="someOtherDepBean"/>
    </bean>

    <bean id="someDepBean" class="some.package.SomeDep"/>
    <bean id="someOtherDepBean" class="some.package.SomeOtherDep"/>
</beans>
```

### Setter-based Dependency Injection
Setter-based dependency injection is accomplished by using the bean's setter methods rather than its constructor arguments. This means the bean must have a zero-argument constructor or zero-argument factory method.
```java
package some.package;

public class MyBean
{
    public MyBean()
    {}

    public void setSomeDep(SomeDep in) { this.someDep = in; }
    public void setSomeOtherDep(SomeOtherDep in) { this.someOtherDep = in; }

    private SomeDep someDep;
    private SomeOtherDep someOtherDep;
}
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="myBean" class="some.package.MyBean"/>
        <!-- Using the ref tag syntax -->
        <property name="someDep">
            <ref bean="someDepBean"/>
        </property>

        <!-- Using the (much cleaner) ref attribute syntax -->
        <property name="someOtherDep" ref="someOtherDepBean"/>
    </bean>

    <bean id="someDepBean" class="some.package.SomeDep"/>
    <bean id="someOtherDepBean" class="some.package.SomeOtherDep"/>
</beans>
```

Constructor-based dependency injection is usually preferred over setter-based dependency injection, but sometimes setter-based is the only choice. Setter-based dependency injection is the better option when a bean property's configuration is optional and has a sensible default.

Setters can be annotated with `@Required` to make their explicit configuration mandatory.
