package kr._19fstudy.effective_java.ch7.item46;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Album {
	private String name;
	private int sales;

	public Album(String name, int sales) {
		this.name = name;
		this.sales = sales;
	}

	public static void main(String[] args) {

		List<Album> albums = List.of(new Album("test1", 10)
			, new Album("test2", 20)
			, new Album("test3", 100));

		Optional<Album> topHitAlbum = albums.stream().collect(Collectors.maxBy(Comparator.comparing(Album::getSales)));
		System.out.println(topHitAlbum.map(Album::getName).orElse("empty..."));

		System.out.println(albums.stream().map(Album::getName).collect(Collectors.joining(",")));

	}

	public String getName() {
		return name;
	}

	public int getSales() {
		return sales;
	}

}
