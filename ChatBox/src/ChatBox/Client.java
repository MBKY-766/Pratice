package ChatBox;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 3060);
        while (true) {
            //用户选择操作
            System.out.println("==============欢迎来到DIY聊天室==============");
            System.out.println("1.登录");
            System.out.println("2.注册");
            Scanner sc = new Scanner(System.in);
            System.out.print("请输入你的选择：");
            String choice = sc.nextLine();
            //将用户的选择发送给服务端
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writeMessageToServer(bw, choice);
            //socket.shutdownOutput();
            //如果是登录，用户输入用户名和密码
            switch (choice) {
                case "1" -> login(sc, bw, socket);
                case "2" -> register(sc, bw, socket);
                default -> System.out.println("无效选择，请重试");
            }
        }
    }

    private static void register(Scanner sc, BufferedWriter bw, Socket socket) throws IOException {
        while (true) {
            System.out.print("请输入用户名：");
            String username = sc.nextLine();
            System.out.print("请输入密码：");
            String password = sc.nextLine();
            String userInfo = "username=" + username + "&" + "password=" + password;
            //将用户信息传递给服务器
            writeMessageToServer(bw, userInfo);
            //接收服务器的反馈
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String feedBack = br.readLine();
            System.out.println(feedBack);
            if(feedBack.equals("注册成功")){
                //注册成功，退出注册
                break;
            }
        }
    }

    //给客户端发送消息
    private static void writeMessageToServer(BufferedWriter bw, String choice) throws IOException {
        bw.write(choice);
        bw.newLine();
        bw.flush();
    }
    //登录逻辑
    private static void login(Scanner sc, BufferedWriter bw, Socket socket) throws IOException, InterruptedException {
        while (true) {
            System.out.print("请输入用户名：");
            String username = sc.nextLine();
            System.out.print("请输入密码：");
            String password = sc.nextLine();
            String userInfo = "username=" + username + "&" + "password=" + password;
            //将用户信息传递给服务器
            writeMessageToServer(bw, userInfo);
            //socket.shutdownOutput();//不能写出结束标记，后面还要传递数据
            //接收服务器的反馈
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String feedBack = br.readLine();
            System.out.println(feedBack);//√
            if (feedBack.equals("登录成功，开始聊天！")) {
                //进入聊天室
                //接收别人的消息-接收消息和发送消息同时进行，多线程
                //接收消息单独开一个线程，一直在接收消息
                new Thread(new ClientMyRunnable(socket)).start();
                //开始聊天
                talkToAll(sc, bw);
            }
            //登录失败，重新输入
        }
    }
    //聊天室
    private static void talkToAll(Scanner sc, BufferedWriter bw) throws IOException, InterruptedException {
        System.out.println("请输入你要说的话：");
        // 向服务器发送消息
        while (true) {
            //Thread.sleep(50);
            String message = sc.nextLine();
            if (message.equals("exit")) {
                break;
            }
            //将消息传递给服务器
            writeMessageToServer(bw, message);
        }
    }
}
