package com.javacodegeeks.examples.rxjavaexample;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RxJavaExampleAdvancedTest {

	private static Logger logger = LoggerFactory.getLogger(RxJavaExampleAdvancedTest.class);

	@Test
	public void testSimpleAsync() throws InterruptedException {
		RxJavaExampleAdvanced.simpleAsync();
		Thread.sleep(3000);
	}

	@Test
	public void testSimpleAsyncMulti() throws InterruptedException {
		RxJavaExampleAdvanced.simpleAsyncMulti();
		Thread.sleep(3000);
	}
	
	@Test
	public void testSimpleAsyncArrayIo() throws InterruptedException {
		RxJavaExampleAdvanced.simpleArrayIo();
		Thread.sleep(3000);
	}
	
	@Test
	public void testSimpleAsyncArrayNewThread() throws InterruptedException {
		RxJavaExampleAdvanced.simpleArrayNewThread();
		Thread.sleep(3000);
	}
	
	@Test
	public void testSimpleAsyncEmmitted() {
		RxJavaExampleAdvanced.simpleAsyncWithEmitted();
	}
	
	@Test
	public void testSimpleAsyncAPICalls() throws InterruptedException {
		RxJavaExampleAdvanced.simpleAsyncAPICalls();
		Thread.sleep(3000);
	}
	
	@Test
	public void testMultipleAsyncAPICalls() throws InterruptedException {
		RxJavaExampleAdvanced.multipleAsyncAPICalls();
		Thread.sleep(3000);
	}
	
	@Test
	public void testMultipleAsyncAPICallsWithThreads() throws InterruptedException {
		RxJavaExampleAdvanced.multipleAsyncAPICallsWithThreads3();
		Thread.sleep(3000);
	}
	
	@Test
	public void testFlatMapAsyncAPICalls() throws InterruptedException {
		RxJavaExampleAdvanced.flatMapAsyncAPICalls();
		Thread.sleep(3000);
	}
	
	@Test
	public void testFlatMapAsyncAPICalls2() throws InterruptedException {
		RxJavaExampleAdvanced.flatMapAsyncAPICalls2();
		Thread.sleep(3000);
	}

	@Test
	public void testFlatMapAsyncAPICalls3() throws InterruptedException {
		RxJavaExampleAdvanced.flatMapAsyncAPICalls3();
		Thread.sleep(3000);
	}
	
	
	@Test
	public void testEventStreamsAPI2() throws InterruptedException, IOException, URISyntaxException {
		RxJavaExampleAdvanced.streamObserable2();
		Thread.sleep(6000);
	}
	

}
