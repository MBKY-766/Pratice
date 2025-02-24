package com.itheima.ui;

import cn.hutool.core.io.IoUtil;
import com.itheima.domain.Daily;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class AddJFrame extends JFrame implements ActionListener {

    //定义标题输入框
    JTextField titleText  = new JTextField();

    //定义内容的输入区域
    JTextArea contentText = new JTextArea();

    //定义保存按钮
    JButton save = new JButton("保存");

    //定义取消按钮
    JButton cancel = new JButton("取消");

    public AddJFrame(){
        //初始化界面
        initFrame();

        //初始化组件
         initView();

        //让界面展示出来
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj == save){
            System.out.println("保存按钮被点击了");
            try {
                //创建日记存储文档的文件夹对象
                File file = new File("diary\\data");
                //得到文件夹下所有的日记存储文档的数组
                File[] files = file.listFiles();
                //如果文件夹下没有日记文件，说明添加的是第一个日记文件
                if(files.length == 0){
                    //与日记文档建立连接管道
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("diary\\data\\data"+1+".txt"));
                    //将输入的标题，内容封装成一个对象写入到日记文档中
                    Daily daily = new Daily(1,titleText.getText(),contentText.getText());
                    //利用糊涂包将序列化的日记对象写入到本地文件当中
                    //序列化：将 Java 对象转换为字节序列的过程
                    //反序列化：将字节序列还原为 Java 对象的过程
                    IoUtil.writeObjects(oos,true,daily);
                    //关闭流
                    oos.close();
                }else{
                    //不是第一个日记文件，获取最大ID
                    //将最后一个日记文件的路径转换成字符串
                    String str = files[files.length-1]+"";//diary\data\data1.txt
                    //截取其序号在增1
                    int ID = str.charAt(15)-'0'+1;
                    //重复
                    //与日记文档建立连接管道(序列化流)
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("diary\\data\\data"+ID+".txt"));
                    //将输入的标题，内容封装成一个对象写入到日记文档中
                    Daily daily = new Daily(ID,titleText.getText(),contentText.getText());
                    //利用糊涂包将序列化的日记对象写入到本地文件当中
                    IoUtil.writeObjects(oos,true,daily);
                    //关闭流
                    oos.close();
                }
                //关闭当前页面
                this.setVisible(false);
                //刷新主界面
                new NoteJFrame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }else if(obj == cancel){
            this.setVisible(false);
            new NoteJFrame();
        }
    }

    private void initView() {
        //定义最上面的每日一记
        JLabel title = new JLabel("每日一记");
        title.setBounds(220, 20, 584, 50);
        title.setFont(new Font("宋体", Font.BOLD, 32));
        this.getContentPane().add(title);

        //定义文字：标题
        JLabel subject = new JLabel("标题");
        subject.setBounds(70,90,100,30);
        subject.setFont(new Font("宋体",Font.PLAIN,16));
        this.getContentPane().add(subject);

        //定义文字：内容
        JLabel content = new JLabel("内容");
        content.setBounds(70,140,100,30);
        content.setFont(new Font("宋体",Font.PLAIN,16));
        this.getContentPane().add(content);


        //设置标题的输入框
        titleText.setBounds(120,90,426,30);
        titleText.setFont(new Font("宋体",Font.PLAIN,16));
        this.getContentPane().add(titleText);

        //设置内容的输入框
        contentText.setBounds(120,140,426,300);
        contentText.setFont(new Font("宋体",Font.PLAIN,16));
        this.getContentPane().add(contentText);

        //设置保存按钮
        save.setBounds(132,466,140,40);
        save.setFont(new Font("宋体",Font.PLAIN,24));
        save.addActionListener(this);
        this.getContentPane().add(save);

        //设置取消按钮
        cancel.setBounds(312,466,140,40);
        cancel.setFont(new Font("宋体",Font.PLAIN,24));
        cancel.addActionListener(this);
        this.getContentPane().add(cancel);

    }


    //对添加界面的一些设置
    private void initFrame() {
        //设置界面的宽高
        this.setSize(600, 600);
        //设置界面的标题
        this.setTitle("每日一记（添加日记）");
        //设置界面置顶
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //取消默认的居中放置，只有取消了才会按照XY轴的形式添加组件
        this.setLayout(null);
        //设置背景颜色
        this.getContentPane().setBackground(new Color(212,212,212));
    }


}
