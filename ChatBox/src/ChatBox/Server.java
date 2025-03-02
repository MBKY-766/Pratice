package ChatBox;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    static ArrayList<Socket> list = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(3060);

        //创建线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                3,//核心线程数量，不能小于0
                6,//最大线程数，不能小于0，最大数量>=核心线程数量
                60,//空闲线程最大存活时间
                TimeUnit.SECONDS,//时间单位
                new ArrayBlockingQueue<>(2),//任务队列
                Executors.defaultThreadFactory(),//创建线程工厂
                new ThreadPoolExecutor.AbortPolicy()//任务的拒绝策略(内部类-依赖于外部类，单独存在没有意义，但又是个单独的个体)
        );

        //读取本地文件中所有的正确用户信息
        //创建Properties（Map）集合存储用户信息
        Properties prop = new Properties();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("ChatBox\\ServerDir\\UserInfo.txt"));
        //读取本地用户信息
        prop.load(bis);
        //System.out.println(prop);//{lisi=123456, zhangsan=123}
        while (true) {
            //监听客户端连接，每个客户端开一个线程，同时聊天
            Socket socket = ss.accept();
            //将该客户添加到客户集合当中
            list.add(socket);
            //开启线程，并把用户信息和与客户端的连接传过去
            pool.submit(new MyRunnable(socket, prop));
        }

    }
}
