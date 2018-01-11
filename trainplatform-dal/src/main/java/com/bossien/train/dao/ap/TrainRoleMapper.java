package com.bossien.train.dao.ap;

import com.bossien.train.domain.TrainRole;
import com.bossien.train.domain.UserTrainRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface TrainRoleMapper {

    /**
     * 新增
     *
     * @param params
     */
    void insert(TrainRole params);

    /**
     * 修改
     *
     * @param params
     */
    void update(Map<String, Object> params);

    int insertBatch(List<Map<String, Object>> list);

    int updateBatch(List<Map<String, Object>> list);

    /**
     * 删除
     *
     * @param params
     */
    void delete(Map<String, Object> params);


    /**
     * 分页查询列表
     *
     * @param params
     * @return
     */
    List<TrainRole> selectList(Map<String, Object> params);

    /**
     * 受训角色
     */
    List<TrainRole> selectVerify(Map<String, Object> params);

    /**
     * 统计
     *
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 查询单条记录
     *
     * @param params
     * @return
     */
    TrainRole selectOne(TrainRole params);

    //通过受训id和公司id来查询
    TrainRole selectByRole(Map<String, Object> params);

    TrainRole selectById(Map<String, Object> params);

    /**
     * 通过companyId查询受训角色
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectTrainRoles(Map<String, Object> params);

    List<Map<String, Object>> selectByParams(Map<String, Object> params);

    List<TrainRole> selectRoleByIds(List<String> list);

    /**
     * 高级设置中查询公司培训角色
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectRoles(Map<String, Object> params);
}
