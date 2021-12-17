package io.github.rothschil.domain.entity;

import io.github.rothschil.common.po.BasePo;
import lombok.*;

@EqualsAndHashCode(callSuper=false)
@Builder(toBuilder=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo extends BasePo<Long> {
    private Long id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String suffixName;
    private String md5;
}