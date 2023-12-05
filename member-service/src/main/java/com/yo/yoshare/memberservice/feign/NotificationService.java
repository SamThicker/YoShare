package com.yo.yoshare.memberservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "NOTIFICATION-SERVICE")
public interface NotificationService {
    @RequestMapping(value = "/mail/send", method = RequestMethod.POST)
    void sendMail(@RequestParam String to, @RequestParam String subject,
                  @RequestParam String content, @RequestParam String operation, @RequestParam String verifycode);
}
