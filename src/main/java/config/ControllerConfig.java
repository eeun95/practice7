package config;

import controller.RegisterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.MemberRegisterService;

@Configuration
public class ControllerConfig {

    @Autowired
    private MemberRegisterService memberRegisterSvc;

    @Bean
    public RegisterController registerController() {
        RegisterController registerController = new RegisterController();
        registerController.setMemberRegisterService(memberRegisterSvc);
        return registerController;
    }
}
