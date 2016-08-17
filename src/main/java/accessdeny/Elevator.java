package accessdeny;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;

@SpringBootApplication
public class Elevator implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Elevator.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Shell32X.SHELLEXECUTEINFO execInfo = new Shell32X.SHELLEXECUTEINFO();
        execInfo.lpFile = new WString(args[0]);
        if (args.length > 1) {
        	String argsx = "";
        	for (String value : args) {
				argsx = argsx + value + " ";
			}
        	execInfo.lpParameters = new WString(argsx);
        }
        execInfo.nShow = Shell32X.SW_SHOWDEFAULT;
        execInfo.fMask = Shell32X.SEE_MASK_NOCLOSEPROCESS;
        execInfo.lpVerb = new WString("runas");
        boolean result = Shell32X.INSTANCE.ShellExecuteEx(execInfo);

        if (!result) {
            int lastError = Kernel32.INSTANCE.GetLastError();
            String errorMessage = Kernel32Util.formatMessageFromLastErrorCode(lastError);
            throw new RuntimeException("Error performing elevation: " + lastError + ": " + 
            errorMessage + " (apperror=" + execInfo.hInstApp + ")");
        }		
		
		
	}

}
