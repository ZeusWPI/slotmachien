import java.io.File;
import java.io.IOException;


public class LockFile {

	private final String path;
	
	public LockFile(String path) {
		this.path = path;
	}
	
	public void create() throws IOException{
		File f = new File(path);
		if(f.exists()){
			throw new IllegalStateException("FELIKAAN! DO NOT MAKE MULTIPLE INSTANCES OF PCMAIN!\nLockfile "+path+" already exists");
		}
		f.createNewFile();
		f.deleteOnExit();
		System.out.println("Created lock file on "+f);
	}
	
	public boolean exists(){
		// delegeren is eren
		return new File(path).exists();
	}
	
}
