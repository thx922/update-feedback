package csio.client;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.charset.CoderResult.OVERFLOW;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Created by ike on 16-10-18.
 */
public class TestWatcherService {

        private WatchService watcher;

        public TestWatcherService(Path path) throws IOException {
            watcher = FileSystems.getDefault().newWatchService();
            path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        }

    //        public void handleEvents( List<String> webFilePath ,List<String> webPackageName, String cilentHtmlPath ) throws InterruptedException {
        public void handleEvents() throws InterruptedException {
            while (true) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind kind = event.kind();

                    if (kind == OVERFLOW) { //事件可能是lost or discarded
                        continue;
                    }

                    WatchEvent<Path> e = (WatchEvent<Path>) event;
                    Path fileName = e.context();
                    System.out.println("happen: " + kind.name() + "----" + fileName);
                    System.out.println("=======================");

                }
//                Client client = new Client();
//                client.clientStart(webFilePath ,webPackageName, cilentHtmlPath);
                if (!key.reset()) {
                    break;
                }
            }
        }

        public static void main(String[] args) throws InterruptedException, IOException {
            new TestWatcherService(Paths.get("/home/ike/java/java/feedback/java/src/main/java/dnn/web1")).handleEvents();
        }
    }