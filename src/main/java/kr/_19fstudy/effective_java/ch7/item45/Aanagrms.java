package kr._19fstudy.effective_java.ch7.item45;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Aanagrms {

	private static String filePath = "/Users/coupang/study/effective-java/src/main/java/kr/_19fstudy/effective_java/ch7/item45/dictionary";
	private static Integer minGroupSize = 2;

	public static void main(String[] args) throws FileNotFoundException {
		computeIfAbsentVersion();


	}

	//java8 에 추가된 computeIfAbsent 를 사용한 버전.
	//각 키에 다수의 값을 매핑하는 맵을 쉽게 구현할 수 있다.
	private static void computeIfAbsentVersion() throws FileNotFoundException {
		File dictionary = new File(filePath);

		Map<String, Set<String>> groups = new HashMap<>();
		try (Scanner s = new Scanner(dictionary)) {
			while(s.hasNext()) {
				String word = s.next();
				//computeIfAbsent 내부 구현 살펴보기
				groups.computeIfAbsent(alphabetize(word), (unused) -> new TreeSet<>()).add(word);
			}
		}

		for (Set<String> group : groups.values()) {
			if (group.size() >= minGroupSize) {
				System.out.println(group.size() + ": " + group);
			}
		}
	}

	//한줄 한줄 독해 해보기
	private static void overUsingStreamVersion() throws IOException {
		Path dictionary = Paths.get(filePath);

		try (Stream<String> words = Files.lines(dictionary)) {
			words.collect(
				groupingBy(
					word -> word
						.chars()
						.sorted()
						.collect(StringBuilder::new, (sb, c) -> sb.append((char) c), StringBuilder::append).toString())
			).values()
				.stream()
				.filter(group -> group.size() >= minGroupSize)
				.map(group -> group.size() + ": " + group)
				.forEach(System.out::println);
		}
	}

	private static void properUsingStreamVersion() throws IOException {
		Path dictionary = Paths.get(filePath);

		try (Stream<String> words = Files.lines(dictionary)) {
			words.collect(groupingBy(Aanagrms::alphabetize))
				.values().stream()
				.filter(group -> group.size() >= minGroupSize)
				.forEach(g -> System.out.println(g.size() + ": " + g));
		}
	}

	//Stream 을 이용해서 구현할 수 있지만 char용 스트림을 지원하지 않기 때문에 잘 못 구현할 가능성이 크다.
	private static String alphabetize(String s) {
		char[] a = s.toCharArray();
		Arrays.sort(a);
		return new String(a);
	}
}
