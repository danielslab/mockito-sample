import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MockitoTest {
	/**
	 * メソッド	外部から注入可能	public	void以外	全部	値を戻す
	 */
	@Test
	public void test001() {
		List mockedList = mock(List.class);

		when(mockedList.get(0)).thenReturn("aaa");

		assertThat((String)mockedList.get(0), is("aaa"));
		assertNull(mockedList.get(1));
	}

	/**
	 * メソッド	外部から注入可能	public	void以外	全部	例外を投げる
	 */
	@Test
	public void test002() {
		List mockedList = mock(List.class);

		when(mockedList.get(0)).thenThrow(new RuntimeException("aaa"));

		try {
			mockedList.get(0);
			fail();
		} catch(RuntimeException e) {
			assertThat(e.getMessage(), is("aaa"));
		}
	}

	/**
	 * メソッド	外部から注入可能	public	void	全部	例外を投げる
	 */
	@Test
	public void test003() {
		List mockedList = mock(List.class);

		doThrow(new RuntimeException("aaa")).when(mockedList).clear();

		try {
			mockedList.clear();
			fail();
		} catch(RuntimeException e) {
			assertThat(e.getMessage(), is("aaa"));
		}
	}

	/**
	 * メソッド	外部から注入可能	public	void以外	一部	値を戻す
	 */
	@Test
	public void test004() {
		List spiedList = spy(new ArrayList());

		// リアルオブジェクトには"bbb"を追加したが
		spiedList.add("bbb");
		// スパイでget(0)は"aaa"を返すようにした
		when(spiedList.get(0)).thenReturn("aaa");

		// 結果は"aaa"が得られる
		assertThat((String)spiedList.get(0), is("aaa"));
		// その他のメソッドはリアルの結果が返される
		assertThat(spiedList.size(), is(1));
	}

	/**
	 * メソッド	外部から注入可能	public	void以外	一部	例外を投げる
	 */
	@Test
	public void test005() {
		List spiedList = spy(new ArrayList());

		// リアルオブジェクトには"bbb"を追加したが
		spiedList.add("bbb");
		// スパイでget(0)はRuntimeExceptionをスローするようにした
		when(spiedList.get(0)).thenThrow(new RuntimeException("aaa"));

		try {
			spiedList.get(0);
			fail();
		} catch(Exception e) {
			assertThat(e.getMessage(), is("aaa"));
		}
		// その他のメソッドはリアルの結果が返される
		assertThat(spiedList.size(), is(1));
	}

	/**
	 * メソッド	外部から注入可能	public	void	一部	例外を投げる
	 */
	@Test
	public void test006() {
		List spiedList = spy(new ArrayList());

		// リアルオブジェクトには"bbb"を追加
		spiedList.add("bbb");
		// clearが呼ばれたときはRuntimeExceptionを投げる
		doThrow(new RuntimeException("aaa")).when(spiedList).clear();

		try {
			spiedList.clear();
			fail();
		} catch(RuntimeException e) {
			assertThat(e.getMessage(), is("aaa"));
		}

		assertThat(spiedList.size(), is(1));
		assertThat((String)spiedList.get(0), is("bbb"));
	}

}
