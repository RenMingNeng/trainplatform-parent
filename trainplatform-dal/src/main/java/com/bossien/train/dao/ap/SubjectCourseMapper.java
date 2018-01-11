package com.bossien.train.dao.ap;

import com.bossien.train.domain.SubjectCourse;

import java.util.List;
import java.util.Map;

public interface SubjectCourseMapper {
    int deleteByPrimaryKey(String intId);

    int deleteByCourseId(Map<String, Object> map);


    int deleteByTrainSubjectId(String TrainSubjectId);

    int insert(SubjectCourse record);

    int insertSelective(SubjectCourse record);

    SubjectCourse selectByPrimaryKey(String intId);

    int updateByPrimaryKeySelective(SubjectCourse record);

    int updateByPrimaryKey(SubjectCourse record);

    /**
     * 根据主题Id拿到课程Id集合
     *
     * @param subjectId
     * @return
     */
    List<String> selectCourseIds(String subjectId);

    /**
     * 批量添加数据
     *
     * @param subjectCourses
     * @return
     */
    int insertBatch(List<SubjectCourse> subjectCourses);

}