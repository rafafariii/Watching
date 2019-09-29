package Monitoramento;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;


public class Monitoramento {

	 private static final Kind<?> ENTRY_CREATE = null;
	private static final Kind<?> ENTRY_MODIFY = null;
	private static final Kind<?> ENTRY_DELETE = null;
	private static final Kind OVERFLOW = null;

	public static void main(String[] args) throws IOException {
	        m2();
	    }
	    
	    static void m2() throws IOException{
	        Path pastaMonitorada = Paths.get("/home/rafael/Documentos/monitoramento");    // teste local ok  ("C:\\pasta");        
	        
	        WatchService monitor = FileSystems.getDefault().newWatchService();
	        Map<WatchKey, Path> registroPastasMonitoradas = new HashMap<>();
	        
	        //eventos q serão monitorados
	        WatchKey setMonitorEventos = pastaMonitorada.register(monitor, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
	        
	        //link watched folder with events to be listened
	        registroPastasMonitoradas.put(setMonitorEventos, pastaMonitorada);
	        
	        while(true){
	            WatchKey watchKey = null;
	            try {
	                watchKey = monitor.take();
	            } catch (InterruptedException e) {
	                System.out.println("interrupeted ex: "+e.getMessage());
	            }
	            
	            //pasta monitorada
	            Path pasta = registroPastasMonitoradas.get(watchKey);
	            System.out.println("pasta: "+pasta.toString());
	            if(pasta == null){
	                System.err.println("WatchKey not recognized!!");
	                continue;
	            }
	            
	            for(WatchEvent<?> event : watchKey.pollEvents()){
	                WatchEvent.Kind kind = event.kind();
	                
	                if (kind == OVERFLOW) {
	                    continue;
	                }
	                
	                WatchEvent<Path> ev = cast(event);
	                Path name = ev.context();
	                Path child = pastaMonitorada.resolve(name);
	                //imprime o evento     
	                System.out.format("%s: %s\n",  event.kind().name(), child);
	                
	                boolean valid = setMonitorEventos.reset();
	                if(!valid){
	                    registroPastasMonitoradas.remove(watchKey);
	                    
	                    if(registroPastasMonitoradas.isEmpty())
	                        break;
	                }
	            }                     
	        }
	    }
	    
	    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
	        return (WatchEvent<T>)event;
	    }    
	}