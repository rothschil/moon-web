package io.github.rothschil.domain.controller;

import com.github.pagehelper.PageInfo;
import io.github.rothschil.base.persistence.mybatis.page.PaginationInfo;
import io.github.rothschil.domain.entity.FileInfo;
import io.github.rothschil.domain.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 2021/12/19 - 10:07
 * @since 1.0.0
 */
@RestController
public class FileInfoController {

    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST})
    public PageInfo<FileInfo> list(){
        PaginationInfo paginationInfo = new PaginationInfo(1,10);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setSuffixName("NEF");
        return fileInfoService.selectPage(paginationInfo,fileInfo);
    }

    private FileInfoService fileInfoService;

    @Autowired
    public void setFileInfoService(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }
}
