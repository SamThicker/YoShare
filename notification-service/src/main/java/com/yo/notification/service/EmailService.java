package com.yo.notification.service;

import com.yo.notification.vo.MailVo;
import org.springframework.scheduling.annotation.Async;

@Async
public interface EmailService {
    MailVo sendMail(MailVo mailVo, String operation, String verifycode);
}
