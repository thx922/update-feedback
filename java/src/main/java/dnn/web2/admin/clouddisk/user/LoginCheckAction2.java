package dnn.web2.admin.clouddisk.user;

import com.dounine.clouddisk360.parser.UserCheckLoginParser;
import com.dounine.clouddisk360.parser.deserializer.user.check.UserCheckLogin;
import com.dounine.clouddisk360.parser.deserializer.user.check.UserCheckLoginParameter;
import dnn.common.json.ResponseText;
import dnn.web2.admin.clouddisk.ClouddiskAction2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/clouddisk/loginCheck")
public class LoginCheckAction2 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginCheckAction2.class);

	@RequestMapping(value = "clear",method = RequestMethod.GET)
	public ResponseText needCaptcha() {
		ResponseText responseText = new ResponseText();
		UserCheckLoginParameter userCheckLoginParameter = new UserCheckLoginParameter();
		userCheckLoginParameter.setClearCacheObject(true);//清除全局静态缓存
		userCheckLoginParameter.setClearCacheStore(true);//清除本地cookieStore缓存文件
		UserCheckLoginParser userCheckLoginParser = new UserCheckLoginParser(ClouddiskAction2.LOGIN_USER_TOKEN);
		UserCheckLogin userCheckLogin = userCheckLoginParser.parse(userCheckLoginParameter);
		responseText.setErrno(userCheckLogin.getErrno());
		responseText.setMsg(userCheckLogin.getCddmsg());
		return responseText;
	}

}
