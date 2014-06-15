import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

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
        playerHand.add(0, deck.get(0));
        dealerHand.add(0, deck.get(1));
        playerHand.add(1, deck.get(2));
        dealerHand.add(1, deck.get(3));
        for (int i = 0; i < 4; i++) {
            deck.remove(i);
        }
        numberOfHands++;
    }

    public static int cardsValue(ArrayList<String> hand) {
        int[] cardValues = new int[2];
        for (int i = 0; i < 2; i++) {
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
        hand.clear();
        return cardValues[0] + cardValues[1];
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
        System.out.print("Number of Decks: ");
        int numberOfDecks = scanner.nextInt();
        makeDeck(numberOfDecks);
        int numberOfCards = 52 * numberOfDecks;
        while (deck.size() > numberOfCards / 5) {
            deal();
            System.out.println(playerHand + " " + checkValue(playerHand));
            System.out.println(dealerHand + " " + checkValue(dealerHand));
            System.out.println("");
        }
        System.out.println("Number of hands: " + (int) numberOfHands);
        System.out.println("Number of blackjacks: " + (int) numberOfBlackjacks);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        System.out.println("Blackjack Percentage: " +
                decimalFormat.format((numberOfBlackjacks / numberOfHands) * 100) + "%");
    }
}