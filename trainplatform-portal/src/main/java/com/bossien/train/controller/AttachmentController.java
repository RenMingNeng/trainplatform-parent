package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.mongodb.gridfs.GridFSDBFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("attachment")
public class AttachmentController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable String id, HttpServletResponse response) throws IOException {
        Query query = new Query();
        Criteria criteria = GridFsCriteria.where("_id").is(id);
        query.addCriteria(criteria);
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(query);
        if(null == gridFSDBFile) {
            response.getWriter().write("file not found");
            return;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(gridFSDBFile.getFilename().getBytes("UTF-8"), "ISO-8859-1"));
        gridFSDBFile.writeTo(response.getOutputStream());
    }

}
