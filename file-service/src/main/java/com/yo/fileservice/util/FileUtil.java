package com.yo.fileservice.util;

import com.yo.fileservice.vo.VOFileResourceInfo;
import com.yo.fileservice.vo.VOFileTransInfo;
import com.yo.yoshare.mbg.mapper.CmsFileTransInfoMapper;
import com.yo.yoshare.mbg.model.CmsFileTransInfo;
import com.yo.yoshare.mbg.model.CmsFileTransInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

@Component
public class FileUtil {

    @Value("${file.rootDir.partFile}")
    String PART_FILE_DIR = "/mydata/yoshare/partFile/";
    @Autowired
    private CmsFileTransInfoMapper fileTransInfoMapper;

    public CmsFileTransInfo getTransInfo(VOFileTransInfo info){
        CmsFileTransInfo realInfo;
        CmsFileTransInfoExample example = new CmsFileTransInfoExample();
        example.createCriteria().andMd5EqualTo(info.getMd5()).andFinishEqualTo("0");
        List<CmsFileTransInfo> realInfos = fileTransInfoMapper.selectByExample(example);
        if (realInfos == null || realInfos.size()<=0){
            realInfo = new CmsFileTransInfo();
            realInfo.setCursor(0);
            realInfo.setFinish("0");
            realInfo.setMd5(info.getMd5());
            realInfo.setName(info.getName());
            realInfo.setTotalNum(info.getTotalNum());
            realInfo.setType(info.getName().substring(info.getName().lastIndexOf(".")+1));
            realInfo.setTotalNum(info.getTotalNum());
            fileTransInfoMapper.insert(realInfo);
        } else {
            realInfo = realInfos.get(0);
        }
        return realInfo;
    }

    /**保存文件*/
    public void saveFile(VOFileTransInfo info) throws IOException {
        String hashString = info.getMd5();
        String fileName = "";
        //扩展名左边补0对齐，方便排序
        if (info.getCursor()/10>0){
            fileName = hashString+"."+ info.getCursor();
        } else {
            fileName = hashString+"."+ "0" + info.getCursor();
        }
        String parentDirStr = PART_FILE_DIR + hashString;
        File parentDir = new File(parentDirStr);
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
        File destFile = new File(parentDir + "/" + fileName);
        info.getFile().transferTo(destFile);
    }
    
    /**合并文件的实际操作*/
    public static void doMergeFiles(String outFile, String[] files, String parentDirStr) {
        int BUFSIZE = 1024 * 1024;
        FileChannel outChannel = null;
        System.out.println("Merge " + Arrays.toString(files) + " into " + outFile);
        String lastFlag = files[files.length-1];
        int len = 0;
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for(String f : files){
                if (outFile.equals(parentDirStr + "/" + f)) {
                    continue;
                }
                if(lastFlag.equals(f)){
                    File last = new File(parentDirStr + "/" + f);
                    BUFSIZE = (int) last.length();
                }
                FileChannel fc = new FileInputStream(parentDirStr + "/" + f).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                while(fc.read(bb) != -1){
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
            System.out.println("Merged " + outFile + "!!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}
        }
    }

    /**删除文件*/
    public static void deleteTempFiles(String parentDirStr){
        File par = new File(parentDirStr);
        System.out.println("deleting files..");
        if (par.isDirectory()){
            String[] list = par.list();
            for (String f : list){
                System.out.println("deleting " + parentDirStr +"/"+f);
                File temp = new File(parentDirStr +"/"+f);
                temp.delete();
            }
        }
        par.delete();
    }

    @Async
    public void callBack(CallBack callBack, CmsFileTransInfo info) throws Exception {
        callBack.doCallBack(info);
    }

}
