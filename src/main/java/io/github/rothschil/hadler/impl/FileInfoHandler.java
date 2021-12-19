package io.github.rothschil.hadler.impl;

import io.github.rothschil.domain.entity.FileInfo;
import io.github.rothschil.hadler.IntfFileInfoHandler;
import io.github.rothschil.domain.service.FileInfoService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 2021/12/17 - 19:35
 * @since 1.0.0
 */
@Data
@Slf4j
@Component("fileInfoHandler")
public class FileInfoHandler implements IntfFileInfoHandler {

    private List<FileInfo> lists;

    @Autowired
    private FileInfoService fileInfoService;

    @Override
    public void processData(){
        fileInfoService.insert(lists);
    }
}
