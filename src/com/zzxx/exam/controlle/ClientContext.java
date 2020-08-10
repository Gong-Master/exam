package com.zzxx.exam.controlle;

import com.zzxx.exam.entity.ExamInfo;
import com.zzxx.exam.entity.QuestionInfo;
import com.zzxx.exam.entity.User;
import com.zzxx.exam.service.ExamService;
import com.zzxx.exam.service.YonHuException;
import com.zzxx.exam.ui.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Thread.sleep;

/*
* 客户端控制器：进行界面和业务模型之间的数据传递/交互
* */

public class ClientContext {
    private LoginFrame loginFrame;
    private MenuFrame menuFrame;
    private WelcomeWindow welcomeWindow;
    private ExamFrame examFrame;
    private MsgFrame msgFrame;
    private ExamService examService;

    public void startShow() throws InterruptedException {
        welcomeWindow.setVisible(true);
        Thread.sleep(3000);
        welcomeWindow.setVisible(false);
        loginFrame.setVisible(true);
    }

    private User user;
    public void login(){
        //loginFrame 中获得 账号输入框 和 密码输入框的内容
        try {
            user=examService.login(loginFrame.getIdField().getText(),loginFrame.getPwdField().getText());
            //界面跳转
            loginFrame.setVisible(false);
//            menuFrame.updateView(user);
            menuFrame.getInfo().setText(user.getName()+" 同学您好!");
            menuFrame.setVisible(true);
        } catch (YonHuException e) {
//            e.printStackTrace();  //打印栈信息
            //更新提示信息
            loginFrame.updateMessage(e.getMessage());
        }
    }
    private ExamInfo info;
    public void start() throws IOException, InterruptedException {
        // 1.界面 菜单界面-隐藏, 考试界面-显示
        // 2.生成考试信息, 以及试卷->List<Question>
        // ExamInfo -> 业务模块生成的
        // 试卷中的一道题目 -> 第一题
        // 访问业务层开始考试
        info=examService.startExam(user);
        // 取得第一道题, 用于显示考题
        currentQuestionInfo=new QuestionInfo[info.getQuestionCount()];
        currentQuestionInfo[0]=examService.getQuestionFormPaper(0);
        // 3.更新考试界面get
        examFrame.updateView(info, currentQuestionInfo[0]);
        // 关闭菜单界面
        menuFrame.setVisible(false);
        // 打开考试界面
        examFrame.setVisible(true);
    }

    public void daoJiShi() throws InterruptedException {
        examService.updateTime(info.getTimeLimit(),examFrame);  //考试倒计时,计时器
        currentQuestionInfo[questionIndex].setUserAnswers(examFrame.getSelects());
        examFrame.setVisible(false);
        menuFrame.setVisible(true);
        for (int i = 0; i < questionIndex+1; i++) {
            System.out.println((i+1)+" "+currentQuestionInfo[i].getUserAnswers());
        }
    }
    // 记录正在作答的题目信息
    private QuestionInfo[] currentQuestionInfo;
    private int questionIndex = 0; // -- 可以使用currentQuestionInfo.getQuestionIndex()取代

    public void next() {
//        examService.next_question();
        if(questionIndex<info.getQuestionCount()-1) {
            currentQuestionInfo[questionIndex].setUserAnswers(examFrame.getSelects());   //保存原题目用户选择
            questionIndex++;
            currentQuestionInfo[questionIndex] = examService.getQuestionFormPaper(questionIndex);    //更新题目
            examFrame.updateView(info, currentQuestionInfo[questionIndex]);
            examFrame.clearSelects(currentQuestionInfo[questionIndex]);
        }
        // 1.更新界面
        // 2.记录当前这道题的用户答案
    }

    public void prev() {
//        examService.previous_question();
        if(questionIndex>0){
            currentQuestionInfo[questionIndex].setUserAnswers(examFrame.getSelects());    //保存原题目用户选择
            questionIndex--;
            currentQuestionInfo[questionIndex] = examService.getQuestionFormPaper(questionIndex);   //更新题目
            examFrame.updateView(info, currentQuestionInfo[questionIndex]);
            examFrame.clearSelects(currentQuestionInfo[questionIndex]);
        }

        // 1.更新界面
        // 2.记录当前这道题的用户答案
    }

    public void send() {
        currentQuestionInfo[questionIndex].setUserAnswers(examFrame.getSelects());
        examFrame.setVisible(false);
        menuFrame.setVisible(true);
        for (int i = 0; i < questionIndex+1; i++) {
            System.out.println((i+1)+" "+currentQuestionInfo[i].getUserAnswers());
        }
//        System.out.println(Arrays.toString(currentQuestionInfo));
    }

    public void showRule() throws IOException {
        msgFrame.setVisible(true);
        BufferedReader br=new BufferedReader(new FileReader("src/com/zzxx/exam/util/rule.txt"));
        String str=null;
        String str1="";
        while((str=br.readLine())!=null){
            str1+=str+'\n';
        }
        msgFrame.showMsg(str1);
    }

    public void setLoginFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

    public void setMenuFrame(MenuFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    public void setWelcomeWindow(WelcomeWindow welcomeWindow) {
        this.welcomeWindow = welcomeWindow;
    }

    public void setExamFrame(ExamFrame examFrame) {
        this.examFrame = examFrame;
    }

    public void setMsgFrame(MsgFrame msgFrame) {
        this.msgFrame = msgFrame;
    }

    public void setExamService(ExamService examService) {
        this.examService = examService;
    }



}
