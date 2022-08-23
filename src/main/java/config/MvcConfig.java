package config;

// 스프링 부트 사용하긴 하지만 스프링 MVC 구조를 자세히 보기 위해 전부 만들어봄

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// 스프링 MVC 설정을 활성화한다. 스프링 MVC를 사용하는데 필요한 다양한 설정을 생성한다.
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    // DispatcherServlet의 매핑 경로를 '/'로 주었을 때, JSP/HTML/CSS 등을 올바르게 처리하기 위한 설정을 추가한다.
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // JSP를 이용해서 컨트롤러의 실행 결과를 보여주기 위한 설정을 추가한다.
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/view/", ".jsp");
    }
}
