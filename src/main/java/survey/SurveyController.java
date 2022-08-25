package survey;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @GetMapping
    public String form(Model model) {
        List<Question> questions = createQuestions();
        model.addAttribute("questions", questions);
        return "survey/surveyForm";
    }

    // ModelAndView 를 사용하면 Model을 이용해
    // 뷰에 전달할 데이터 설정 및 결과를 보여줄 뷰 이름을 리턴할 수 있다
    public ModelAndView formModelAndView() {
        List<Question> questions = createQuestions();
        ModelAndView mav = new ModelAndView();
        mav.addObject("questions", questions);
        mav.setViewName("survey/surveyForm");
        return mav;
    }

    private List<Question> createQuestions() {
        Question q1 = new Question("당신의 역할은 무엇입니까?", Arrays.asList("서버","프론트","풀스택"));
        Question q2 = new Question("많이 사용하는 개발도구는 무엇입니까?", Arrays.asList("이클립스","인텔리J","서브라임"));
        Question q3 = new Question("하고 싶은 말을 적어주세요.");
        return Arrays.asList(q1, q2, q3);
    }

    @PostMapping
    public String submit(@ModelAttribute("ansData") AnsweredData data) {
        return "survey/submitted";
    }

}
