import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

// new Objectをモックするテスト
@RunWith(PowerMockRunner.class)
@PrepareForTest(DateUtil.class)
public class DateUtilTest {	
	@Test
	public void testGetCurrentDate() throws Exception {
		// 期待値は2015/05/05
		Calendar c = Calendar.getInstance();
		c.set(2015, 4, 5);
		Date expected = c.getTime();

		// 記録フェーズ
		Date dateMocked = mock(Date.class);
		whenNew(Date.class).withNoArguments().thenReturn(dateMocked);
		when(dateMocked.getTime()).thenReturn(expected.getTime());
		
		// リプレイフェーズ
		String strDate = DateUtil.getCurrentDate();
		
		// 検証フェーズ
		assertThat(strDate, is("2015/05/05"));
	}
}
