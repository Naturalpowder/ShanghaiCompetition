package test;

/**
 * @program: ShanghaiCompetition
 * @description:
 * @author: Naturalpowder
 * @create: 2020-10-22 17:11
 **/
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;




/**
 * 渐变色测试
 * @author HUANGGR
 *
 */
public class GradientTest extends JFrame implements ActionListener{

    JButton resultBtn;
    JButton startBtn,endBtn;
    GradientTest(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(300,300);






        startBtn = new JButton("start color");
        startBtn.setBounds(20, 20, 100, 30);
        startBtn.setEnabled(false);
        this.add(startBtn);


        endBtn = new JButton("end color");
        endBtn.setBounds(140, 20, 100, 30);
        endBtn.setEnabled(false);
        this.add(endBtn);

        resultBtn = new JButton("result");
        resultBtn.setBounds(80, 70, 100, 30);
        this.add(resultBtn);
        resultBtn.addActionListener(this);

        this.setVisible(true);
    }



    class ColorRunThread implements Runnable{


        @Override
        public void run() {
            int oldR = 10 ,oldG =10,oldB =10;
            int newR = 100 ,newG =200, newB =75;
            Color oldColor = new Color(oldR,oldG,oldB);  //初始颜色
            Color newColor = new Color(newR,newG,newB);  //结束颜色

            startBtn.setBackground(oldColor);
            endBtn.setBackground(newColor);
            int step = 20;  //分多少步完成
            for(int i = 1 ; i <= step; i++){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
                int r = oldColor.getRed() + (newColor.getRed() - oldColor.getRed())*i/step;
                int g = oldColor.getGreen() + (newColor.getGreen() - oldColor.getGreen())*i/step;
                int b = oldColor.getBlue() + (newColor.getBlue() - oldColor.getBlue())*i/step;
                Color tempColor = new Color(r,g,b);
                resultBtn.setBackground(tempColor);
            }



        }

    }

    public static void main(String[] args) {

        new GradientTest();
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        Thread t = new Thread(new ColorRunThread());
        t.start();
    }


}
