package ChatBox;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class MyRunnable implements Runnable {

    //与客户的的连接
    Socket socket;
    //存储用户正确信息
    //用Properties集合存储用户正确信息
    Properties prop;

    //初始化socket
    public MyRunnable(Socket socket, Properties prop) {
        this.socket = socket;
        this.prop = prop;
    }

    @Override
    public void run() {
        //判断当前用户选择的操作：1-登录 2-注册
        //读取客户端发来的消息
        try {
            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String choice = br.readLine();
                System.out.println(choice);
                switch (choice) {
                    case "1" -> login(br);
                    case "2" -> register(br);
                    //default -> "错误提示"
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void register(BufferedReader br) throws IOException {
        while (true) {
            //获取用户输入的用户信息
            String userInfo = br.readLine();//username=zhangsan&password=123456
            String username = userInfo.split("&")[0].split("=")[1];
            String password = userInfo.split("&")[1].split("=")[1];
            //与客户端连接输出流，向客户端反馈消息
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            if(prop.containsKey(username)){
                //用户名重复
                writeMessageToClient(bw, "用户名已存在，请重试");
            }else{
                //用户名不存在，可以注册
                //判断用户名，密码是否符合规则
                String userNameRegex = "[a-zA-Z0-9]{3,16}";
                String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{3,16}$";
                if(username.matches(userNameRegex) && password.matches(passwordRegex)){
                    //用户名和密码合法
                    //将用户信息添加到集合当中
                    prop.put(username, password);
                    //创建输出流关联本地文件
                    FileOutputStream fos = new FileOutputStream("ChatBox\\ServerDir\\UserInfo.txt");
                    prop.store(fos,"");
                    fos.close();
                    writeMessageToClient(bw, "注册成功");
                    //注册成功跳出循环
                    break;
                }else if(username.matches(userNameRegex)){
                    //密码非法
                    writeMessageToClient(bw, "密码不符合要求，密码长度3~16位，由数字和字母组成，且必须同时包含数字和字母");
                    //重新输入
                }else{
                    //用户名非法,或用户名和密码都非法
                    writeMessageToClient(bw, "用户名不符合要求，只能为6~18位的纯字母");
                    //重新输入
                }

            }
        }
    }
    private void login(BufferedReader br) throws IOException {
        while (true) {
            //登录
            //测试消息发送
            //获取用户输入的用户信息
            String userInfo = br.readLine();//username=zhangsan&password=123456
            String username = userInfo.split("&")[0].split("=")[1];
            String password = userInfo.split("&")[1].split("=")[1];
            //System.out.println(userInfo);//√

            //与客户端连接输出流，向客户端反馈消息
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //在prop集合中查找是否存在该的用户信息
            if (prop.containsKey(username)) {
                //用户名存在，判断密码是否正确
                String rightPassword = prop.get(username).toString();
                if (rightPassword.equals(password)) {
                    //用户名和密码正确
                    //反馈登录成功
                    //System.out.println("登录成功");√
                    writeMessageToClient(bw, "登录成功，开始聊天！");
                    talkToAll(br, username);
                } else {
                    //密码错误
                    //反馈密码错误
                    //System.out.println("密码错误");//√
                    writeMessageToClient(bw, "密码输入错误~");
                }
            } else {
                //用户不存在
                writeMessageToClient(bw, "用户名不存在！");
            }
        }
    }

    //聊天室
    private void talkToAll(BufferedReader br, String username) throws IOException {
        //进入聊天室
        while (true) {
            //接收客户端发来的消息
            String message = br.readLine();
            //将message转发给所有用户
            System.out.println(username + "发来消息：" + message);
            for (Socket socket : Server.list) {
                if (this.socket != socket) {
                    //不给该客户发送自己的消息
                    BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    //bw2.newLine();
                    bw2.write(username + "发来消息：" + message);
                    bw2.newLine();
                    bw2.flush();
                }
            }
        }
    }

    //反馈客户消息
    private static void writeMessageToClient(BufferedWriter bw, String message) throws IOException {
        bw.write(message);//√
        bw.newLine();
        bw.flush();
    }
}
