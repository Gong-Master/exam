package com.zzxx.exam.service;


/*
* 所有业务的模型：登录，开始考试，查看规则，交卷，上一题，下一题
**/

import com.zzxx.exam.entity.*;
import com.zzxx.exam.ui.ExamFrame;
import com.zzxx.exam.ui.MenuFrame;
import com.zzxx.exam.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

public class ExamService {
    private EntityContext entityContext;   //数据库

    //登陆页面 loginFrame
    public User login(String id,String password) throws YonHuException {
        //在这里写登录的过程
        //1.获得用户输入的账号和密码
        //2.在模拟的数据库中(users)查找有无对应的User对象
        Map<String, User> users = entityContext.getUsers();
//        User user = entityContext.findUserById(Integer.parseInt(id));
        User user = users.get(id);
        if(user!=null){
            //判断密码
            //提示信息
            if(password.equals(user.getPassword())){
                return user;
            }
        }
        throw new YonHuException("编号或密码错误");
        //3.如果有user,密码正确,登陆成功,界面跳转
        //4.如果有user,密码不正确,提示错误信息,界面不跳转
        //5.没有user，提示错误信息
    }
    //登陆页面 loginFrame CanCel
    public void canCelB(){

    }

    //主菜单界面MenuFrame 开始考试
    public ExamInfo startExam(User user) throws IOException {
        ExamInfo info=new ExamInfo();
        Config con=new Config("config.properties");
        info.setQuestionCount(con.getInt("QuestionNumber"));
        info.setTimeLimit(con.getInt("TimeLimit"));
        info.setTitle(new String(con.getString("PaperTitle").getBytes("iso8859-1"),"utf-8"));
        info.setUser(user);
        /*Properties pro=new Properties();
        InputStream in =ExamService.class.getClassLoader().getResourceAsStream("config.properties");
        pro.load(in);
        info.setQuestionCount(Integer.parseInt(pro.getProperty("QuestionNumber")));
        info.setTimeLimit(Integer.parseInt(pro.getProperty("TimeLimit")));
        info.setTitle(pro.getProperty("PaperTitle"));*/
//        info.setTimeLimit(文件中读取的);
//        info.setQuestionCount(文件中读取的);
//        info.setTitle(文件中读取的);

        //1.界面 菜单界面-隐藏,考试界面-显示
        //2.生成考试信息 ,试卷 ->list<Question>
        //examInfo -> 业务模块生成
        //试卷中的一道题目
        createExam();
        return info;
    }

    private List<QuestionInfo> paper=new ArrayList<>();
    private void createExam() {
        int index=0;
        for (int i = Question.LEVEL1; i <= Question.LEVEL10 ; i++) {
            List<Question> qlist = entityContext.findQutionLevel(i);
            /*for (int j = 0; j < 2; j++) {
                Question qqq=qlist.remove((int)(Math.random()*qlist.size()));
                if(paper.get(2*(i-1)+j).equals(qqq)){
                    j--;
                }else{
                    paper.add(new QuestionInfo((i-1)*2+j,qqq));
                }
            }*/
            Question q1 = qlist.remove((int)(Math.random()*qlist.size()));
            Question q2 = qlist.remove((int)(Math.random()*qlist.size()));
            paper.add(new QuestionInfo(index++, q1));
            paper.add(new QuestionInfo(index++, q2));
        }
    }

    //主菜单界面MenuFrame 分数
    public void myScore(){

    }
    //主菜单MenuFrame 考试规则
    public void showRules(){

    }
    //主菜单界面MenuFrame 离开
    public void Exit(){

    }

    //考试界面ExamFrame  交卷
    public void endExam(){

    }


    public void next_question(){

        //记录用户的题目答案
    }
    //考试界面ExamFrame  上一题
    public void previous_question(){

    }




    public void setEntityContext(EntityContext entityContext) {
        this.entityContext = entityContext;
    }

    public QuestionInfo getQuestionFormPaper(int questionIndex) {
        return paper.get(questionIndex);
    }

    public void updateTime(int timeLimit, ExamFrame ex) throws InterruptedException {
        Long time=(long)timeLimit*60;
        Callable aaa=new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    Long t=time;
                    while(true) {
                        ex.updateTime(t/3600,t%3600/60,t%60);
                        t--;
                        Thread.sleep(1000);
                        if(t<0){
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return time;
            }
        };

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Long t=time;
                    while(true) {
                        ex.updateTime(t/3600,t%3600/60,t%60);
                        t--;
                        Thread.sleep(1000);
                        if(t<0){
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
}
