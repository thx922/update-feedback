package dnn.web2.custom.procing;

import dnn.common.MethodVersionAnnotation;
import dnn.common.validation.Add;
import dnn.entity.user.User;
import dnn.service.feedbackInfo.ISerFeedbackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * #classDes 这个是producing类,用来处理表单状态为处理中的哦
 */
@RestController
@RequestMapping("custom/procing")
public class ProcingAct2 {

    @Autowired
    private ISerFeedbackInfo iSerFeedbackInfo;

    /**
     * 这是home方法,我们用来重定向到index页面
     * #nameDes 担任过home方法
     * #param request
     * #exclude  i
     * #include
     * #version 1.2
     * #return 放回String
     */
    @MethodVersionAnnotation(version = "1.2")
    @GetMapping("")
    public ModelAndView home(HttpServletRequest request) {
        return new ModelAndView("redirect:/custom/procing/index");
    }
    /**
     * 哈哈哈,这里不做任何处理,制作转发,版本为最大的方法
     * #nameDes 方法大姑
     * #exclude
     * #include
     * #version 1.4
     * #return
     */
    @GetMapping("index")
    @MethodVersionAnnotation(version = "1.4")
    public ModelAndView index(String ss) {
        return new ModelAndView("custom/procing/index");
    }





    /**
     * 猜猜我是做什么的,我是index版本为1.3哦
     * #exclude
     * #include
     * #version 1.3
     * #return
     */
    @MethodVersionAnnotation(version = "1.3")
    public ModelAndView index(int a,int b) {
        return new ModelAndView("custom/procing/index");
    }

    /**
     * 我最难受,我夹在中间 ,没人使用我,让我一个人静静
     * #exclude
     * #include
     * #version 1.2
     * #return
     */
    @MethodVersionAnnotation(version = "1.2")
    public ModelAndView index(int b) {
        return new ModelAndView("custom/procing/index");
    }

    /**
     * 我是1.0版本的index方法,没有神马用,若要使用index请使用上面1.4版本的吧
     * #param request
     * #exclude  request
     * #include
     * #version 1.0
     * #return
     */
    @MethodVersionAnnotation(version = "1.0")
    public ModelAndView index(HttpServletRequest request,@Validated(Add.class) User users, String ss ,List<Map<String, Object>> list,File f,int i,@Validated(Add.class) User user) {
        return new ModelAndView("custom/procing/index");
    }


}