package top.jpower.jpower.task.jdbc;

import top.jpower.jpower.enums.TaskStatusEnum;
import top.jpower.jpower.enums.TaskTypeEnum;
import top.jpower.jpower.module.common.support.ChainMap;
import top.jpower.jpower.module.common.utils.BeanUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.SqlUtil;
import top.jpower.jpower.task.entity.TaskDelay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2023/7/2 11:36 AM
 */
@Repository
@Slf4j
public class TaskDelayJdbc {

    @Resource
    private NamedParameterJdbcTemplate taskNamedParameterJdbcTemplate;

    /**
     * 保存
     *
     * @author mr.g
     * @param taskTimeBean 任务
     * @return 是否成功
     **/
    public boolean save(TaskDelay taskDelay) {
        String insertSql = SqlUtil.buildInsertMysql(TaskDelay.class);
        if (Fc.isBlank(insertSql)){
            log.error("TaskDelay生成的SQL语句是空的");
        }
        return taskNamedParameterJdbcTemplate.update(insertSql, BeanUtil.beanToMap(taskDelay)) > 0;
    }

    /**
     * 查询任务前100条
     *
     * @author mr.g
     * @param serverName 服务名称
     * @param taskStatus 任务状态
     * @param taskType 任务类型
     * @return 任务列表
     **/
    public List<TaskDelay> queryByServer(String serverName, TaskStatusEnum taskStatus, TaskTypeEnum taskType) {

        Map<String,Object> map = ChainMap.<String, Object>create().put("serverName",serverName).put("status", taskStatus.getCode()).put("type", taskType.getCode()).build();
        return taskNamedParameterJdbcTemplate.query("select id, task_time, is_run from task_delay where is_run = 0 and app_name = :serverName and type = :type and status = :status and is_deleted = 0 order by task_time limit 100 FOR UPDATE", map, new BeanPropertyRowMapper<>(TaskDelay.class));

//        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
//        DataSourceTransactionManager dm = new DataSourceTransactionManager(taskNamedParameterJdbcTemplate.getJdbcTemplate().getDataSource());
//        TransactionStatus tmp = dm.getTransaction(transactionDefinition);
//
//        try{
//            // todo 如果读取之后服务停止了,重启服务后如何读取到?
//
//            Map<String,Object> map = ChainMap.<String, Object>create().put("serverName",serverName).put("status", taskStatus.getCode()).put("type", taskType.getCode()).build();
//            List<TaskDelay> list = taskNamedParameterJdbcTemplate.query("select * from task_delay where is_read = 0 and app_name = :serverName and type = :type and status = :status and is_deleted = 0 order by task_time limit 100 FOR UPDATE", map, new BeanPropertyRowMapper<>(TaskDelay.class));
//
//            if (Fc.isNotEmpty(list)){
//                MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
//                mapSqlParameterSource.addValue("ids", list.stream().map(TaskDelay::getId).collect(Collectors.toList()));
//                taskNamedParameterJdbcTemplate.update("update task_delay set is_read = 1 where id in (:ids)", mapSqlParameterSource);
//            }
//
//            dm.commit(tmp);
//            return list;
//        } catch (Exception e) {
//            dm.rollback(tmp);
//            throw e;
//        }
    }

    /**
     * 查询需要执行任务条数
     *
     * @author mr.g
     * @param serverName 服务名称
     * @param taskStatus 任务状态
     * @param taskType 任务类型
     * @return 任务条数
     **/

    public Long countByServer(String serverName, TaskStatusEnum taskStatus, TaskTypeEnum taskType) {
        Map<String,Object> map = ChainMap.<String, Object>create().put("serverName",serverName).put("status", taskStatus.getCode()).put("type", taskType.getCode()).build();
        return taskNamedParameterJdbcTemplate.queryForObject("select count(*) from task_delay where app_name = :serverName and type = :type and status = :status and is_deleted = 0", map, Long.class);
    }

    /**
     * 获取一条任务,且如果没有在执行则更新成正在执行
     *
     * @author mr.g
     * @param id
     * @return
     **/
    public TaskDelay getByIdAndUpdateIsRun(Long id) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        DataSourceTransactionManager dm = new DataSourceTransactionManager(taskNamedParameterJdbcTemplate.getJdbcTemplate().getDataSource());
        TransactionStatus tmp = dm.getTransaction(transactionDefinition);

        try{
            // todo 如果读取之后服务停止了,重启服务后如何读取到?

            Map<String,Object> map = ChainMap.<String, Object>create().put("id",id).build();
            TaskDelay delay = taskNamedParameterJdbcTemplate.queryForObject("select * from task_delay where id = :id FOR UPDATE", map, new BeanPropertyRowMapper<>(TaskDelay.class));

            if (Fc.notNull(delay) && !Fc.toBoolean(delay.getIsRun(), Boolean.FALSE)){
                taskNamedParameterJdbcTemplate.update("update task_delay set is_run = 1 where id = :id", map);
            }

            dm.commit(tmp);
            return delay;
        } catch (Exception e) {
            dm.rollback(tmp);
            throw e;
        }
    }

    /**
     * 更新任务状态
     * @author mr.g
     * @param status 任务状态
     * @param id 任务ID
     * @return
     **/
    public boolean updateStatusById(Integer status, Long id) {
        return taskNamedParameterJdbcTemplate.update("update task_delay set is_run = 0 , status = :status where id = :id", ChainMap.<String,Object>create().put("status",status).put("id",id).build()) > 0;
    }

}
