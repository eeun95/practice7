package config;

// 스프링 부트 사용하긴 하지만 스프링 MVC 구조를 자세히 보기 위해 전부 만들어봄

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import controller.MemberListController;
import controller.RegisterRequestValidator;
import interceptor.AuthCheckInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
// 스프링 MVC 설정을 활성화한다. 스프링 MVC를 사용하는데 필요한 다양한 설정을 생성한다.
@EnableWebMvc   // OptionalValidatorFactoryBean 을 글로벌 범위 Validator로 등록
public class MvcConfig implements WebMvcConfigurer {

    /*
    @Override
    public Validator getValidator() {
        return new RegisterRequestValidator();
    }

     */

    // DispatcherServlet의 매핑 경로를 '/'로 주었을 때, JSP/HTML/CSS 등을 올바르게 처리하기 위한 설정을 추가한다.
    // SpringBoot에서는 사실상 DispatcherServlet만 사용하기 떄문에 기본적으로 등록되던 DefaultServlet이 더 이상 등록되지 않는다
    // 그래서 필요시 프로퍼티 파일에 server.servlet.register-default-servlet=true 추가해줘야함
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // JSP를 이용해서 컨트롤러의 실행 결과를 보여주기 위한 설정을 추가한다.
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/view/", ".jsp");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/main").setViewName("main");
    }

    // 빈의 아이디를 messageSource로 지정하지 않으면 정상적으로 동작하지 않음
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasenames("message.label");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

    // 인터셉터를 설정하는 메서드
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authCheckInterceptor()).addPathPatterns("/edit/**");
    }

    @Bean
    public AuthCheckInterceptor authCheckInterceptor() {
        return new AuthCheckInterceptor();
    }

    // 모든 날짜 타입을 ISO-8601 형식으로 변환하기 위한 설정
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
