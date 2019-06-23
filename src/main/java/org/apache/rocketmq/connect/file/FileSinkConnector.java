package org.apache.rocketmq.connect.file;

import io.openmessaging.KeyValue;
import io.openmessaging.connector.api.Task;
import io.openmessaging.connector.api.sink.SinkConnector;
import java.util.ArrayList;
import java.util.List;

public class FileSinkConnector extends SinkConnector {

    private KeyValue config;


    @Override public String verifyAndSetConfig(KeyValue config) {
        for (String requestKey : FileConfig.REQUEST_CONFIG) {
            if (!config.containsKey(requestKey)) {
                return "Request config key: " + requestKey;
            }
        }
        this.config = config;
        return "";
    }

    @Override public void start() {

    }

    @Override public void stop() {

    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }

    @Override public Class<? extends Task> taskClass() {
        return FileSinkTask.class;
    }

    @Override public List<KeyValue> taskConfigs() {
        List<KeyValue> config = new ArrayList<>();
        config.add(this.config);
        return config;
    }
}
