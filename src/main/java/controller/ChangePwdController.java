package controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.AuthInfo;
import spring.ChangePasswordService;
import spring.WrongIdPasswordException;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/edit/changePassword")
public class ChangePwdController {

    private ChangePasswordService changePasswordService;

    public void setChangePasswordService(ChangePasswordService changePasswordService) {
        this.changePasswordService = changePasswordService;
    }

    @GetMapping
    public String form(@ModelAttribute("command") ChangePasswordCommand pwdCmd) {
        return "edit/changePwdForm";
    }

    @PostMapping
    public String submit(@ModelAttribute("command") ChangePasswordCommand pwdCmd, Errors error, HttpSession session) {
        new ChangeCommandPasswordValidator().validate(pwdCmd, error);

        if (error.hasErrors()) {
            return "edit/changePwdForm";
        }

        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        try {
            changePasswordService.changePassword(authInfo.getEmail(), pwdCmd.getCurrentPassword(), pwdCmd.getNewPassword());
            return "edit/changePwd";
        } catch (WrongIdPasswordException e) {
            error.rejectValue("currentPassword", "notMatching");
            return "edit/changePwdForm";
        }
    }

}
