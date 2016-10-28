package dnn.web1.custom.proced;

import dnn.service.feedbackInfo.ISerFeedbackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("custom/proced")
public class ProcedAct1 {

    @Autowired
    private ISerFeedbackInfo iSerFeedbackInfo;

    /**
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public ModelAndView home(HttpServletRequest request) {
        return new ModelAndView("redirect:/custom/proced/index");
    }

    @GetMapping("index")
    public ModelAndView index(HttpServletRequest request) {
        return new ModelAndView("custom/proced/index");
    }
}
