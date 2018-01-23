import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

// new Objectをモックするテスト
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DateUtil.class })
public class DateUtilTest {
	@Test
	public void test007() throws Exception {
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

	@Test
	public void test008() throws Exception {
		// 記録フェーズ
		Date dateMocked = mock(Date.class);
		whenNew(Date.class).withNoArguments().thenReturn(dateMocked);
		when(dateMocked.getTime()).thenThrow(new RuntimeException("aaa"));

		// リプレイフェーズ
		try {
			String strDate = DateUtil.getCurrentDate();
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("aaa"));
		}
	}

	@Test
	public void test009() throws Exception {
		// 記録フェーズ
		Date dateMocked = mock(Date.class);
		whenNew(Date.class).withNoArguments().thenReturn(dateMocked);
		doThrow(new RuntimeException("bbb")).when(dateMocked).setTime(anyLong());

		// リプレイフェーズ
		try {
			DateUtil.setCurrentDate(123456789L);
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("bbb"));
		}
	}

	@Test
	public void test010() {
		PowerMockito.mockStatic(String.class);
		PowerMockito.when(String.valueOf(anyBoolean())).thenReturn("false");

		assertThat(String.valueOf(true), is("false"));
		assertThat(String.valueOf(false), is("false"));
	}

	@Test
	public void test011() {
		PowerMockito.mockStatic(String.class);
		PowerMockito.when(String.valueOf(true)).thenThrow(new RuntimeException("aaa"));

		try {
			assertThat(String.valueOf(true), is("false"));
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("aaa"));
		}
		assertNull(String.valueOf(false));
	}

	@Test
	public void test012() {
		PowerMockito.mockStatic(DateUtil.class);
		try {
			doThrow(new RuntimeException("bbb")).when(DateUtil.class, "setCurrentDate", anyLong());
		} catch (Exception e) {
			fail();
		}

		try {
			DateUtil.setCurrentDate(12345L);
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("bbb"));
		}
	}

	@Test
	public void test016() throws Exception {
		// 記録フェーズ
		DateUtil dateMocked = PowerMockito.spy(new DateUtil());
		PowerMockito.doReturn("12345678").when(dateMocked, "getYYYYMMDDString", (Date) any());

		// リプレイフェーズ
		String strDate = dateMocked.getYYYYMMDDStringWrapper(new Date());

		// 検証フェーズ
		assertThat(strDate, is("12345678"));
	}

	@Test
	public void test017() throws Exception {
		// 記録フェーズ
		DateUtil dateMocked = PowerMockito.spy(new DateUtil());
		PowerMockito.doThrow(new RuntimeException("aaa")).when(dateMocked, "getYYYYMMDDString", (Date) any());

		// リプレイフェーズ
		try {
			String strDate = dateMocked.getYYYYMMDDStringWrapper(new Date());
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("aaa"));
		}
	}

	@Test
	public void test018() throws Exception {
		// 記録フェーズ
		DateUtil dateMocked = PowerMockito.spy(new DateUtil());
		PowerMockito.doThrow(new Exception("bbb")).when(dateMocked, "setFlag", anyBoolean());

		// リプレイフェーズ
		Method setFlagMethod = DateUtil.class.getDeclaredMethod("setFlag", Boolean.class);
		setFlagMethod.setAccessible(true);
		try {
			setFlagMethod.invoke(dateMocked, true);
			fail();
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				InvocationTargetException ite = (InvocationTargetException) e;
				assertThat(ite.getTargetException().getMessage(), is("bbb"));
			} else {
				fail();
			}
		}
	}
}