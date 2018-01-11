package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ProjectExerciseOrderMapper;
import com.bossien.train.domain.ProjectExerciseOrder;
import com.bossien.train.service.IProjectExerciseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/27.
 */
@Service("projectExerciseOrderService")
public class ProjectExerciseOrderServiceImpl implements IProjectExerciseOrderService {
    @Autowired
    private ProjectExerciseOrderMapper projectExerciseOrderMapper;
    @Override
    public List<Map<String, Object>> selectProjectExerciseOrderList(
        Map<String, Object> params) {
        return projectExerciseOrderMapper.selectProjectExerciseOrderList(params);
    }

    @Override
    public int selectProjectExerciseOrderCount(Map<String, Object> params) {
        return projectExerciseOrderMapper.selectProjectExerciseOrderCount(params);
    }

    @Override
    public int insertBatch(List<Map<String, Object>> item) {
        return projectExerciseOrderMapper.insertBatch(item);
    }

    @Override
    public List<Map<String, Object>> selectExerciseInfoList(Map<String, Object> param) {
        return projectExerciseOrderMapper.selectExerciseInfoList(param);
    }

    @Override
    public ProjectExerciseOrder selectOne(Map<String, Object> params) {
        return projectExerciseOrderMapper.selectOne(params);
    }

    @Override
    public void update(ProjectExerciseOrder projectExerciseOrder) {
        projectExerciseOrderMapper.update(projectExerciseOrder);
    }

    @Override
    public int delete(Map<String, Object> params) {
        return projectExerciseOrderMapper.delete(params);
    }

    @Override
    public List<String> selectIdList(Map<String, Object> params) {
        return projectExerciseOrderMapper.selectIdList(params);
    }

    @Override
    public int updateInfo(Map<String, Object> params) {
        return projectExerciseOrderMapper.updateInfo(params);
    }

    @Override
    public void updateBatch(List<Map<String, Object>> param) {
        projectExerciseOrderMapper.updateBatch(param);
    }
}
