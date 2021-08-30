package hello.world;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "hello.world")
public class Application
{
    public static void main(String[] args)
    {
        ApplicationContext context
            = new AnnotationConfigApplicationContext(Application.class);

        Application p = context.getBean(Application.class);
        p.start();
    }

    private void start()
    {
        System.out.println("start");
    }
}
