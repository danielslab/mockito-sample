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
	 * ���\�b�h	�O�����璍���\	public	void�ȊO	�S��	�l��߂�
	 */
	@Test
	public void test001() {
		List mockedList = mock(List.class);

		when(mockedList.get(0)).thenReturn("aaa");

		assertThat((String)mockedList.get(0), is("aaa"));
		assertNull(mockedList.get(1));
	}

	/**
	 * ���\�b�h	�O�����璍���\	public	void�ȊO	�S��	��O�𓊂���
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
	 * ���\�b�h	�O�����璍���\	public	void	�S��	��O�𓊂���
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
	 * ���\�b�h	�O�����璍���\	public	void�ȊO	�ꕔ	�l��߂�
	 */
	@Test
	public void test004() {
		List spiedList = spy(new ArrayList());

		// ���A���I�u�W�F�N�g�ɂ�"bbb"��ǉ�������
		spiedList.add("bbb");
		// �X�p�C��get(0)��"aaa"��Ԃ��悤�ɂ���
		when(spiedList.get(0)).thenReturn("aaa");

		// ���ʂ�"aaa"��������
		assertThat((String)spiedList.get(0), is("aaa"));
		// ���̑��̃��\�b�h�̓��A���̌��ʂ��Ԃ����
		assertThat(spiedList.size(), is(1));
	}

	/**
	 * ���\�b�h	�O�����璍���\	public	void�ȊO	�ꕔ	��O�𓊂���
	 */
	@Test
	public void test005() {
		List spiedList = spy(new ArrayList());

		// ���A���I�u�W�F�N�g�ɂ�"bbb"��ǉ�������
		spiedList.add("bbb");
		// �X�p�C��get(0)��RuntimeException���X���[����悤�ɂ���
		when(spiedList.get(0)).thenThrow(new RuntimeException("aaa"));

		try {
			spiedList.get(0);
			fail();
		} catch(Exception e) {
			assertThat(e.getMessage(), is("aaa"));
		}
		// ���̑��̃��\�b�h�̓��A���̌��ʂ��Ԃ����
		assertThat(spiedList.size(), is(1));
	}

	/**
	 * ���\�b�h	�O�����璍���\	public	void	�ꕔ	��O�𓊂���
	 */
	@Test
	public void test006() {
		List spiedList = spy(new ArrayList());

		// ���A���I�u�W�F�N�g�ɂ�"bbb"��ǉ�
		spiedList.add("bbb");
		// clear���Ă΂ꂽ�Ƃ���RuntimeException�𓊂���
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
