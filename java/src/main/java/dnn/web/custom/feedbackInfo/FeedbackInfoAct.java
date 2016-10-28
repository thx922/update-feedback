package dnn.web.custom.feedbackInfo;

/** #name file function
 * Created by lgq on 16-9-4.
 */

import dnn.common.MethodVersionAnnotation;
import dnn.dto.feedbackInfo.FeedbackInfoDto;
import dnn.common.json.ResponseText;
import dnn.entity.feedbackInfo.detection.DetectionInfo;
import dnn.entity.feedbackInfo.FeedbackInfo;
import dnn.entity.feedbackInfo.specimen.ChemicalCell;
import dnn.entity.feedbackInfo.specimen.PhysicalEnergy;
import dnn.entity.user.User;
import dnn.enums.DisposeType;
import dnn.enums.FeedbackStatus;
import dnn.enums.OperatorStatus;
import dnn.service.feedbackInfo.ISerFeedbackInfo;
import dnn.service.user.ISerUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("custom/feedbackInfo")
public class FeedbackInfoAct {
    @Autowired
    private ISerFeedbackInfo iSerFeedbackInfo;

    /**
     * 保存
     *
     * #exclude
     * #include
     * #version 1.0
     * #return
     */

//    @GetMapping
//    @PutMapping
//    @PatchMapping
//    @DeleteMapping
//    @RequestMapping(method = RequestMethod.GET,value = "asdf")
    @MethodVersionAnnotation(version = "1.0")
    @PostMapping("save")
    public ResponseText save(FeedbackInfo feedbackInfo) throws Throwable {
        iSerFeedbackInfo.save(feedbackInfo);
        return new ResponseText<>();
    }

    @MethodVersionAnnotation(version = "1.0")
    @GetMapping("findById")
    public ResponseText findById(String id) throws Throwable {
        ResponseText text = new ResponseText(iSerFeedbackInfo.findById(id));
        return text;
    }

    /**
     * 查看所有分页工单
     *dto (feedbackstatus or detectionNum(编号))
     * #nameDes 查看所有分页工单maps
     * #example{status:'UNTREATED',detectionNum:'2016ST2',filename:'**\*.[png]'}
     * #exclude
     * #include
     * #version 1.0
     * #return{"count":2,"rows":[{"chemicalCell":{"sampleName":"此而可忍发"},"copyNum":3,"createTime":"2016-09-12T11:26:30.906","customerName":"刘霞","detectionInfo":{"basis":"符合如而非","disposeType":"PICKUP"},"detectionNo":1,"detectionNum":"2016ST1","feedbackStatus":"UNTREATED","finalCopyField":1,"id":"57d6206661d0c878ffe2380a","invoiceInfo":{"name":"好多次而非"},"operatorStatus":"MANAGERCONFIRM","userId":"57cfdf3179d23f8f570f49fd"},{"chemicalCell":{"sampleName":"等军工而"},"copyNum":1,"createTime":"2016-09-12T11:28:18.932","customerName":"刘霞","detectionInfo":{"basis":"觉得偶地位","disposeType":"PICKUP"},"detectionNo":2,"detectionNum":"2016ST2","feedbackStatus":"UNTREATED","finalCopyField":1,"id":"57d620d761d0c878ffe2380b","invoiceInfo":{"name":"发热头诶"},"operatorStatus":"INIT","userId":"57cfdf3179d23f8f570f49fd"}]},"errno":0}
     */
    @MethodVersionAnnotation(version = "1.0")
    @PostMapping("maps")
    public ResponseText maps(FeedbackInfoDto dto) throws Throwable {
        Map<String, Object> maps = new HashMap<>();
        maps.put("rows", iSerFeedbackInfo.findByPage(dto));
        maps.put("count", iSerFeedbackInfo.count(dto));
        return new ResponseText(maps);
    }

    /**
     * 查看详细内容
     *feedbackInfo (userId and detectionNum(编号))
     * 返回最新的版本
     * #exclude
     * #include
     * #version 1.0
     * #return   fdg
     */
    @MethodVersionAnnotation(version = "1.0")
    @GetMapping("feedbackDetail")
    public ResponseText feedbackDetail(FeedbackInfo feedbackInfo) throws Throwable {
        ResponseText text = new ResponseText(iSerFeedbackInfo.findOneInfo(feedbackInfo));
        return text;
    }

    /**
     * 工单流程状态修改
     * feedbackInfo (userId and detectionNum(编号))
     *修改的是副本
     * #exclude
     * #include
     * #version 1.0
     * #return
     */
    @MethodVersionAnnotation(version = "1.0")
    @GetMapping("updateFeedbackOperateStatus")
    public ResponseText updateFeedbackOperateStatus(FeedbackInfo feedbackInfo) throws Throwable {
        iSerFeedbackInfo.updateOneInfo(feedbackInfo);
        ResponseText text = new ResponseText();
        return text;
    }

    /**
     * 管理员最后对工单受理确认
     *
     * #exclude
     * #include
     * #version 1.0
     * #return
     */
    @MethodVersionAnnotation(version = "1.0")
    @GetMapping("confirmFeedback")
    public ResponseText confirmFeedback(FeedbackInfo feedbackInfo) throws Throwable {
        iSerFeedbackInfo.confirmFeedback(feedbackInfo);
        ResponseText text = new ResponseText();
        return text;
    }

    /**
     * 删除
     *feedbackInfo (userId and detectionNum(编号))
     * #param request
     * #exclude
     * #include
     * #version 1.0
     * #return
     */
    @MethodVersionAnnotation(version = "1.0")
    @GetMapping("deleteFeedback")
    public ResponseText deleteFeedback(FeedbackInfo feedbackInfo) throws Throwable {
        int result = iSerFeedbackInfo.deleteFeedback(feedbackInfo);
        ResponseText text = new ResponseText(result);
        return text;
    }

}
