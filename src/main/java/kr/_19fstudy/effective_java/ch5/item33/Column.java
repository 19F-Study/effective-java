package kr._19fstudy.effective_java.ch5.item33;

public class Column<T> {

	private final Class<T> type;

	public Column(Class<T> type) {
		this.type = type;
	}

	public T cast(Object o) {
		return o == null ? null : type.cast(o);
	}
}
