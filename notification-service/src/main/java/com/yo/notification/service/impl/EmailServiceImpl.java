package com.yo.notification.service.impl;

import com.yo.notification.service.EmailService;
import com.yo.notification.vo.MailVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class EmailServiceImpl implements EmailService {

    String htmlTemplate;
    String textTemplate;

    @Autowired
    private JavaMailSenderImpl mailSender;

    public EmailServiceImpl(){
        try {
            InputStream htmlInputStream = this.getClass().getResourceAsStream("/MailTemplate.html");
            InputStream textInputStream = this.getClass().getResourceAsStream("/MailTemplate.txt");
            BufferedReader bis = new BufferedReader(new InputStreamReader(htmlInputStream));
            StringBuilder content = new StringBuilder();
            String szTemp;
            while ((szTemp = bis.readLine()) != null) {
                content.append(szTemp);
            }
            bis.close();
            htmlTemplate = content.toString();
            content.delete( 0, content.length() );
            bis = new BufferedReader(new InputStreamReader(textInputStream));
            while ((szTemp = bis.readLine()) != null) {
                content.append(szTemp);
            }
            bis.close();
            textTemplate = content.toString();
        } catch (Exception e) {
            htmlTemplate = textTemplate = "";
        }
    }

    @Async
    @Override
    public MailVo sendMail(MailVo mailVo, String operation, String verifycode){
        try{
            if (StringUtils.isEmpty(mailVo.getText())){
                mailVo.setText(htmlTemplate);
            }
            checkMail(mailVo);
            sendMimeMail(mailVo,operation,verifycode);
            return saveMail(mailVo);
        } catch (Exception e){
            System.out.println("error");
            e.printStackTrace();
            mailVo.setStatus("failed");
            mailVo.setError(e.getMessage());
            return mailVo;
        }
    }

    private void checkMail(MailVo mailVo){
        if (StringUtils.isEmpty(mailVo.getTo())){
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getSubject())){
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailVo.getText())){
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    private void sendMimeMail(MailVo mailVo, String operatinon, String verifycode){
        try{
            htmlTemplate = htmlTemplate.replace("{operation}",operatinon);
            htmlTemplate = htmlTemplate.replace("{verifycode}",verifycode);
            textTemplate = textTemplate.replace("{operation}",operatinon);
            textTemplate = textTemplate.replace("{verifycode}",verifycode);
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            messageHelper.setFrom(mailVo.getFrom(),"YoTech");
            messageHelper.setTo(mailVo.getTo());
            messageHelper.setSubject(mailVo.getSubject());
            //messageHelper.setText(mailVo.getText());
            messageHelper.setText(textTemplate, htmlTemplate);
            if (!StringUtils.isEmpty(mailVo.getCc())){
                messageHelper.setCc(mailVo.getCc().split(","));
            }
            if (!StringUtils.isEmpty(mailVo.getBcc())){
                messageHelper.setCc(mailVo.getBcc().split(","));
            }
            if (mailVo.getMultipartFiles() != null){
                for (MultipartFile multipartFile : mailVo.getMultipartFiles()){
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            mailSender.send(messageHelper.getMimeMessage());
            mailVo.setStatus("OK");
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private MailVo saveMail(MailVo mailVo){
        return mailVo;
    }

    public String getMailSendFrom(){
        return mailSender.getJavaMailProperties().getProperty("from");
    }

}
