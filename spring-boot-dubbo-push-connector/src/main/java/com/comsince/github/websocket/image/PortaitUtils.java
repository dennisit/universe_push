package com.comsince.github.websocket.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author comsicne
 * Copyright (c) [2019]
 * @Time 20-4-16 下午5:06
 **/
public class PortaitUtils {
    /**
     * 图片格式：JPG
     */
    private static final String PICTRUE_FORMATE = "jpg";

    /**
     * 生成组合头像
     * 画布宽度和高度为166，图片间距为2
     *
     * @param paths   用户头像路径列表
     * @param outPath 生成后的头像保存路径
     * @throws IOException
     */
    public static void generate(List<String> paths, String outPath) throws IOException {
        generate(paths, 166, 2, outPath);
    }

    /**
     * 生成组合头像
     *
     * @param paths    用户头像路径列表
     * @param length   画板的宽高和高度
     * @param interval 画板中的图片间距
     * @param outPath  生成后的头像保存路径
     * @throws IOException
     */
    public static void generate(List<String> paths, int length, int interval, String outPath) throws IOException {
        int wh = (length - interval * 4) / 3; // 每个图片的宽高和高度：图片数>4
        if (paths.size() == 1) {
            wh = length - interval * 2; // 每个图片的宽高和高度：图片数=1
        }
        if (paths.size() > 1 && paths.size() < 5) {
            wh = (length - interval * 3) / 2; // 每个图片的宽高和高度：图片数>0并且<5
        }
        List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
        for (int i = 0; i < paths.size(); i++) {
            bufferedImages.add(resize(paths.get(i), wh, wh, false));
        }
        // BufferedImage.TYPE_INT_RGB可以自己定义可查看API
        BufferedImage outImage = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
        // 生成画布
        Graphics g = outImage.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        // 设置背景色
        g2d.setBackground(new Color(255, 255, 255));
        // 通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
        g2d.clearRect(0, 0, length, length);
        // 开始拼凑 根据图片的数量判断该生成那种样式的组合头像
        for (int i = 1; i <= bufferedImages.size(); i++) {
            int j = i % 3 + 1;
            if (bufferedImages.size() < 5) {
                j = i % 2 + 1;
            }
            int x = interval * j + wh * (j - 1);
            int split = (wh + interval) / 2;
            if (bufferedImages.size() == 9) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3, null);
                } else if (i <= 6) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x, interval, null);
                }
            } else if (bufferedImages.size() == 8) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3, null);
                } else if (i <= 6) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x - split, interval, null);
                }
            } else if (bufferedImages.size() == 7) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3, null);
                } else if (i <= 6) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), (length - wh) / 2, interval, null);
                }
            } else if (bufferedImages.size() == 6) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3 - split, null);
                } else if (i <= 6) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2 - split, null);
                }
            } else if (bufferedImages.size() == 5) {
                if (i <= 3) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh * 2 + interval * 3 - split, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x - split, wh + interval * 2 - split, null);
                }
            } else if (bufferedImages.size() == 4) {
                if (i <= 2) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x, interval, null);
                }
            } else if (bufferedImages.size() == 3) {
                if (i <= 2) {
                    g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2, null);
                } else {
                    g2d.drawImage(bufferedImages.get(i - 1), x - split, interval, null);
                }
            } else if (bufferedImages.size() == 2) {
                g2d.drawImage(bufferedImages.get(i - 1), x, wh + interval * 2 - split, null);
            } else if (bufferedImages.size() == 1) {
                g2d.drawImage(bufferedImages.get(i - 1), interval, interval, null);
            }
            // 需要改变颜色的话在这里绘上颜色。可能会用到AlphaComposite类
        }
        ImageIO.write(outImage, PICTRUE_FORMATE, new File(outPath));
    }

    public static BufferedImage resize(String filePath, int width, int height) {
        try {
            BufferedImage bi = ImageIO.read(new File(filePath));
            bi.getSubimage(0, 0, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片缩放
     *
     * @param filePath 图片路径
     * @param height   高度
     * @param width    宽度
     * @param bb       比例不对时是否需要补白
     */
    public static BufferedImage resize(String filePath, int height, int width, boolean bb) {
        try {
            double ratio = 0; // 缩放比例
            File f = new File(filePath);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null)) {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                } else {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                }
                g.dispose();
                itemp = image;
            }
            return (BufferedImage) itemp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[]  args){
        String[] imageUrls = new String[9];
        imageUrls[0] = "http://image.comsince.cn/1-qJqkqkBB-1586859367172-152c33b18a346dbc7f4b9d95511e1c47.jpg";
        imageUrls[1] = "http://image.comsince.cn/1-373z3zNN-1586769371016-1f54b568b4e9d2444cef785f21dbf6de.jpeg";
        imageUrls[2] = "http://image.comsince.cn/1-MRM5M5UU-1586769718913-4496d7098c1f9729ec7dcc8222276c0f.jpeg";
        imageUrls[3] = "http://image.comsince.cn/1-8C8181CC-1586779409545-7b04ff294513d5d045c8d9c6f2ae1b55.jpeg";
        imageUrls[4] = "http://image.comsince.cn/1-8C8181CC-1586779409545-7b04ff294513d5d045c8d9c6f2ae1b55.jpeg";
        imageUrls[5] = "http://image.comsince.cn/1-t0tptp00-1586779480758-53181fc0e8d6a9976f52996dd2957484.jpg";
        imageUrls[6] = "http://image.comsince.cn/1-VdVMVMss-1586779593209-14c9b43d92d6df7cd3e3fd26f30c4453.jpg";
        imageUrls[7] = "http://b-ssl.duitang.com/uploads/item/201808/03/20180803090324_qrygh.thumb.700_0.jpeg";
        imageUrls[8] = "http://b-ssl.duitang.com/uploads/item/201803/19/20180319132911_UxCLe.thumb.700_0.jpeg";

        CountDownLatch countDownLatch = new CountDownLatch(imageUrls.length);
        List<String> portraitList = new ArrayList<>();
        for(String url : imageUrls){
            DownloadManager.get().download(url, "/data/boot/download",  new DownloadManager.OnDownloadListener() {
                @Override
                public void onSuccess(File file) {
                    portraitList.add(file.getAbsolutePath());
                    countDownLatch.countDown();
                }

                @Override
                public void onProgress(int progress) {

                }

                @Override
                public void onFail() {

                }
            });
        }

        try {
            countDownLatch.await();
            System.out.println("portrait list "+portraitList);
            generate(portraitList,"/data/boot/one.jpg");
            System.out.println("generate success");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
