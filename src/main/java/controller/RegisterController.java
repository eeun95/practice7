package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import spring.DuplicateMemberException;
import spring.MemberRegisterService;
import spring.RegisterRequest;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private MemberRegisterService memberRegisterService;

    public void setMemberRegisterService(MemberRegisterService memberRegisterService) {
        this.memberRegisterService = memberRegisterService;
    }

    @RequestMapping("/register/step1")
    public String handleStep1() {
        return "register/step1";
    }

    @PostMapping("/register/step2")
    public String handleStep2(@RequestParam(value = "agree", defaultValue = "false") Boolean agree, Model model) {
        if (!agree) {
            return "register/step1";
        }
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register/step2";
    }

    @GetMapping("/register/step2")
    public String handleStep2Get() {
        return "redirect:/register/step1";
    }

    @PostMapping("/register/step3")
    public String handleStep3(@Valid RegisterRequest regReq, Errors errors) {
        // @Valid 어노테이션을 쓰려면 Errors 타입 파라미터는 필수이다
        //new RegisterRequestValidator().validate(regReq, errors);
        if (errors.hasErrors()) {
            return "register/step2";
        }
        try {
            memberRegisterService.regist(regReq);
            return "register/step3";
        } catch (DuplicateMemberException e) {
            // 특정 프로퍼티가 아닌 커맨드 객체 자체에 에러코드 추가
            errors.rejectValue("email", "duplicate");
            return "register/step2";
        }
    }

    // 컨트롤러의 요청 처리 메서드를 실행하기 전에 매번 실행됨(handle~ 메서드 실행 전에 매번 호출된다)
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // 컨트롤러 범위에 적용할 Validator 설정
        binder.setValidator(new RegisterRequestValidator());
    }

}
