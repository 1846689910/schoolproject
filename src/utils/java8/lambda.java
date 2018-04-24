package utils.java8;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class lambda {
    public static void main(String[] args) {
//        lambda expression 小记:
//        1 lambda表达式实现Runnable接口(lambda表达式可以用于实现函数式接口，也就是只有一个方法需要重写的接口):
// Java 8之前：
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Before Java8, too much code for too little to do");
            }
        }).start();
//Java 8方式：
        new Thread( () -> System.out.println("In Java8, Lambda expression rocks !!") ).start();

//        2 使用lambda表达式进行事件处理
// Java 8之前：
        JButton show =  new JButton("Show");
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Event handling without lambda expression is boring");
            }
        });
// Java 8方式：
        show.addActionListener((e) -> {
            System.out.println("Light, Camera, Action !! Lambda expressions Rocks");
        });

//        通常像Collections.sort()或者Arrays.sort(), PriorityQueue中的comparator都可以用lambda表达式来代替实现

//        3 可迭代类可以使用lambda表达式的forEach来迭代
// Java 8之前：
        List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
        for (String feature : features) {
            System.out.println(feature);
        }
// Java 8之后：
        List<String> features1 = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
        features1.forEach(n -> System.out.println(n));

// 使用Java 8的方法引用更方便，方法引用由::双冒号操作符标示，
// 看起来像C++的作用域解析运算符
        features1.forEach(System.out::println);

    }
}
