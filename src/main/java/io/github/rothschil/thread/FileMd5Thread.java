package io.github.rothschil.thread;

import io.github.rothschil.common.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

/** 异步获取文件名
 * @author <a>https://github.com/rothschil</a>
 * @date 20/12/30 13:16
 * @since  1.0.0
*/
public class FileMd5Thread implements Callable<String> {

    private File file;

    public FileMd5Thread(){}

    public FileMd5Thread(File file){
        this.file = file;
    }

    @Override
    public String call() {
        try {
            return DigestUtils.md5Hex(new FileInputStream(file));
        } catch (IOException e) {
            return StringUtils.EMPTY;
        }
    }
}
