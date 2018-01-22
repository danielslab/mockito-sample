import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * http://static.javadoc.io/org.mockito/mockito-all/1.9.5/org/mockito/Mockito.html
 */
public class MockitoExampleTest {
	/**
	 * 1. Let's verify some behaviour!
	 */
	@Test
	public void test01() {
		List mockedList = mock(List.class);

		mockedList.add("one");
		mockedList.clear();

		verify(mockedList).add("one");
		verify(mockedList).clear();
	}

	/**
	 * 2. How about some stubbing?
	 */
	@Test
	public void test02() {
		LinkedList mockedList = mock(LinkedList.class);

		when(mockedList.get(0)).thenReturn("first");
		when(mockedList.get(1)).thenThrow(new RuntimeException());

		System.out.println(mockedList.get(0));

		try {
			System.out.println(mockedList.get(1));
			fail();
		} catch (RuntimeException e) {
			// Do nothing.
		}

		System.out.println(mockedList.get(999));

		verify(mockedList).get(0);
	}
	
	/**
	 * 3. Argument matchers
	 */
	@Test
	public void test03() {
		LinkedList mockedList = mock(LinkedList.class);

		when(mockedList.get(anyInt())).thenReturn("element");
		
		when(mockedList.contains(argThat(isValid()))).thenReturn(true);
		
		System.out.println(mockedList.get(999));
		
		verify(mockedList).get(anyInt());
	}

	private Matcher<Object> isValid() {
		// TODO Auto-generated method stub
		return org.hamcrest.CoreMatchers.notNullValue();
	}

	/**
	 * 3. Argument matchers
	 */
	@Test
	public void test03_1() {
		LinkedList mockedList = mock(LinkedList.class);

		when(mockedList.get(0)).thenReturn("element");
		
		when(mockedList.contains(argThat(isValid()))).thenReturn(true);
		
		assertThat((String)mockedList.get(0), is("element"));
		assertNull(mockedList.get(1));
		assertTrue(mockedList.contains("element"));
		assertFalse(mockedList.contains(null));
		
		verify(mockedList).get(0);
		verify(mockedList).get(1);
		verify(mockedList).contains("element");
		verify(mockedList).contains(null);
	}

	/**
	 * 4. Verifying exact number of invocations / at least x / never
	 */
	@Test
	public void test04() {
		LinkedList mockedList = mock(LinkedList.class);

		mockedList.add("once");
		
		mockedList.add("twice");
		mockedList.add("twice");
		
		mockedList.add("three times");
		mockedList.add("three times");
		mockedList.add("three times");
		
		for(int i=0; i<5; i++) {
			mockedList.add("five times");
		}
		verify(mockedList).add("once");
		verify(mockedList, times(1)).add("once");
		
		verify(mockedList, times(2)).add("twice");
		verify(mockedList, times(3)).add("three times");
		
		verify(mockedList, never()).add("never happened");
		
		verify(mockedList, atLeastOnce()).add("three times");
		verify(mockedList, atLeast(2)).add("five times");
		verify(mockedList, atMost(5)).add("three times");
	}

	/**
	 * 5. Stubbing void methods with exceptions
	 */
	@Test
	public void test05() {
		LinkedList mockedList = mock(LinkedList.class);
		
		doThrow(new RuntimeException()).when(mockedList).clear();
		
		try {
			mockedList.clear();
			fail();
		} catch(RuntimeException e) {
			
		}
	}

	/**
	 * 6. Verification in order
	 */
	@Test
	public void test06() {
		List singleMock = mock(List.class);
		
		singleMock.add("was added first");
		singleMock.add("was added second");
		
		InOrder inOrder = Mockito.inOrder(singleMock);
		
		inOrder.verify(singleMock).add("was added first");
		inOrder.verify(singleMock).add("was added second");
		
		List firstMock = mock(List.class);
		List secondMock = mock(List.class);
		
		firstMock.add("was called first");
		secondMock.add("was called second");
		
		InOrder inOrder2 = Mockito.inOrder(firstMock, secondMock);
		
		inOrder2.verify(firstMock).add("was called first");
		inOrder2.verify(secondMock).add("was called second");
	}
	
	/**
	 * 7. Making sure interaction(s) never happened on mock
	 */
	@Test
	public void test07() {
		LinkedList mockOne = mock(LinkedList.class);
		LinkedList mockTwo = mock(LinkedList.class);
		LinkedList mockThree = mock(LinkedList.class);
		
		mockOne.add("one");
		verify(mockOne).add("one");
		
		verify(mockOne, never()).add("two");
		
		verifyZeroInteractions(mockTwo, mockThree);
	}

	/**
	 * 8. Finding redundant invocations
	 */
	@Test
	public void test08() {
		LinkedList mockedList = mock(LinkedList.class);
		
		mockedList.add("one");
		//mockedList.add("two");
		
		verify(mockedList).add("one");
		
		verifyNoMoreInteractions(mockedList);
	}

	@Mock private LinkedList mockedLinkedList;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * 9. Shorthand for mocks creation - @Mock annotation
	 */
	@Test
	public void test09() {
		mockedLinkedList.add("one");
		
		verify(mockedLinkedList).add("one");
	}

	/**
	 * 10. Stubbing consecutive calls (iterator-style stubbing)
	 */
	@Test
	public void test10() {
		LinkedList mockedList = mock(LinkedList.class);
		
		when(mockedList.add("one"))
			.thenThrow(new RuntimeException())
			.thenReturn(true);
		
		try {
			mockedList.add("one");
			fail();
		} catch(RuntimeException e) {
			// Do nothing.
		}
		
		assertTrue(mockedList.add("one"));
		//Any consecutive call: returns true as well (last stubbing wins).
		assertTrue(mockedList.add("one"));
	}

	/**
	 * 10. Stubbing consecutive calls (iterator-style stubbing)
	 */
	@Test
	public void test10_2() {
		LinkedList mockedList = mock(LinkedList.class);
		
		when(mockedList.add("one"))
			.thenReturn(true, false, true);
		
		assertTrue(mockedList.add("one"));
		assertFalse(mockedList.add("one"));
		assertTrue(mockedList.add("one"));
	}

	/**
	 * 11. Stubbing with callbacks
	 */
	@Test
	public void test11() {
		LinkedList mockedList = mock(LinkedList.class);
		
		when(mockedList.get(anyInt())).thenAnswer(new Answer() {
			public String answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				Object mock = invocation.getMock();
				return "called with arguments: " + Arrays.toString(args);
			}
		});
		
		assertThat((String)mockedList.get(0), is("called with arguments: [0]"));
		assertThat((String)mockedList.get(3), is("called with arguments: [3]"));
	}

	/**
	 * 12. doReturn()|doThrow()| doAnswer()|doNothing()|doCallRealMethod() family of methods
	 */
	@Test
	public void test12() {
		LinkedList mockedList = mock(LinkedList.class);
		
		doThrow(new RuntimeException()).when(mockedList).clear();

		try {
			mockedList.clear();
			fail();
		} catch(RuntimeException e) {
			// Do nothing.
		}
	}

	/**
	 * 13. Spying on real objects
	 */
	@Test
	public void test13() {
		List<Integer> list = new LinkedList<Integer>();
		List spy = Mockito.spy(list);
		
		when(spy.size()).thenReturn(100);
		
		// using the spy calls *real* methods
		spy.add("one");
		spy.add("two");
		
		assertThat((String)spy.get(0), is("one"));
		
		assertThat(spy.size(), is(100));
		
		verify(spy).add("one");
		verify(spy).add("two");
	}

	/**
	 * 14. Changing default return values of unstubbed invocations (Since 1.7)
	 */
	@Test
	public void test14() {
		Answer myAnswer = new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Method method = invocation.getMethod();
				Object[] args = invocation.getArguments();
				Object mock = invocation.getMock();
				
				if("toString".equals(method.getName())) {
					return "called with arguments: " + Arrays.toString(args);
				} else {
					return invocation.callRealMethod();
				}
			}
		};

		LinkedList mockedList1 = mock(LinkedList.class, Mockito.RETURNS_SMART_NULLS);
		LinkedList mockedList2 = mock(LinkedList.class, myAnswer);
		
		assertThat(mockedList1.toString().substring(0, 19), is("Mock for LinkedList"));
		assertThat(mockedList1.size(), is(0));
		assertThat(mockedList2.toString(), is("called with arguments: []"));
		assertThat(mockedList2.size(), is(0));
	}
	/**
	 * 15. Capturing arguments for further assertions (Since 1.8.0)
	 */
	@Test
	public void test15() {
		LinkedList mockedList = mock(LinkedList.class);
		mockedList.add("one");
		
		ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
		verify(mockedList).add(argument.capture());
		assertEquals("one", argument.getValue());
	}

	/**
	 * 16. Real partial mocks (Since 1.8.0)
	 */
	@Test
	public void test16() {
		//you can create partial mock with spy() method:    
		List list = spy(new LinkedList());

		//you can enable partial mock capabilities selectively on mocks:
		LinkedList mockedList = mock(LinkedList.class);
		//Be sure the real implementation is 'safe'.
		//If real implementation throws exceptions or depends on specific state of the object then you're in trouble.
		when(mockedList.add(anyString())).thenCallRealMethod();
		when(mockedList.size()).thenCallRealMethod();

		mockedList.add("one");
		assertThat(mockedList.size(), is(1));
		mockedList.clear();
		assertThat(mockedList.size(), is(1));
	}

	/**
	 * 17. Resetting mocks (Since 1.8.0)
	 */
	@Test
	public void test17() {
		List mock = mock(List.class);
		when(mock.size()).thenReturn(10);
		mock.add(1);
		
		reset(mock);
		
		assertThat(mock.size(), is(0));
	}

	/**
	 * 19. Aliases for behavior driven development (Since 1.8.0)
	 */
	@Test
	public void test19() {
		List mockedList = mock(LinkedList.class);

		// given
		given(mockedList.size()).willReturn(100);
		
		// when
		int size = mockedList.size();
		
		// then
		assertThat(size, is(100));
	}

	/**
	 * 20. Serializable mocks (Since 1.8.1)
	 */
	@Test
	public void test20() {
		List mockedList = mock(LinkedList.class, withSettings().serializable());

		List<Object> list = new ArrayList<Object>();
		List<Object> spy = mock(ArrayList.class, withSettings()
		                .spiedInstance(list)
		                .defaultAnswer(CALLS_REAL_METHODS)
		                .serializable());
	}
}
