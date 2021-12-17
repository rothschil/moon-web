package io.github.rothschil.queue;

import io.github.rothschil.hadler.IntfFileInfoHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/** 异步文件写入队列

 * @author <a>https://github.com/rothschil</a>
 * @date 20/11/13 16:14
 * @since 1.0.0
*/
@Slf4j
@Component
public class FileInfoQueue {

    private final LinkedBlockingQueue<IntfFileInfoHandler> queue = new LinkedBlockingQueue<>(500);

    /**
     * 线程池
     */
    private ExecutorService service = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    /**
     * 检查服务是否运行
     */
    private volatile boolean running = true;

    /**
     * 线程状态
     */
    private Future<?> threadStatus = null;

    @PostConstruct
    public void init(){
        threadStatus = service.submit(
                () -> {
                    while(running){
                        try {
                            // 队列中不存在元素 则不处理
                            if(!queue.isEmpty()){
                                IntfFileInfoHandler taskHandler = queue.take();
                                taskHandler.processData();
                            }
                        } catch (InterruptedException e) {
                            log.error("服务停止，退出", e);
                            running = false;
                        }
                    }
                });
    }

    @PreDestroy
    public void destory() {
        running = false;
        service.shutdownNow();
    }

    public void activeService() {
        running = true;
        if (service.isShutdown()) {
            service = Executors.newSingleThreadExecutor();
            init();
            log.info("线程池关闭，重新初始化线程池及任务");
        }
        if (threadStatus.isDone()) {
            init();
            log.info("线程池任务结束，重新初始化任务");
        }
    }

    public boolean addQueue(IntfFileInfoHandler taskHandler){
        if(!running){
            log.warn("service is stop");
            return false;
        }
        boolean isFull = queue.offer(taskHandler);
        if(!isFull){
            log.warn("添加任务到队列失败");
        }
        return isFull;
    }
    public boolean empty(){
        return queue.isEmpty();
    }

}
