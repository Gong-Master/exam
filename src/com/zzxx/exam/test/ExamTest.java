package com.zzxx.exam.test;

import com.zzxx.exam.entity.EntityContext;
import org.junit.Test;

import java.io.IOException;

public class ExamTest {
    @Test
    public void Test01() throws IOException {
        EntityContext enn=new EntityContext();
        System.out.println(enn.getUsers());
        System.out.println(enn.getQuestions());
    }
}
