package com.yo.notification.controller;

import com.yo.notification.service.EmailService;
import com.yo.notification.vo.MailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public void sendMail(@RequestParam String to, String subject,
                         String content, String operation, String verifycode){
        MailVo mailVo = new MailVo();
        mailVo.setFrom("zhenyuefeng@aliyun.com");
        mailVo.setTo(to);
        mailVo.setSubject(subject);
        mailVo.setText(content);
        emailService.sendMail(mailVo,operation,verifycode);
    }

}
