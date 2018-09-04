package org.luncert;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OutputFile {

    private void ergodic(String path) {
        File base = new File(path);
        if (!base.exists())
            throw new IllegalArgumentException();
        if (base.isDirectory())
            ergodic(base, 0);
        else
            System.out.println(base.getName());
    }

    public void ergodic(File base, int depth) {
        for (int i = 0; i < depth; i++)
            System.out.print(' ');
        System.out.println(base.getName());
        if (base.isDirectory()) {
            for (File file : base.listFiles())
                ergodic(file, depth + 2);
        }
    }

    @Test
    public void test() {
        ergodic("/home/luncert/Platform/clojure");
    }

}