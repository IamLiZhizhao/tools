package com.lzz.tools.biz;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lizhizhao
 * @since 2020-05-27 11:24
 */
@Component
@Slf4j
public class MQBiz {

    private ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());

    private DelayQueue<DelayedItem> delayQueue = new DelayQueue<>();












    // region  延迟队列发送kafka

    /**
     * 延迟队列发送kafka 延迟1秒
     */
    public void delaySendKafka(String message) {
        delayQueue.add(new DelayedItem(System.currentTimeMillis() + 4000, message));
        executorService.submit(() -> {
            while (!delayQueue.isEmpty()) {
                try {
                    DelayedItem delayedItem = delayQueue.take();
                    // TODO 发消息的操作
//                    mqBiz.createMaterialMessage(delayedItem.getMaterialDO(), delayedItem.getIdentifyParam());
                } catch (Exception e) {
                    log.error("延迟发送消息失败", e);
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    @Getter
    @Setter
    class DelayedItem implements Delayed {

        private Long cancelTime;
        private String message;

        DelayedItem(Long cancelTime, String message) {
            this.cancelTime = cancelTime;
            this.message = message;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(cancelTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return this.getCancelTime().compareTo(((DelayedItem) o).getCancelTime());
        }
    }

    // endregion
}
