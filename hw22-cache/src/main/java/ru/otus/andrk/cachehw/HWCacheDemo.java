package ru.otus.andrk.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;



public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
        System.out.println();
        new HWCacheDemo().demoWithSideEffect();
    }

    private HwListener<String, Integer> listener;
    private void demo() {
        logger.info("*******    DEMO    *******");
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        listener = new HwListener<>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        doDemoContent(cache);
    }

    private void demoWithSideEffect() {
        logger.info("*******    DEMO with side effect    *******");

        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        listener = (key, value, action) -> logger.info("key:{}, value:{}, action: {}", key, value, action);
        doDemoContent(cache);
    }

    private void doDemoContent(HwCache<String, Integer> cache) {

        logger.info("***    Add Listener by ref to Cache");
        var listenerRef = new WeakReference<>(listener);
        cache.addListener(listenerRef);
        listener = null;
        showToLogListenerRefState(listenerRef);

        logger.info("***    Working equally");
        cache.put("1", 1);
        logger.info("GetValue:{}", cache.get("1"));

        logger.info("***    Prepare Side-effect");
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("***    Expect that no listener, but it could be");
        showToLogListenerRefState(listenerRef);
        cache.get("1");
        cache.remove("1");

        logger.info("***    Kill listener if exist");
        cache.removeListener(listenerRef);

        logger.info("***    No Listener");
        cache.put("tst", 1);
        cache.get("tst");
        cache.remove("tst");
    }

    private <T> void showToLogListenerRefState(WeakReference<T> ref) {
        logger.info("listener={}", ref.get());
    }


}
