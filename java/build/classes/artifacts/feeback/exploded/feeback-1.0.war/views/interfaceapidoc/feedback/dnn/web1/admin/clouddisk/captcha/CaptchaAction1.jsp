<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><div id='4'> <div id='4-1'><div div class='content' id='4-1-1'><div class='main'><div class='useGuide'><h5>validCaptcha</h5><p class='explain'></br>no Description</p></div><div class='main_body'><div class='Interface'><h6>接口例子</h6><div class='method'><p><span>令牌</span>需要(未做)</p><p><span>版本号</span>No Version Write</p><p><span>请求方式</span>POST </p></div><div class='command'><div class='com_con'> curl -X POST  --cookie 'token=b42a5e5845c046c49eec3d01c63365c0.2130706433.1476337615937' -d 请自己在这里拼参数&nbsp;'http://localhost:8080/admin/clouddisk/captcha/valid'</div></div>响应报文 :<div class='command'>无</div></div><br/><div class='parameter'><h6>参数</h6><table  cellspacing='0'><thead><tr><th>参数类型</th><th>参数名</th><th>参数约束说明</th></tr></thead><tbody><tr ><td>String</td><td>e</td><td></td></tr><tr ><td> CaptchaValidator</td><td>this</td><td><b>captchaValue : </b>String  ,<br/><b>addTime : </b>.LocalDateTime  ,<br/><b>validTime : </b>.LocalDateTime  ,<br/><b>captFilePath : </b>String  ,<br/><b>validMsg : </b>String  ,<br/><b>success : </b>boolean  ,<br/></td></tr></tbody></table></div></div></div></div><div div class='content' id='4-1-2'><div class='main'><div class='useGuide'><h5>needCaptcha</h5><p class='explain'></br>no Description</p></div><div class='main_body'><div class='Interface'><h6>接口例子</h6><div class='method'><p><span>令牌</span>需要(未做)</p><p><span>版本号</span>No Version Write</p><p><span>请求方式</span></p></div><div class='command'></div>响应报文 :<div class='command'>无</div></div><br/><div class='parameter'><h6>参数</h6><table  cellspacing='0'><thead><tr><th>参数类型</th><th>参数名</th><th>参数约束说明</th></tr></thead><tbody><tr ><td>String</td><td>account</td><td></td></tr></tbody></table></div></div></div></div><div div class='content' id='4-1-3'><div class='main'><div class='useGuide'><h5>readCaptcha</h5><p class='explain'></br>no Description</p></div><div class='main_body'><div class='Interface'><h6>接口例子</h6><div class='method'><p><span>令牌</span>需要(未做)</p><p><span>版本号</span>No Version Write</p><p><span>请求方式</span>GET </p></div><div class='command'><div class='com_con'> curl -X GET  --cookie 'token=b42a5e5845c046c49eec3d01c63365c0.2130706433.1476337615937' 'http://localhost:8080/admin/clouddisk/captcha/read?请自己在这里拼参数'</div></div>响应报文 :<div class='command'>无</div></div><br/><div class='parameter'><h6>参数</h6><table  cellspacing='0'><thead><tr><th>参数类型</th><th>参数名</th><th>参数约束说明</th></tr></thead><tbody><tr ><td>String</td><td>e</td><td></td></tr><tr ><td> OutputStream</td><td>e</td><td></td></tr></tbody></table></div></div></div></div></div></div>