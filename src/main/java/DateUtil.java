import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String getCurrentDate() {
		Date d = new Date();
		return new SimpleDateFormat("yyyy/MM/dd").format(d);
	}

	public static void setCurrentDate(long currentTime) {
		Date d = new Date();
		d.setTime(currentTime);
	}
}
