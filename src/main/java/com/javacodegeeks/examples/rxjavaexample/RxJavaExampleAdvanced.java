package com.javacodegeeks.examples.rxjavaexample;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;
import rx.functions.Action1;
import rx.functions.Func1;

public class RxJavaExampleAdvanced {

	static int counter = 0;

	public static void simpleAsync() {
		Flowable.create((FlowableEmitter<String> s) -> {
			try {
				System.out.println("Executing async flowable.");
				Thread.sleep(1000);
				System.out.println("Finished async flowable.");
			} catch (Exception e) {
			}
			s.onComplete();
		}, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).subscribe();

		System.out.println("Print finished async method.");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void simpleAsyncWithEmitted() {
		Flowable.create((FlowableEmitter<String> s) -> {
			try {
				System.out.println("Executing async flowable.");
				Thread.sleep(1000);
				System.out.println("Finished async flowable.");
			} catch (Exception e) {
			}
			s.onNext("emitted");
			s.onComplete();
		}, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).subscribe(System.out::println);

		System.out.println("Print finished async method.");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void simpleAsyncAPICalls() {
		String test = "";
		System.out.println("Starting async api");
		Flowable.create(new FlowableOnSubscribe<String>() {
			@Override
			public void subscribe(FlowableEmitter<String> e) throws Exception {
				makeCall("http://localhost:8080/jcg/service/stream/no", test);
			}
		}, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io()).subscribe(System.out::println);

		System.out.println("Ending async api");

	}

	public static void flatMapAsyncAPICalls() {
		String test = "";
		System.out.println("Starting async api");

		Observable<String> result = Observable.fromArray("1", "2", "3");
		Observable<String> result2 = Observable.fromArray(returnList("http://localhost:8080/jcg/service/stream/no"));
		Observable<String> result4 = Observable.zip(result, result2, (s, s2) -> s + s2);

		Observable<Object> result5 = result4.flatMap((r) -> Observable.just(r.toString()));
		result5.subscribeOn(Schedulers.io()).subscribe(System.out::println);

	}

	@SuppressWarnings("unchecked")
	public static void flatMapAsyncAPICalls2() {
		String test = "";
		System.out.println("Starting async api");

		List<String> list = null;
		String[] strList = new String[0];
		list = makeCall("http://localhost:8080/jcg/service/stream/no/int/list");
		Observable<String> result = Observable.fromArray(list.toArray(strList));
		strList = makeCall("http://localhost:8080/jcg/service/stream/no/string/list", strList);

		Observable<String> result2 = Observable.fromArray(strList);
		Observable<String> result4 = Observable.zip(result, result2, (s, s2) -> s + s2);

		Observable<Object> result5 = result4.flatMap((r) -> Observable.just(r.toString()));
		result5.subscribeOn(Schedulers.io()).subscribe(System.out::println);

	}

	@SuppressWarnings("unchecked")
	public static void flatMapAsyncAPICalls3() {
		String test = "";
		System.out.println("Starting async api");

		List<String> list = null;
		String[] strList = new String[0];
		list = makeCall("http://localhost:8080/jcg/service/stream/no/int/list");
		Flowable<String> result = Flowable.fromArray(list.toArray(strList));
		strList = makeCall("http://localhost:8080/jcg/service/stream/no/string/list", strList);

		Flowable<String> result2 = Flowable.fromArray(strList);
		Flowable<String> result4 = Flowable.zip(result, result2, new BiFunction<String, String, String>() {
			@Override
			public String apply(String t1, String t2) throws Exception {
				System.out.println("Func: " + t1 + t2);
				return t1 + t2;
			}

		});

		result4.subscribe(new Consumer<String>() {

			@Override
			public void accept(String t) throws Exception {
				System.out.printf("Entry %s\n", t);
			}
		}, new Consumer<Throwable>() {
			@Override
			public void accept(Throwable t) throws Exception {
				System.err.printf("Failed to process: %s\n", t);
			}
		}, new Action() {
			@Override
			public void run() throws Exception {
				System.out.println("Done");
			}
		});

		Flowable<String> result5 = result4.flatMap((r) -> Flowable.just(r.toString()));
		result5.subscribe(new Consumer<String>() {

			@Override
			public void accept(String t) throws Exception {
				System.out.println("C-Entry: " + t);

			}

		});

		Subscriber<String> subscriber2 = new Subscriber<String>() {

			@Override
			public void onSubscribe(Subscription s) {
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(String t) {
				System.out.printf("S-Entry %s\n", t);
			}

			@Override
			public void onError(Throwable t) {
				System.err.printf("Failed to process: %s\n", t);
			}

			@Override
			public void onComplete() {
				System.out.println("Done");
			}

		};
		result5.subscribeOn(Schedulers.io()).subscribe(subscriber2);

		// Flowable.fromArray(1, 2, 3, 4).subscribe(i ->
		// System.out.printf("iEntry %d\n", i),
		// e -> System.err.printf("iFailed to process: %s\n", e), () ->
		// System.out.println("iDone"));

	}

	public static void streamObserable2() throws URISyntaxException, IOException, InterruptedException {
		System.out.println("---- executeStreamingViaObservableHttpWithForEach");
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		httpclient.start();

		// URL against
		// https://github.com/Netflix/Hystrix/tree/master/hystrix-examples-webapp
		// More information at
		// https://github.com/Netflix/Hystrix/tree/master/hystrix-contrib/hystrix-metrics-event-stream
		ObservableHttp
				.createRequest(HttpAsyncMethods.createGet("http://localhost:8080/jcg/service/stream/event2"),
						httpclient)
				// ObservableHttp.createRequest(HttpAsyncMethods.createGet("http://localhost:8989/hystrix-examples-webapp/hystrix.stream"),
				// client)
				.toObservable().flatMap(new Func1<ObservableHttpResponse, rx.Observable<String>>() {

					@Override
					public rx.Observable<String> call(ObservableHttpResponse response) {
						return response.getContent().map(new Func1<byte[], String>() {

							@Override
							public String call(byte[] bb) {
								System.out.println("timestamp inner "
										+ SimpleDateFormat.getDateTimeInstance().format(new Date().getTime()));
								System.out.println("counter: " + RxJavaExample3.counter++);
								return new String(bb);
							}

						});
					}
				}).buffer(5, TimeUnit.SECONDS, 5, rx.schedulers.Schedulers.io()).subscribeOn(rx.schedulers.Schedulers.io())
				.subscribe(new Action1<List<String>>() {

					@Override
					public void call(List<String> resp) {
						System.out.println(
								"timestamp " + SimpleDateFormat.getDateTimeInstance().format(new Date().getTime()));
						System.out.println(resp.toString());
					}
				});

		Thread.sleep(20000);
	}

	private static <T> T makeCall(String URI, T clazz) {
		RestTemplate restTemplate = new RestTemplate();
		T result = (T) restTemplate.getForObject(URI, clazz.getClass());
		// System.out.println(result.toString());
		return result;
	}

	private static List makeCall(String URI) {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new MappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(converters);
		Object[] result = restTemplate.getForObject(URI, Object[].class);
		// System.out.println(result.toString());
		return Arrays.asList(result);
	}

	private static String[] returnList(String URI) {
		return (String[]) Arrays.asList("test", "test1", "test2").toArray();

	}
}
