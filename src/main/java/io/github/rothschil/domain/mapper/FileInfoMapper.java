package io.github.rothschil.domain.mapper;

import io.github.rothschil.base.persistence.mybatis.mapper.BaseMapper;
import io.github.rothschil.domain.entity.FileInfo;

import java.util.List;


public interface FileInfoMapper extends BaseMapper<FileInfo,Long> {

    void batchInsert(List<FileInfo> lists);

}