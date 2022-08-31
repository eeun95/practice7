package controller;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import spring.RegisterRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 커맨드 객체의 값 검증을 위한 구현체
public class RegisterRequestValidator implements Validator {

    private static final String emailRegExp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;

    public RegisterRequestValidator() {
        pattern = Pattern.compile(emailRegExp);
    }

    // 파라미터로 전달받은 clazz 객체가 RegisterRequest 클래스로 타입 변환이 가능한지 확인
    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterRequest.class.isAssignableFrom(clazz);
    }

    // target 파라미터는 검사 대상 객체, errors 파라미터는 검사 결과 에러 코드를 설정하기 위한 객체
    @Override
    public void validate(Object target, Errors errors) {
        // 전달받은 target을 실제 타입으로 변환한다
        RegisterRequest regReq = (RegisterRequest) target;

        // email 프로퍼티의 값이 유효한지 검사한다
        if (regReq.getEmail() == null || regReq.getEmail().trim().isEmpty()) {
            // 첫번째는 프로퍼티의 이름을 전달받고, 두번째는 에러코드를 전달받는다
            errors.rejectValue("email", "required");
        } else {
            Matcher matcher = pattern.matcher(regReq.getEmail());
            if (!matcher.matches()) {
                errors.rejectValue("email", "bad");
            }
        }

        // ValidationUtils 클래스는 객체의 값 검증 코드를 간결하게 작성할 수 있도록 도와준다
        // errors 객체의 getFieldValue("name") 메서드를 실행해서 커맨드 객체의 name 프로퍼티 값을 구함
        // 따라서 커맨드 객체를 직접 전달하지 않아도 값 검증을 할 수 있음
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
        ValidationUtils.rejectIfEmpty(errors, "password", "required");
        ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "required");
        if (!regReq.getPassword().isEmpty()) {
            if (!regReq.isPasswordEqualToConfirmPassword()) {
                errors.rejectValue("confirmPassword", "nomatch");
            }
        }
    }
}
