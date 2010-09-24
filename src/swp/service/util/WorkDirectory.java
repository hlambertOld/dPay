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

    public synchronized void setWorkDirectory(File workDirectory) {
        if (this.workDirectory == null) {
            this.workDirectory = workDirectory;
            if (!workDirectory.exists()) {
                throw new RuntimeException("work directory " + workDirectory.getAbsolutePath() + " does not exist");
            }
        } else {
            throw new RuntimeException("work directory can only be set once");
        }
    }
}
