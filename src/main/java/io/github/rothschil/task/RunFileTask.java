package io.github.rothschil.task;

import io.github.rothschil.common.utils.file.FileUtil;
import io.github.rothschil.common.utils.thread.ThreadPoolsUtil;
import io.github.rothschil.consts.ImageConst;
import io.github.rothschil.domain.entity.FileInfo;
import io.github.rothschil.domain.service.FileInfoService;
import io.github.rothschil.hadler.impl.FileInfoHandler;
import io.github.rothschil.queue.FileInfoQueue;
import io.github.rothschil.thread.FileMd5Thread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**

 * @author <a>https://github.com/rothschil</a>
 * @date 20/12/30 12:58
 * @since 1.0.0
*/
@Component
@Slf4j
public class RunFileTask {

    public static final String THREAD_NAME ="RUN_FILE_NAME";

    private ThreadPoolExecutor executor = ThreadPoolsUtil.doCreate(3,5,THREAD_NAME);


    /**
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/12/19-9:51
     * @param targetFile  目标文件
     **/
    public void run(File targetFile){
        if(!targetFile.isDirectory()){
            return;
        }
        listFiles(targetFile);
    }

    /**
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/12/19-9:51
     * @param targetFilePath  目标文件路径
     **/
    public void run(String targetFilePath){
        File file = new File(targetFilePath);
        run(file);
    }

    protected void listFiles(File file){
        File[] files = file.listFiles();
        int fileLength = files.length;
        if(0==fileLength){
            return;
        }
        List<FileInfo> lists = new ArrayList<>(fileLength);
        for (File elmFile : files) {
            if(elmFile.isDirectory()){
                listFiles(elmFile);
                continue;
            }
            String suffixName = FileUtil.getSuffix(elmFile);
            if(!ImageConst.LIST_SUFFIX.contains(suffixName.toUpperCase())){
                continue;
            }
            Future<String> md5Future = executor.submit(new FileMd5Thread(elmFile));
            String fileName = FileUtil.getName(elmFile);
            long size = elmFile.length();
            String filePath = FileUtil.getAbsolutePath(elmFile);
            try {
                FileInfo fileInfo = FileInfo.builder().fileName(fileName).filePath(filePath).fileSize(size).suffixName(suffixName)
                        .md5(md5Future.get()).build();
                lists.add(fileInfo);
            } catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        }
        if(!lists.isEmpty()){
            fileInfoHandler.setLists(lists);
            fileInfoQueue.addQueue(fileInfoHandler);
        }
        lists.clear();
    }

    public FileInfoService fileInfoService;

    private FileInfoQueue fileInfoQueue;

    private FileInfoHandler fileInfoHandler;

    @Autowired
    public void setFileInfoService(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }

    @Autowired
    public void setFileInfoQueue(FileInfoQueue fileInfoQueue) {
        this.fileInfoQueue = fileInfoQueue;
    }

    @Autowired
    public void setFileInfoHandler(FileInfoHandler fileInfoHandler) {
        this.fileInfoHandler = fileInfoHandler;
    }
}
