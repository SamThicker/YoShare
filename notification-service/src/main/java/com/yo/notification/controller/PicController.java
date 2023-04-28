package com.yo.notification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Controller
@RequestMapping("/images")
public class PicController {
    private int width = 100, height = 30;
    private String source = "1234567890";

    @RequestMapping("pic")
    public void showPic(HttpSession session, HttpServletResponse response) throws Exception {
        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffer.getGraphics();
        // 设定背景色
        g.setColor(new Color(197, 219, 223));
        g.fillRect(0, 0, width, height);

        // 画边框
        g.setColor(new Color(5, 87, 255));
        g.drawRect(2, 2, width - 5, height - 5);
        String checkcode = this.generateCheckcode(6);
        session.setAttribute("checkcode", checkcode);
        g.setFont(new Font("宋体", Font.BOLD, 22));
        g.setColor(new Color(60, 165, 200));
        g.drawString(checkcode, 5, height - 7);
        // 随机产生10个干扰点
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            g.drawOval(x, y, 2, 1);
        }
        g.dispose();
        response.setHeader("pragme", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setDateHeader("expires", -1);
        response.setContentType("image/jpeg");
        response.resetBuffer();
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(buffer, "jpg", sos);
        sos.flush();
        sos.close();

    }
    private String generateCheckcode(int len) {
        char[] res = new char[len];
        Random r = new Random();
        for (int i = 0; i < len; i++) {
            res[i] = source.charAt(r.nextInt(source.length()));
        }
        return new String(res);
    }
}



