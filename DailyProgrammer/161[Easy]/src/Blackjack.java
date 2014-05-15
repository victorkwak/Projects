import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Simulates a blackjack game. The player enters how many decks are being played, and the player and dealer
 * are dealt two cards each. The dealer stops dealing when 20% of the deck remains, and the user is shown
 * the number of hands played, number of blackjacks dealt, and the percentage of the hands that were blackjacks.
 */
public class Blackjack {
    private static double numberOfHands = 0;
    private static double numberOfBlackjacks = 0;
    private static ArrayList<String> deck = new ArrayList<>();
    private static ArrayList<String> playerHand = new ArrayList<>();
    private static ArrayList<String> dealerHand = new ArrayList<>();

    public static void makeDeck(int numberOfDecks) {
        String[] cardNumber = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (int j = 0; j < numberOfDecks; j++) {
            for (int i = 0; i < 13; i++) {
                deck.add(cardNumber[i] + "☘");
            }
            for (int i = 0; i < 13; i++) {
                deck.add(cardNumber[i] + "♠");
            }
            for (int i = 0; i < 13; i++) {
                deck.add(cardNumber[i] + "♥");
            }
            for (int i = 0; i < 13; i++) {
                deck.add(cardNumber[i] + "♦");
            }
        }
        Collections.shuffle(deck);
    }

    public static void deal() {
        int index = 0;

        playerHand.add(0, deck.get(index));
        dealerHand.add(0, deck.get(++index));
        playerHand.add(1, deck.get(++index));
        dealerHand.add(1, deck.get(++index));

        if (cardsValue(dealerHand) > 16) {
            if (cardsValue(playerHand) < cardsValue(dealerHand)) {
                playerHand.add(2, deck.get(++index));
            }
            if (cardsValue(playerHand) < cardsValue(dealerHand)) {
                playerHand.add(3, deck.get(++index));
            }
            if (cardsValue(playerHand) < cardsValue(dealerHand)) {
                playerHand.add(4, deck.get(++index));
            }
            if (cardsValue(playerHand) < cardsValue(dealerHand)) {
                playerHand.add(5, deck.get(++index));
            }
        }

        if (cardsValue(dealerHand) < 17) {
//            dealerHand.add();
        }
        for (int i = 0; i < 4; i++) {
            deck.remove(i);
        }
        numberOfHands++;
    }

    public static int cardsValue(ArrayList<String> hand) {
        int[] cardValues = new int[hand.size()];
        for (int i = 0; i < cardValues.length; i++) {
            switch (hand.get(i).charAt(0)) {
                case '2':
                    cardValues[i] = 2;
                    break;
                case '3':
                    cardValues[i] = 3;
                    break;
                case '4':
                    cardValues[i] = 4;
                    break;
                case '5':
                    cardValues[i] = 5;
                    break;
                case '6':
                    cardValues[i] = 6;
                    break;
                case '7':
                    cardValues[i] = 7;
                    break;
                case '8':
                    cardValues[i] = 8;
                    break;
                case '9':
                    cardValues[i] = 9;
                    break;
                case '1':
                    cardValues[i] = 10;
                    break;
                case 'J':
                    cardValues[i] = 10;
                    break;
                case 'Q':
                    cardValues[i] = 10;
                    break;
                case 'K':
                    cardValues[i] = 10;
                    break;
                case 'A':
                    cardValues[i] = 11;
                    break;
            }
        }
        int total = 0;
        for (int cardValue : cardValues) {
            total += cardValue;
        }
        return total;
    }

    public static String checkValue(ArrayList<String> hand) {
        int cardsValue = cardsValue(hand);
        if (cardsValue == 21) {
            numberOfBlackjacks++;
            return String.valueOf(cardsValue) + " - Blackjack!";
        } else {
            return String.valueOf(cardsValue);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Number of decks: ");
        int numberOfDecks = scanner.nextInt();
        makeDeck(numberOfDecks);
        int numberOfCards = 52 * numberOfDecks;
        while (deck.size() > numberOfCards / 5) {
            deal();
            System.out.println(playerHand.get(0) + playerHand.get(1) + " " + checkValue(playerHand));
            System.out.println(dealerHand + " " + checkValue(dealerHand));
            System.out.println("");
        }
        System.out.println("Number of hands: " + (int) numberOfHands);
        System.out.println("Number of blackjacks: " + (int) numberOfBlackjacks);
        System.out.printf("Blackjack percentage: %.1f%%",
                (numberOfBlackjacks / numberOfHands) * 100);
    }
}