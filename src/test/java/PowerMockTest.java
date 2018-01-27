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

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DateUtil.class })
public class PowerMockTest {
	/**
	 * newで生成されるオブジェクト、publicメソッド、戻り型はvoid以外、全部モック、値を戻す
	 */
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

	/**
	 * newで生成されるオブジェクト、publicメソッド、戻り型はvoid以外、全部モック、例外を投げる
	 */
	@Test
	public void test008() throws Exception {
		// 記録フェーズ
		Date dateMocked = mock(Date.class);
		whenNew(Date.class).withNoArguments().thenReturn(dateMocked);
		when(dateMocked.getTime()).thenThrow(new RuntimeException("aaa"));

		// リプレイ＆検証フェーズ
		try {
			DateUtil.getCurrentDate();
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("aaa"));
		}
	}

	/**
	 * newで生成されるオブジェクト、publicメソッド、戻り型はvoid、全部モック、例外を投げる
	 */
	@Test
	public void test009() throws Exception {
		// 記録フェーズ
		Date dateMocked = mock(Date.class);
		whenNew(Date.class).withNoArguments().thenReturn(dateMocked);
		doThrow(new RuntimeException("bbb")).when(dateMocked).setTime(anyLong());

		// リプレイ＆検証フェーズ
		try {
			DateUtil.setCurrentDate(123456789L);
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("bbb"));
		}
	}

	/**
	 * public staticメソッド、戻り型はvoid以外、全部モック、値を戻す
	 */
	@Test
	public void test010() {
		// 記録フェーズ
		PowerMockito.mockStatic(String.class);
		PowerMockito.when(String.valueOf(anyBoolean())).thenReturn("false");

		// リプレイ＆検証フェーズ
		assertThat(String.valueOf(true), is("false"));
		assertThat(String.valueOf(false), is("false"));
	}

	/**
	 * public staticメソッド、戻り型はvoid以外、全部モック、例外を投げる
	 */
	@Test
	public void test011() {
		// 記録フェーズ
		PowerMockito.mockStatic(String.class);
		PowerMockito.when(String.valueOf(true)).thenThrow(new RuntimeException("aaa"));

		// リプレイ＆検証フェーズ
		try {
			String.valueOf(true);
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("aaa"));
		}
		assertNull(String.valueOf(false));
	}

	/**
	 * public staticメソッド、戻り型はvoid、全部モック、例外を投げる
	 */
	@Test
	public void test012() {
		// 記録フェーズ
		PowerMockito.mockStatic(DateUtil.class);
		try {
			doThrow(new RuntimeException("bbb")).when(DateUtil.class, "setCurrentDate", anyLong());
		} catch (Exception e) {
			fail();
		}

		// リプレイ＆検証フェーズ
		try {
			DateUtil.setCurrentDate(12345L);
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("bbb"));
		}
	}

	/**
	 * privateメソッド、戻り型はvoid以外、全部モック、値を戻す
	 */
	@Test
	public void test013() throws Exception {
		// 記録フェーズ
		DateUtil dateMocked = PowerMockito.mock(DateUtil.class);
		PowerMockito.when(dateMocked, "getYYYYMMDDString", (Date) any()).thenReturn("12345678");

		// リプレイフェーズ
		Method getYYYYMMDDStringMethod = DateUtil.class.getDeclaredMethod("getYYYYMMDDString", Date.class);
		getYYYYMMDDStringMethod.setAccessible(true);
		String actual = (String)getYYYYMMDDStringMethod.invoke(dateMocked, new Date());

		// 検証フェーズ
		assertThat(actual, is("12345678"));
	}

	/**
	 * privateメソッド、戻り型はvoid以外、全部モック、例外を投げる
	 */
	@Test
	public void test014() throws Exception {
		// 記録フェーズ
		DateUtil dateMocked = PowerMockito.mock(DateUtil.class);
		PowerMockito.when(dateMocked, "getYYYYMMDDString", (Date) any()).thenThrow(new Exception("aaa"));

		// リプレイ＆検証フェーズ
		try {
			Method getYYYYMMDDStringMethod = DateUtil.class.getDeclaredMethod("getYYYYMMDDString", Date.class);
			getYYYYMMDDStringMethod.setAccessible(true);
			getYYYYMMDDStringMethod.invoke(dateMocked, new Date());
			fail();
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				InvocationTargetException ite = (InvocationTargetException) e;
				assertThat(ite.getTargetException().getMessage(), is("aaa"));
			} else {
				fail();
			}
		}
	}

	/**
	 * privateメソッド、戻り型はvoid、全部モック、例外を投げる
	 */
	@Test
	public void test015() throws Exception {
		// 記録フェーズ
		DateUtil dateMocked = PowerMockito.mock(DateUtil.class);
		PowerMockito.doThrow(new Exception("bbb")).when(dateMocked, "setFlag", anyBoolean());

		// リプレイ＆検証フェーズ
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

	/**
	 * privateメソッド、戻り型はvoid以外、一部モック、値を戻す
	 */
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

	/**
	 * privateメソッド、戻り型はvoid以外、一部モック、例外を投げる
	 */
	@Test
	public void test017() throws Exception {
		// 記録フェーズ
		DateUtil dateMocked = PowerMockito.spy(new DateUtil());
		PowerMockito.doThrow(new RuntimeException("aaa")).when(dateMocked, "getYYYYMMDDString", (Date) any());

		// リプレイ＆検証フェーズ
		try {
			dateMocked.getYYYYMMDDStringWrapper(new Date());
			fail();
		} catch (RuntimeException e) {
			assertThat(e.getMessage(), is("aaa"));
		}
	}

	/**
	 * privateメソッド、戻り型はvoid、一部モック、例外を投げる
	 */
	@Test
	public void test018() throws Exception {
		// 記録フェーズ
		DateUtil dateMocked = PowerMockito.spy(new DateUtil());
		PowerMockito.doThrow(new Exception("bbb")).when(dateMocked, "setFlag", anyBoolean());

		// リプレイ＆検証フェーズ
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