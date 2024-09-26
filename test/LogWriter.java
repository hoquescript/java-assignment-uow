import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogWriter {

    private File logFile;

    public LogWriter(File file) {
        this.logFile = file;
    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public void writeLog(String content) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(this.getLogFile(), true));
        try {
            bw.write(content);
            bw.newLine();
        } finally {
            bw.close();
        }
    }

    public static void main(String[] args) {
        LogWriter logWriter = new LogWriter(new File("log.txt"));
        try {
            logWriter.writeLog("This is a log entry.");
            System.out.println("Log written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
