package swp.service.util;

import java.io.File;

/**
 */
public class WorkDirectory {

    private File workDirectory;

    private static WorkDirectory INSTANCE = new WorkDirectory();

    private WorkDirectory() {
    }

    public static WorkDirectory getInstance() {
        return INSTANCE;
    }

    public File getWorkDirectory() {
        return workDirectory;
    }

    public synchronized void setWorkDirectory(String workDirectory) {
        if (this.workDirectory == null) {
            this.workDirectory = new File(System.getProperty("user.home"), workDirectory);
            if (!this.workDirectory.exists()) {
                throw new RuntimeException("work directory " + this.workDirectory.getAbsolutePath() + " does not exist");
            }
        } else {
            throw new RuntimeException("work directory can only be set once");
        }
    }
}
