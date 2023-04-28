package com.yo.fileservice.components;

import com.yo.fileservice.service.FileService;
import com.yo.fileservice.service.GroupFileService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "delete.groupfile.process.queue")
public class GroupMessageReceiver {

    @Autowired
    private GroupFileService fileService;

    @RabbitHandler()
    public void deleteFile(String msg) throws Exception {
        String[] params = msg.split(":");
        fileService.deleteFileInMinio(params[0],params[1],params[2]);
    }
}
