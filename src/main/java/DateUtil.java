import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private Boolean flag;
	public static String getCurrentDate() {
		Date d = new Date();
		return new SimpleDateFormat("yyyy/MM/dd").format(d);
	}

	public static void setCurrentDate(long currentTime) {
		Date d = new Date();
		d.setTime(currentTime);
	}

	public String getYYYYMMDDStringWrapper(Date date) {
		return getYYYYMMDDString(date);
	}

	private String getYYYYMMDDString(Date date)  {
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}

	private void setFlag(Boolean flag) throws Exception {
		this.flag = flag;
	}
}
