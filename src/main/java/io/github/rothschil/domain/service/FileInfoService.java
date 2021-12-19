package io.github.rothschil.domain.service;//package xyz.wongs.drunkard.war3.domain.service;

import io.github.rothschil.base.persistence.mybatis.service.BaseService;
import io.github.rothschil.domain.entity.FileInfo;
import io.github.rothschil.domain.mapper.FileInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**

 * @author <a>https://github.com/rothschil</a>
 * @date 2020/9/9 16:11
 * @since  1.0.0
*/
@Slf4j
@Service(value="fileInfoService")
@Transactional(readOnly = true)
public class FileInfoService extends BaseService<FileInfoMapper,FileInfo, Long> {

	@Transactional(readOnly = false)
	public void insert(List<FileInfo> lists){
        baseMpper.batchInsert(lists);
	}

}
