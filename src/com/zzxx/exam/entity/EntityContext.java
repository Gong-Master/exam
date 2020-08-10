package com.zzxx.exam.entity;

import com.zzxx.exam.util.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * 实体数据管理, 用来读取数据文件放到内存集合当中
 */
public class EntityContext {
    //key - 用户的编号id,value - 用户对象
    private Map<String,User> users=new HashMap<>();

    //key - 试题库难度 , value - 难度级别对应的所有试题.
    private Map<Integer, List<Question>> questions = new HashMap<>();

    public EntityContext() throws IOException {
        loadUsers("src/com/zzxx/exam/util/user.txt");
        loadQuestions("src/com/zzxx/exam/util/corejava.txt");
    }

    /*
    * 读取user.txt文件,将其中的数据,封装为用户对象,然后存储到集合中
    * */
    public void loadUsers(String filename) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader(filename));
        String line=null;
        String[] strs;
        User us;
        while((line=br.readLine())!=null){
            if(line.startsWith("#") || line.equals("")){
                continue;
            }
            strs=line.split("[:]+");
            us=new User(strs[1],strs[0],strs[2]);
            us.setEmail(strs[4]);
            us.setPhone(strs[3]);
            users.put(us.getId(),us);
        }
    }

    /*
    * 读取corejava.txt文件,将其中的数据封装为Question对象，存储到集合中
    * */
    public void loadQuestions(String filename) throws IOException{
        BufferedReader br=new BufferedReader(new FileReader(filename));
        String[] strs;
        List<Question> li = null;
        List<String> sl = null;
        List<Integer> intl;
        Question qu = null;
        int count=1;
        String line;
        while((line=br.readLine())!=null){
            if(count%6==1) {
                li = new ArrayList<>();
                sl = new ArrayList<>();
                intl = new ArrayList<>();
                qu = new Question();
                qu.setId(count/6);   //题目的Id序号
                strs = line.split("[,=]+");      // 分解答案，分数，难度 @answer , 2/3, score , 5,level , 5
                String[] answer=strs[1].split("[/]+");
                if(answer.length>1){
                    qu.setType(Question.MULTI_SELECTION);
                }else{
                    qu.setType(Question.SINGLE_SELECTION);
                }
                for(String ans: answer){
                    intl.add(Integer.parseInt(ans));
                }
                qu.setAnswers(intl);
                qu.setLevel(Integer.parseInt(strs[5]));
                qu.setScore(Integer.parseInt(strs[3]));
                li.add(qu);
                qu.setOptions(sl);
                if(questions.get(qu.getLevel())==null) {    //判断该难度是否已被创建,未创建则新建难度 键值,已存在则对应键的list值中添加新的Question对象.
                    questions.put(qu.getLevel(), li);
                }
                else{
                    questions.get(qu.getLevel()).add(qu);
                }
            }else if(count%6==2) {
                qu.setTitle(line);
            }
            else {
                sl.add(line);
            }
            count++;
        }
    }


    //此处模拟的是数据库     但此处直接从数据库中得到了所有信息
    public Map<String, User> getUsers() {
        return users;
    }
    //此处模拟的是数据库     但此处直接从数据库中得到了所有信息
    public Map<Integer, List<Question>> getQuestions() {
        return questions;
    }

    //此处模拟从数据库中获得对应信息
    public User findUserById(Integer id){
        return users.get(id);
    }

    public List<Question> findQutionLevel(Integer level){
        return questions.get(level);
    }
}