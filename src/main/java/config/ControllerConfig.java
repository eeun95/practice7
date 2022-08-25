package config;

import controller.RegisterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.MemberRegisterService;
import survey.SurveyController;

@Configuration
public class ControllerConfig {

    @Autowired
    private MemberRegisterService memberRegisterService;

    @Bean
    public RegisterController registerController() {
        RegisterController regController = new RegisterController();
        regController.setMemberRegisterService(memberRegisterService);
        return regController;
    }

    @Bean
    public SurveyController surveyController() {
        return new SurveyController();
    }
}
