import com.qqlei.zhongjiaxin.inventory.utils.HttpClientUtil;

import java.util.Random;

/**
 * Created by 李雷 on 2017/10/12.
 */
public class Tesss {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=40;i>0;i--){
                    try {
                        Random r1 = new Random();
                        Thread.sleep(r1.nextInt(5000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String url = "http://localhost:8080/updateProductInventory?productId=1&inventoryCnt="+i;
                    System.out.println(HttpClientUtil.sendGet(url));
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=320;i>0;i--){
                    try {
                        Random r1 = new Random();
                        Thread.sleep(r1.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String url = "http://localhost:8080/getProductInventory?productId=1";
                    System.out.println(HttpClientUtil.sendGet(url));
                }
            }
        }).start();
    }
}
