package helloworld;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloWorldController
{
    @GetMapping
    public String printHello(ModelMap model)
    {
        //model.addAttribute("message", "Hello Spring MVC Framework!");
        return "hello";
    }
}
