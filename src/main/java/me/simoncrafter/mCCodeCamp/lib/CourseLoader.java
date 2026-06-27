package me.simoncrafter.mCCodeCamp.lib;

import me.simoncrafter.mCCodeCamp.courseBackEnd.basics.example1.BackendExample1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseLoader {

    private static Map<String, ICourseExample> courseExamples = new HashMap<>();

    public static void loadExamples() {
        loadBasics();

        for (ICourseExample example : courseExamples.values()) {
            example.onLoad();
        }
    }
    private static void loadBasics() {
        BackendExample1 example1 = new BackendExample1();
        courseExamples.put(example1.getName(), example1);
    }

}
