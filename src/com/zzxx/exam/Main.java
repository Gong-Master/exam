package com.zzxx.exam;

import com.zzxx.exam.controlle.ClientContext;
import com.zzxx.exam.entity.EntityContext;
import com.zzxx.exam.service.ExamService;
import com.zzxx.exam.ui.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        LoginFrame loginFrame =new LoginFrame();
        ExamFrame examFrame=new ExamFrame();
        MenuFrame menuFrame=new MenuFrame();
        MsgFrame msgFrame=new MsgFrame();
        WelcomeWindow welcomeWindow=new WelcomeWindow();

        ClientContext controller=new ClientContext();
        ExamService service =new ExamService();
        EntityContext entityContext=new EntityContext();

        //控制器和界面之间有依赖，不是谁包含谁
        //注入依赖
        loginFrame.setContext(controller);
        service.setEntityContext(entityContext);
        menuFrame.setContext(controller);
        examFrame.setContext(controller);

        controller.setLoginFrame(loginFrame);
        controller.setExamService(service);
        controller.setMenuFrame(menuFrame);
        controller.setWelcomeWindow(welcomeWindow);
        controller.setMsgFrame(msgFrame);
        controller.setExamFrame(examFrame);

        controller.startShow();
    }
}
