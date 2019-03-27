package kr._19fstudy.effective_java.ch5.item33;

public class DatabaseClient {

	public static void main(String[] args) {
		Column<Integer> integerColumn = new Column<>(Integer.class);
		Column<String> stringColumn = new Column<>(String.class);

		DatabaseRow databaseRow = new DatabaseRow();
		databaseRow.putColumn(integerColumn, 3);
		databaseRow.putColumn(stringColumn, "3");

		System.out.println("The integer + 1: "+ (databaseRow.getColumn(integerColumn)+1));
		System.out.println("The string: "+ databaseRow.getColumn(stringColumn));

	}
}
