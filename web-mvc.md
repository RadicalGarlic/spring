# Web MVC
Spring Web MVC is one of the many applications of the Spring framework. This application in particular is specifically for running web applications.

The full name is "Spring Web MVC" (Model-View-Controller), but many times people just say "Spring MVC".

The framework is used to run one or more "servlets" in a "servlet container", which then functions as a full web application server.

The servlet container is typically one of the many off-the-shelf options (Apache Tomcat, Jetty, Glassfish, JBoss, etc.).

## Servlets
In Java, a servlet is basically any class that implements the `javax.servlet.Servlet` interface.

The idea is that each servlet is capable of handling some of the request types that would be made to the web app. All of these servlets are then combined in the servlet container, and once the servlet container is running with all the servlets, it's capable of handling all the requests that could be made to the web app.

## Architecture
Spring MVC uses the "front controller" pattern for its servlets, meaning there's one central servlet that receives every request which then delegates work to some other servlet based on the properties of the request. This front controller is called the `DispatcherServlet`.

Without Spring MVC, a servlet container would be configured with many individual servlets with each implementing a small part of the server's functionality. With the `DispatcherServlet`, the servlet container configuration need only reference the `DispatcherServlet`. The `DispatcherServlet` does all the work of finding the appropriate sub-servlet to delegate the request to.

## DispatcherServlet
Like all servlets, the `DispatcherServlet` needs to be configured with either a `web.xml` or in Java code.

Below is an example using Java code.
```java
public class MyWebApplicationInitializer implements WebApplicationInitializer
{
    @Override
    public void onStartup(ServletContext servletContext)
    {
        // Load Spring web application configuration
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);

        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic registration = servletContext.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/app/*");
    }
}
```

Below is an example using the `web.xml` file.
```xml
<web-app>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/app-context.xml</param-value>
    </context-param>

    <servlet>
        <servlet-name>app</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value></param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>app</servlet-name>
        <url-pattern>/app/*</url-pattern>
    </servlet-mapping>
</web-app>
```
