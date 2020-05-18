package com.da.shuai.shardingspherecase.controller;


import com.da.shuai.shardingspherecase.entity.Student;
import com.da.shuai.shardingspherecase.service.StudentService;
import com.da.shuai.shardingspherecase.service.mq.StudentProvider;
import com.da.shuai.shardingspherecase.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * (Student)表控制层
 *
 * @author ${USER}
 * @since 2020-05-08 13:30:50
 */
@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {
    /**
     * 服务对象
     */
    @Resource
    private StudentService studentService;

    @Resource
    private StudentProvider studentProvider;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public Student selectOne(Integer id) {

        boolean b = redisUtil.hasKey(id+"");
        Student o = (Student)redisUtil.get(id + "");

//        Object o = redisUtil.get("student::134");
//        Student o1 = (Student)redisUtil.get("student::134");
        return this.studentService.queryById(id);
    }

    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    public List<Student> queryAll() {
        return this.studentService.queryAllByLimit(0, 10);
    }

    @RequestMapping("/saveMQ")
    public void saveStudent(String name, Integer age) {
        studentProvider.save();
    }

    @RequestMapping("/insertOne")
    public Student insertOne(Integer id,String name, Integer age) {
        Student student = new Student();
      
        student.setId(id);
        student.setName(name);
        student.setSex(age%2==0?"男":"女");
        student.setAge(age);
        student.setStuclass((age/2)+"班");
        redisUtil.set(id+"",student);
       return this.studentService.insert(student);
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Student updateStudent(Integer id,String name){
        Student student = this.studentService.queryById(id);
        student.setName(name);
       return  this.studentService.update(student);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Boolean deleteStudent(Integer id){

       return this.studentService.deleteById(id);

    }

}