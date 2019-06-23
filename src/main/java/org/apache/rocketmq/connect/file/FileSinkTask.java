package org.apache.rocketmq.connect.file;

import io.openmessaging.KeyValue;
import io.openmessaging.connector.api.data.SinkDataEntry;
import io.openmessaging.connector.api.exception.ConnectException;
import io.openmessaging.connector.api.sink.SinkTask;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSinkTask extends SinkTask {

    private Logger log = LoggerFactory.getLogger(FileSinkTask.class);

    private FileConfig fileConfig;

    private PrintStream outputStream;

    @Override public void put(Collection<SinkDataEntry> sinkDataEntries) {
        for (SinkDataEntry record : sinkDataEntries) {
            log.trace("Writing line to {}: {}", logFilename(), record.getPayload());
            outputStream.println(record.getPayload());
        }
    }

    @Override public void start(KeyValue props) {
        fileConfig = new FileConfig();
        fileConfig.load(props);
        if (fileConfig.getFilename() == null || fileConfig.getFilename().isEmpty()) {
            outputStream = System.out;
        } else {
            try {
                outputStream = new PrintStream(
                    Files.newOutputStream(Paths.get(fileConfig.getFilename()), StandardOpenOption.CREATE, StandardOpenOption.APPEND),
                    false,
                    StandardCharsets.UTF_8.name());
            } catch (IOException e) {
                throw new ConnectException(-1, "Couldn't find or create file '" + fileConfig.getFilename() + "' for FileStreamSinkTask", e);
            }
        }
    }

  /*  public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {
        log.trace("Flushing output stream for {}", logFilename());
        outputStream.flush();
    }*/

    @Override public void stop() {
        if (fileConfig.getFilename() == null || fileConfig.getFilename().isEmpty()) {
            outputStream = System.out;
        } else {
            try {
                outputStream = new PrintStream(
                    Files.newOutputStream(Paths.get(fileConfig.getFilename()), StandardOpenOption.CREATE, StandardOpenOption.APPEND),
                    false,
                    StandardCharsets.UTF_8.name());
            } catch (IOException e) {
                throw new ConnectException(-1, "Couldn't find or create file '" + fileConfig.getFilename() + "' for FileStreamSinkTask", e);
            }
        }
    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }

    private String logFilename() {
        return fileConfig.getFilename() == null ? "stdout" : fileConfig.getFilename();
    }

}
