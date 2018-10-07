package wtssg.xdly.workflow.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wtssg.xdly.attend.dao.AttendMapper;
import wtssg.xdly.attend.entity.Attend;
import wtssg.xdly.workflow.dao.ReAttendMapper;
import wtssg.xdly.workflow.entity.ReAttend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ReAttendServiceImpl")
public class ReAttendServiceImpl implements ReAttendService {

    /**
     * 补签流程状态
     */
    private static final Byte RE_ATTEND_STATUS_ONGOING = 1;
    private static final Byte RE_ATTEND_STATUS_PASS = 2;
    private static final Byte RE_ATTEND_STATUS_REFUSE = 3;
    /**
     * 任务关键补签数据键
     */
    private static final String RE_ATTEND_SIGN = "re_attend";
    /**
     * 流程下一步处理人
     */
    private static final String NEXT_HANDLER = "next_handler";
    /**
     * 工作流ID
     */
    private static final String RE_ATTEND_FLOW_ID = "re_attend";
    private static final Byte ATTEND_STATUS_NORMAL = 1;


    @Autowired
    ReAttendMapper reAttendMapper;

    @Autowired
    AttendMapper attendMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    @Override
    public List<ReAttend> listReAttend(String username) {
        List<ReAttend> list =reAttendMapper.selectReAttendRecord(username);
        return list;
    }

    /**
     * 开启补签工作流
     * @param reAttend
     */
    @Override
    public void startReAttendFlow(ReAttend reAttend) {
        // 从公司组织架构中，查询此人的上级领导用户名
        reAttend.setCurrentHandler("boss");
        reAttend.setStatus(RE_ATTEND_STATUS_ONGOING);
        // 插入数据库补签表中
        reAttendMapper.insertSelective(reAttend);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(RE_ATTEND_SIGN, reAttend);
        map.put(NEXT_HANDLER,reAttend.getCurrentHandler());
        // 启动补签流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(RE_ATTEND_FLOW_ID, map);
        // 提交用户补签任务
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        taskService.complete(task.getId(), map);
    }

    /**
     * 根据流程任务变量，查询某人需要处理的任务
     * @param userName
     * @return
     */
    @Override
    public List<ReAttend> listTasks(String userName) {
        List<ReAttend> reAttendList = new ArrayList<ReAttend>();
        List<Task> taskList = taskService.createTaskQuery().processVariableValueEquals(userName).list();
        // 转化为页面实体
        if (CollectionUtils.isNotEmpty(taskList)) {
            for (Task task : taskList) {
                Map<String, Object> variable = taskService.getVariables(task.getId());
                ReAttend reAttend = (ReAttend) variable.get(RE_ATTEND_SIGN);
                reAttendList.add(reAttend);
            }
        }
        return reAttendList;
    }

    /**
     * 审批工作流
     * @param reAttend
     */
    @Override
    @Transactional
    public void approve(ReAttend reAttend) {
        Task task = taskService.createTaskQuery().taskId(reAttend.getTaskId()).singleResult();
        if (("" + RE_ATTEND_STATUS_PASS).equals(reAttend.getApproveFlag())) {
            // 审批通过 修改补签数据状态
            // 修改相关考勤数据 考勤状态改为正常
            Attend attend = new Attend();
            attend.setId(reAttend.getAttendId());
            attend.setAttendStatus(ATTEND_STATUS_NORMAL);
            attendMapper.updateByPrimaryKeySelective(attend);
            reAttend.setStatus(RE_ATTEND_STATUS_PASS);
            reAttendMapper.updateByPrimaryKeySelective(reAttend);
            attendMapper.updateByPrimaryKey(attend);
        } else if (("" + RE_ATTEND_STATUS_REFUSE).equals(reAttend.getApproveFlag())) {
            reAttend.setStatus(RE_ATTEND_STATUS_REFUSE);
            reAttendMapper.updateByPrimaryKeySelective(reAttend);
        }
        taskService.complete(reAttend.getTaskId());
    }

}
