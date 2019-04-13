package kr._19fstudy.effective_java.ch8;

public class CardGame {

	public static class Card {
		private int rank;
		private String suit;

		public int getRank() {
			return rank;
		}

		public String getSuit() {
			return suit;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

		public void setSuit(String suit) {
			this.suit = suit;
		}
	}

	public void submit(Card card) {
		int rank = card.getRank();
		String suit = card.getSuit();

	}
}
