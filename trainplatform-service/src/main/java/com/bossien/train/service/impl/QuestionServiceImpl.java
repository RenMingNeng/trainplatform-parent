package com.bossien.train.service.impl;

import com.bossien.train.dao.ex.QuestionMapper;
import com.bossien.train.domain.*;
import com.bossien.train.domain.dto.QuestionMessage;
import com.bossien.train.domain.eum.QuestionsTypeEmun;
import com.bossien.train.service.*;
import com.bossien.train.util.MapUtils;
import com.bossien.train.util.PropertiesUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class QuestionServiceImpl implements IQuestionService {
    private static final JsonParser jsonParser = new JsonParser();
    public static String EXERCISE_RANDOM_QUESTION_NUMBER = PropertiesUtils.getValue("EXERCISE_RANDOM_QUESTION_NUMBER");

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private IFastDFSService fastDFSService;
    @Autowired
    private IQuestionCollectionService questionCollectionService;
    @Autowired
    private IExamAnswersService examAnswersService;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectCourseService projectCourseService;

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params,
                                                List<QuestionCollection> collects, List<ExamAnswers> answers) {
        params.put("chrValid", "1");
        List<Question> questions = questionMapper.selectList(params);

        return combinQuestion(questions, collects, answers);
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params, List<QuestionCollection> collects) {

        return selectList(params, collects, new ArrayList<ExamAnswers>());
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {

        return selectList(params, new ArrayList<QuestionCollection>(), new ArrayList<ExamAnswers>());
    }

    @Override
    public List<Map<String, Object>> selectListByType(Map<String, Object> params, String type, List<QuestionCollection> collects) {
        List<Map<String, Object>> rs = new ArrayList<>();
        if (null != params && null != type && !type.equals("")) {
            //courseList随机练习 single单选 many多选 judge判断 fillout填空 quesAns问答

            params.put("projectId", params.get("project_id"));
            params.put("userId", params.get("user_id"));

            //先查询角色 项目id+用户id
            ProjectUser projectUser = projectUserService.selectByProjectIdAndUserId(params.get("project_id").toString(),
                    params.get("user_id").toString());
            if (null == projectUser) {
                return rs;
            }
            //再根据角色查询课程集合
            params.put("roleId", projectUser.getRoleId());
            List<String> courseList = projectCourseService.selectCourseIds(params.get("project_id").toString(), projectUser.getRoleId());
            if (null == courseList || courseList.size() < 1) {
                return rs;
            }
            //根据课程集合查询所有题
            params.put("intCourseIds", courseList);
            List<String> questionList = courseQuestionService.selectQuestionIdList(params);
            if (null == questionList || questionList.size() < 1) {
                return rs;
            }
            //根据条件查询试题详情
            params = new HashedMap();
            if (type.equals("courseList")) {//随机练习
                //打乱顺序
                Collections.shuffle(questionList);
                if (Integer.parseInt(EXERCISE_RANDOM_QUESTION_NUMBER) < questionList.size()) {
                    questionList = questionList.subList(0, 150);
                }
            } else if (type.equals("single")) {
                params.put("chrType", QuestionsTypeEmun.QUESTIONTYPE_SINGLE.getValue());
            } else if (type.equals("many")) {
                params.put("chrType", QuestionsTypeEmun.QUESTIONTYPE_MANY.getValue());
            } else if (type.equals("judge")) {
                params.put("chrType", QuestionsTypeEmun.QUESTIONTYPE_JUDGE.getValue());
            }
            params.put("intIds", questionList);
            rs = selectList(params, collects, new ArrayList<ExamAnswers>());
        }
        return rs;
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return questionMapper.selectCount(params);
    }

    @Override
    public Map<String, Object> selectOne(Map<String, Object> params) {
        Question queryBean = questionMapper.selectOne(params);
        Map<String, Object> result = MapUtils.newHashMap();
        MapUtils.putAll(result, queryBean);
        return result;
    }

    @Override
    public Question selectById(Map<String, Object> params) {
        return questionMapper.selectById(params);
    }

    @Override
    public int insertSelective(QuestionMessage questionMessage) {
        Question question = new Question();
        question.setIntId(questionMessage.getQuestionId());
        question.setVarNo(questionMessage.getQuestionNo());
        question.setVarTitle(questionMessage.getQuestionTitle());
        question.setVarContent(questionMessage.getQuestionContent());
        question.setChrCategory(questionMessage.getQuestionCategory());
        question.setChrType(questionMessage.getQuestionType());
        question.setIntDifficult(questionMessage.getQuestionDifficult());
        question.setChrValid(questionMessage.getQuestionValid());
        question.setVarAnswer(questionMessage.getQuestionAnswer());
        question.setVarAnswerDesc(questionMessage.getQuestionAnswerDesc());
        question.setVarSource(questionMessage.getQuestionSource());
        question.setVarAnalysis(questionMessage.getQuestionAnalysis());
        question.setVarExamPoint(questionMessage.getQuestionExamPoint());
        question.setVarCreateUser(questionMessage.getCreateUser());
        question.setDatCreateDate(questionMessage.getDatCreateDate());
        question.setVarOperUser(questionMessage.getOperUser());
        question.setDatOperDate(questionMessage.getDatOperDate());
       /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        question.setDatCreateDate(dateFormat.format(new Date()));
        question.setDatOperDate(dateFormat.format(new Date()));*/
        questionMapper.insertSelective(question);
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(QuestionMessage questionMessage) {
        Question question = new Question();
        question.setIntId(questionMessage.getQuestionId());
        question.setVarNo(questionMessage.getQuestionNo());
        question.setVarTitle(questionMessage.getQuestionTitle());
        question.setVarContent(questionMessage.getQuestionContent());
        question.setChrCategory(questionMessage.getQuestionCategory());
        question.setChrType(questionMessage.getQuestionType());
        question.setIntDifficult(questionMessage.getQuestionDifficult());
        question.setChrValid(questionMessage.getQuestionValid());
        question.setVarAnswer(questionMessage.getQuestionAnswer());
        question.setVarAnswerDesc(questionMessage.getQuestionAnswerDesc());
        question.setVarSource(questionMessage.getQuestionSource());
        question.setVarAnalysis(questionMessage.getQuestionAnalysis());
        question.setVarExamPoint(questionMessage.getQuestionExamPoint());
        question.setVarCreateUser(questionMessage.getCreateUser());
        question.setDatCreateDate(questionMessage.getDatCreateDate());
        question.setVarOperUser(questionMessage.getOperUser());
        question.setDatOperDate(questionMessage.getDatOperDate());
        questionMapper.updateByPrimaryKeySelective(question);

        return 0;
    }

    @Override
    public int insertBatch(List<Question> list) {
        return questionMapper.insertBatch(list);
    }

    @Override
    public int updateBatch(List<Question> list) {
        return questionMapper.updateBatch(list);
    }

    @Override
    public int deleteByPrimaryKey(Map<String, Object> params) {
        return questionMapper.deleteByPrimaryKey(params);
    }

    @Override
    public List<Integer> selectQuestionsTypeCount(Map<String, Object> params) {
        return questionMapper.selectQuestionsTypeCount(params);
    }

    @Override
    public Map<String, Object> selectTypeCount(Map<String, Object> params) {
        return questionMapper.selectTypeCount(params);
    }

    public List<Map<String, Object>> combinQuestion(List<Question> questions,
                                                    List<QuestionCollection> collects, List<ExamAnswers> answers) {
        List<Map<String, Object>> qList = new ArrayList<>();
        Map<String, Object> qMap;
        for (int i = 0; i < questions.size(); i++) {
            qMap = new HashedMap();
            Question question = questions.get(i);
            //将基本数据导入map中
            MapUtils.putAll(qMap, question);


            Object json = question.getVarContent();
            Gson gson = new Gson();
            Content content = gson.fromJson(String.valueOf(json), Content.class);
            //选项
            qMap.putAll(setOptionInfo(content.getOptions()));
            qMap.put("varQuestionsContent", content.getTitle());
            qMap.put("questionTitleFiles", makeFileInfo(content.getTitleImgs()));
            //解析
            String varAnalysis = question.getVarAnalysis();
            if (null != varAnalysis && !varAnalysis.equals("")) {
                Analysis analysis = gson.fromJson(String.valueOf(question.getVarAnalysis()), Analysis.class);
                qMap.putAll(setAnalysis(analysis));
            }
            if (collects.size() > 0) {
                qMap.put("chrIsCollect", checkIsCollect(question, collects));
            }

            if (answers.size() > 0) {
                qMap.putAll(checkIsAnswer(question, answers));
            }

            qList.add(qMap);
        }
        return qList;
    }

    /**
     * 解析
     *
     * @param analysis
     * @return
     */
    public Map<String, Object> setAnalysis(Analysis analysis) {
        Map<String, Object> result = new HashedMap();
        if (null == analysis) {
            return result;
        }
        String content = analysis.getContent();
        List<FileInfo> images = analysis.getImages();
        result.put("analysisContent", content);
        result.put("analysisFiles", makeFileInfo(images));
        return result;
    }

    /**
     * 组装多个附件对象
     *
     * @param fileInfos
     * @return
     */
    public List<Map<String, Object>> makeFileInfo(List<FileInfo> fileInfos) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (null == fileInfos || fileInfos.size() < 1) {
            return result;
        }
        for (FileInfo fileInfo : fileInfos) {
            result.add(makeFileInfo(fileInfo));
        }
        return result;
    }

    /**
     * 组装单个附件对象
     *
     * @param fileInfo
     * @return
     */
    public Map<String, Object> makeFileInfo(FileInfo fileInfo) {
        Map<String, Object> result = new HashedMap();
        if (null == fileInfo) {
            return result;
        }
        String fileId = fileInfo.getFileId();
        String fileName = fileInfo.getFileName();
        if (null == fileId || fileId.equals("") ||
                null == fileName || fileName.equals("")) {
            return result;
        }
        result.put("mimeType", getMimeType(fileName));
        result.put("mimeUrl", fastDFSService.getURLToken(fileId));
        return result;
    }

    /**
     * //是否收藏：1收藏2未收藏
     *
     * @param question
     * @return
     */
    public boolean checkIsCollect(Question question, List<QuestionCollection> collects) {
        String intId = question.getIntId();
        if (null != intId && intId.length() > 0 && collects.size() > 0) {
            for (QuestionCollection questionCollection : collects) {
                if (null != questionCollection &&
                        !questionCollection.getQuestion_id().equals("") &&
                        questionCollection.getQuestion_id().equals(intId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否答对
     *
     * @param question
     * @return
     */
    public Map<String, Object> checkIsAnswer(Question question, List<ExamAnswers> answers) {
//        true答对false答错
        Map<String, Object> result = new HashedMap();
        String intId = question.getIntId();
        if (null != intId && intId.length() > 0 && answers.size() > 0) {
            for (ExamAnswers examAnswers : answers) {
                String aIntId = String.valueOf(examAnswers.getQuestion_id());
                if (!intId.equals("null") && intId.equals(aIntId)) {
                    result.put("myAnswer", examAnswers.getAnswer());
                    result.put("isCorrect", examAnswers.getIs_correct());//1答对2答错
                }
            }
        }
        return result;
    }

    /**
     * 组装选项
     *
     * @param options
     * @return
     */
    public Map<String, Object> setOptionInfo(List<Option> options) {
        Map<String, Object> result = new HashedMap();
        List<Map<String, Object>> optionList = new LinkedList<>();
        if (null == options || options.size() < 1) {
            return result;
        }
        //选项个数
        int intOptions = 0;
        for (Option option : options) {
            intOptions++;
            FileInfo fileInfo = option.getFileInfo();
            Map<String, Object> data = new HashedMap();
            data.put("oType", option.getItem());
            data.put("optionContent", option.getItemDesc());
            data.putAll(makeFileInfo(fileInfo));
            optionList.add(data);
        }
        result.put("options", optionList);
        result.put("intOptions", intOptions);
        return result;
    }

    /**
     * 获取文本类型
     *
     * @param varFileName
     * @return
     */
    public String getMimeType(String varFileName) {
        String img = "png,PNG,jpeg,JPEG,BMP,bmp,jpg,JPG";
        String mp3 = "mp3,MP3";
        String video = "SWF,swf,FLV,flv,mp4,MP4";
        String suffix = suffix(varFileName);// 获取后缀名称
        if (isContains(img, suffix)) {// 是否包含该后缀
            return "img";
        }
        if (isContains(mp3, suffix)) {
            return "mp3";
        }
        if (isContains(video, suffix)) {
            return "video";
        }
        throw new RuntimeException("文件类型不匹配!");
    }

    /**
     * 获取文件后缀  例如: "文件安全.docx"------>"docx"
     *
     * @param fileName
     * @return
     */
    public static String suffix(String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return suffix;
    }

    /**
     * 目标串是否是源字符串的子串
     */
    public static boolean isContains(String src, String tag) {
        if (tag == null) {
            return false;
        }
        Pattern r = Pattern.compile(tag);
        Matcher matcher = r.matcher(src);
        return matcher.find();
    }

}
