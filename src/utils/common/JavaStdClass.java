package utils.common;

import java.io.Serializable;
/**
    类的内部结构：
 静态代码块/静态成员变量显式赋值 -> 构造代码块/成员变量显式赋值 -> 构造函数
 ----------------------------------------------------------------------------------------------------
 |       |静态/构造代码块 |                                       |                                   |                   |
 |       |----------------------------------------------------------------------------------------- |
 |       |构造函数       |                                       |                                   |                    |
 |       |----------------------------------------------------------------------------------------  |
 |  类   |成员属性       | 成员变量(事物的公共属性/SerialVersionUID) |                                    |
 |       |             |----------------------------------------------------------------------------|
 |       |            | 成员方法(事物的公开行为)                   | 局部代码块                           |
 |       |             |                                       |------------------------------------|
 |       |            |                                       | 局部变量                              |
 |       |             |                                       |------------------------------------|
 |       |            |                                       | 局部内部类(包含匿名内部类,               |
 |       |            |                                       | 需要借用父类/父接口名字,一旦定义就要使用)  |
 |       |------------------------------------------------------------------------------------------|
 |       |成员内部类                                                                                  |                    |
 ----------------------------------------------------------------------------------------------------
    修饰符：
    权限修饰符   本类      本包      子类      其他包
    public     true     true      true      true
    protected  true     true      true      false
    不写        true     true      false     false
    private    true     false     false     false

 * */

public class JavaStdClass implements Serializable {//如果对象需要序列化，就是写入文件，就要实现Serializable接口
    private static final long serialVersionUID = 1L;//指定类的序列号，防止修改成员后系统分配序列号会变化，导致无法读入对象
    String name;//成员变量(不写修饰符)
    int age;//成员变量(不写修饰符)
    static transient int month = 10;//静态成员变量显式赋值, transient表示对象序列化时不要把该项写入文件
    int year = 2016;//成员变量显式赋值
    private int salary;//成员变量(封装起来private修饰)
    {//构造代码块，会放入构造函数内，先执行
        System.out.println("构造代码块执行了");
        year = 2015;
        month = 9;
    }
    static {//静态代码块，
        System.out.println("静态代码块执行了");
        month = 11;
    }
    public JavaStdClass () {}//无参构造方法
    public JavaStdClass (String name, int age) {//构造方法
        this.name = name;
        this.age = age;
        month = 12;
        year = 2017;
    }
    public JavaStdClass (String name, int age, int salary) {//重载构造方法
        this(name, age);//this的重要作用：调用本类的另一个构造方法
        this.salary = salary;
    }
    public void setSalary (int salary) { //公共方法,设置封装的属性
        this.salary = salary;//this调用了成员变量
    }
    public int getSalary () {//公共方法，获取封装的属性
        return this.salary;
    }
    public void run () {
        int seconds = (int) (Math.random() * (600 - 100 + 1));//局部变量，位于方法之内
        String status;//局部变量
        {//局部代码块
            status = "气喘吁吁";
            int a = 1; //出了局部代码块就消失，外部访问不到
        }
        System.out.println(this.name + " 跑了" + seconds + "秒" + status);
    }
    void study () {
        System.out.println(this.name + " is studying");
    }
    public class Heart {//成员内部类
        public void beat () {
            System.out.println(JavaStdClass.this.name + "的心脏在跳动");//从内部类访问外部类的成员变量
        }
    }
    public void purchase () {//成员方法
        final int money = 5000;//局部内部类访问的变量必须用final修饰
        class Wallet {//局部内部类
            public void countMoney () {//局部内部类的方法
                System.out.println(JavaStdClass.this.name + " has " + money + " in wallet");
            }
        }
        new Wallet().countMoney();
        new IntPockets() { //匿名内部类, 借用了接口的名字表示接口的实现类
            public IntPockets countMoney() {
                System.out.println(money);//1000, 就近原则，访问的是接口的money
                return this;
            }
            public void yell () {
                System.out.println("快没钱啦");
            }
        }.countMoney().yell();
        IntPockets pockets = new IntPockets() {//匿名内部类,左边是接口的变量名=右边接口实现类借用接口名字;多态给子类命名
            public IntPockets countMoney() {
                System.out.println(money);//1000, 就近原则，访问的是接口的money
                return this;
            }
            public void yell () {
                System.out.println("快没钱啦");
            }
        };
        pockets.countMoney();
        pockets.yell();
    }
}
class Son extends JavaStdClass {
    String school;
    public static final double PI = 3.1415;//final修饰的为最终的，不能重写，不能继承
    public Son(String name, int age) {//因为父类已经有了name和age的声明，子类就不要再声明了，可以直接继承
        super(name, age);
    }
    public Son(String name, int age, String school) {//构造函数重载
        super(name, age);//super位于构造函数第一句. 父类有的构造方法部分直接拿来用
        this.school = school;//父类没有的，自己写定义
    }
    public void run () {
        super.run();//调用父类的方法
    }
    protected void study () {//重写父类方法，父类study是不写修饰符，那么protected权限更大
        //父类没报错，子类要更优秀更不能报错；子类返回值类型也要<=父类
        System.out.println(this.name + " is working harder");
    }
}
interface IntPockets {
    public static final int money = 1000;
    public abstract IntPockets countMoney ();
    public abstract void yell ();
}
