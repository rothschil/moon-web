package io.github.rothschil.task;

import io.github.rothschil.common.utils.file.FileUtil;
import io.github.rothschil.common.utils.thread.ThreadPoolsUtil;
import io.github.rothschil.consts.ImageConst;
import io.github.rothschil.domain.entity.FileInfo;
import io.github.rothschil.domain.service.FileInfoService;
import io.github.rothschil.hadler.impl.FileInfoHandler;
import io.github.rothschil.queue.FileInfoQueue;
import io.github.rothschil.thread.FileSizeThread;
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

    public void run(String path){
        File file = new File(path);
        if(!file.isDirectory()){
            return;
        }
        listFiles(file);
    }

    public void listFiles(File file){
        File[] files = file.listFiles();
        List<FileInfo> lists = new ArrayList<>();
        if(files.length==0){
            return;
        }
        for (File fl : files) {
            if(fl.isDirectory()){
                listFiles(fl);
                continue;
            }
            String suffixName = FileUtil.getSuffix(fl);
            if(!ImageConst.LIST_SUFFIX.contains(suffixName.toUpperCase())){
                continue;
            }
            Future<String> result = executor.submit(new FileSizeThread(fl));
            String fileName = FileUtil.getName(fl);
            long size = fl.length();
            String filePath = FileUtil.getAbsolutePath(fl);
            try {
                FileInfo fileInfo = FileInfo.builder().fileName(fileName).filePath(filePath).fileSize(size).suffixName(suffixName)
                        .md5(result.get()).build();
                lists.add(fileInfo);
            } catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        }
        if(!lists.isEmpty()){
            fileInfoHandler.setLists(lists);
            fileInfoQueue.addQueue(fileInfoHandler);
        }
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
