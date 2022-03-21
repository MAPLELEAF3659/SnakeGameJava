import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

public class snakegame  {
    public static void main(String[] args){
        new snakegame();
    }
    JFrame f = new JFrame("SNAKE GAME") ;
    JButton startbtn = new JButton("新遊戲"), btn[][];
    JLabel label1=new JLabel("長度 : ");JLabel label2=new JLabel("時間 : 00:00:00'0");
    Queue <JButton> body =new LinkedList<>() ,prebody =new LinkedList<>() ;
    public int x,y,fx,fy,d=1,t;String TimerTime;
    Timer tick=new Timer(),time=new Timer();
    public snakegame(){
        Font F1 = new Font("新細明體",1,20);
        f.setBounds(400,400,416,538);          //x,y為視窗顯示位置
        f.setLayout(null);                 //取消自動排版
        btn = new JButton[10][10];        //設定遊戲區域大小(10*10)
        for (int x=0;x<=9;x++){
            for (int y =0;y<=9;y++){
                btn[x][y]=new JButton();
                btn[x][y].setBounds(x*40,y*40+100,40,40);
                btn[x][y].setBackground(Color.white );
                btn[x][y].setEnabled(false);
                f.add(btn[x][y]);
            }
        }
        startbtn.setBounds(20,10,150,80);       //x,y為開始鍵顯示位置
        startbtn.setFont(F1);
        f.add(startbtn);
        startbtn.addActionListener(new ActionListener(){               //開始鍵功能實作於ActionListener
            public void actionPerformed(ActionEvent e)
            {
                if (startbtn.getText() =="新遊戲"){
                    startbtn.setEnabled(false);
                    clear();
                    tick.schedule(new tick() , 0,200);
                    time.schedule(new timer(),0,10);
                }else if (startbtn.getText() =="重新開始"){
                    clear();
                    startbtn.setText("新遊戲") ;startbtn.setEnabled(false);
                    tick=new Timer() ;time =new Timer();
                    tick.schedule(new tick() , 0,200);
                    time.schedule(new timer(),0,10);
                }
                f.requestFocus() ;
            }
        });
        label1.setBounds(200,10,900,40);
        label1.setFont(F1);
        f.add(label1);
        label2.setBounds(200,50,900,40);
        label2.setFont(F1);
        f.add(label2);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.addKeyListener(new KeyAdapter() {                           //按鍵監聽實作於KeyAdapter
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:  //up
                        if (d != 2) d = 1;
                        break;
                    case KeyEvent.VK_S:  //down
                        if (d != 1) d = 2;
                        break;
                    case KeyEvent.VK_A:  //left
                        if (d != 4) d = 3;
                        break;
                    case KeyEvent.VK_D:  //right
                        if (d != 3) d = 4;
                        break;
                }
            }
        }) ;
    }
    public void clear(){      //重置
        body .clear() ;
        prebody .clear() ;
        d=1;t=0;
        for (int x=0;x<=9;x++){
            for (int y =0;y<=9;y++){
                btn[x][y].setBackground(Color.white );
            }
        }
        x=5;y=5;
        for (int i = 4;i>=0;i--){
            body.add(btn[x][y+i]);
        }
        update();
    }
    public void update(){     //更新蛇體
        prebody.addAll(body);
        label1.setText("長度 : "+prebody .size()) ;
        do{
            prebody.poll().setBackground(Color.blue);
        }while(prebody .size() !=1);
        prebody.poll().setBackground(Color.yellow);
    }
    class tick extends TimerTask{     //每200ms偵測蛇的狀況
        public void run(){
            Random ran = new Random();    //用於隨機生成食物
            if (d==1)y-=1;
            else if (d==2)y+=1;
            else if (d==3)x-=1;
            else if (d==4)x+=1;
            if (x<0||x>9||y<0||y>9) {      //偵測是否撞牆
                tick.cancel();time.cancel();
                JOptionPane.showMessageDialog(f,"遊戲結束\n你撞到牆了\n"+TimerTime) ;
                startbtn .setText("重新開始"); startbtn.setEnabled(true);clear();
            } else if (body.size()==99){   //偵測是否已完成遊戲
                tick.cancel();time.cancel();
                JOptionPane.showMessageDialog(f,"遊戲結束\n"+TimerTime) ;
                startbtn .setText("重新開始");startbtn.setEnabled(true);
                clear();
            } else if (btn[x][y].getBackground()==Color.blue ){    //偵測是否撞到自己
                tick.cancel();time.cancel();
                JOptionPane.showMessageDialog(f,"遊戲結束\n你撞到你自己\n"+TimerTime) ;
                startbtn .setText("重新開始");startbtn.setEnabled(true);
                clear();
            } else {     //以上均不成立則往前一格
                body.offer(btn[x][y]);
                body.poll().setBackground(Color.white);
                update();
            }
            if(btn[fx][fy].getBackground() ==Color.WHITE ||btn[fx][fy].getBackground() ==Color.BLUE){  //隨機生成食物
                fx = ran.nextInt(10);
                fy = ran.nextInt(10);
                btn[fx][fy].setBackground(Color.red);
            }else if(btn[fx][fy].getBackground() ==Color.YELLOW  ){    //如果蛇吃到食物則長度+1
                body.offer(btn[x][y]);
            }
        }
    }
    class timer extends TimerTask {    //計時器(最小單位10ms)
        public void run() {
                t++;
                TimerTime="時間 : "+ String.format("%02d",t/144000%86400)  +":"+ String.format("%02d",t / 6000%3600) + ":" + String.format("%02d",t / 100%60) + "'" + t%100;
                label2.setText(TimerTime);
        }
    }
}