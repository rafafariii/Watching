package br.com.Watching.app;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WatchingApplication {

	private static final Object ENTRY_CREATE = null;

	public static void main(String[] args) {
		//SpringApplication.run(WatchingApplication.class, args);
		
		try (WatchService service = FileSystems.getDefault().newWatchService()){
			Map<WatchKey, Path> keyMap = new HashMap<>();
			Path path  = Paths.get("/home/rafael/Documentos/monitoramento");
			keyMap.put(path.register(service, 
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY),
					path);
			
			WatchKey watchKey;
			
			do {
				watchKey = service.take();
				Path eventDir = keyMap.get(watchKey);
				
				for (WatchEvent<?> event : watchKey.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					Path eventPath = (Path)event.context();
					if (kind==ENTRY_CREATE) {
					System.out.println("Evento criação neste caspo deveremos tomar uma descisão");
					}
					System.out.println(eventDir + ": " + kind + ": "+ eventPath  );
				}
				
			} while (watchKey.reset());
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}

