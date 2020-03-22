import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This is the only class you should edit.
 *
 * @author Noah Rosamilia
 * @author Ian Spryn
 */
public class Player {
    // Add whatever variables you want. You MAY NOT use static variables, or otherwise allow direct communication between
    // different instances of this class by any means; doing so will result in a score of 0.
    private List<Card> myCards;


    // Delete this once you actually write your own version of the class.
    private static Scanner scn = new Scanner(System.in);

    /**
     * This default constructor should be the only constructor you supply.
     */
    public Player() {
        myCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            myCards.add(new Card(-1, -1));
        }
    }

    /**
     * This method runs whenever your partner discards a card.
     *
     * @param startHand  The hand your partner started with before discarding.
     * @param discard    The card he discarded.
     * @param disIndex   The index from which he discarded it.
     * @param draw       The card he drew to replace it; null, if the deck is empty.
     * @param drawIndex  The index to which he drew it.
     * @param finalHand  The hand your partner ended with after redrawing.
     * @param boardState The state of the board after play.
     */
    public void tellPartnerDiscard(Hand startHand, Card discard, int disIndex, Card draw, int drawIndex,
                                   Hand finalHand, Board boardState) {
    }

    /**
     * This method runs whenever you discard a card, to let you know what you discarded.
     *
     * @param discard    The card you discarded.
     * @param boardState The state of the board after play.
     */
    public void tellYourDiscard(Card discard, Board boardState) {

    }

    /**
     * This method runs whenever your partner played a card
     *
     * @param startHand    The hand your partner started with before playing.
     * @param play         The card she played.
     * @param playIndex    The index from which she played it.
     * @param draw         The card she drew to replace it; null, if the deck was empty.
     * @param drawIndex    The index to which she drew the new card.
     * @param finalHand    The hand your partner ended with after playing.
     * @param wasLegalPlay Whether the play was legal or not.
     * @param boardState   The state of the board after play.
     */
    public void tellPartnerPlay(Hand startHand, Card play, int playIndex, Card draw, int drawIndex,
                                Hand finalHand, boolean wasLegalPlay, Board boardState) {
    }

    /**
     * This method runs whenever you play a card, to let you know what you played.
     *
     * @param play         The card you played.
     * @param wasLegalPlay Whether the play was legal or not.
     * @param boardState   The state of the board after play.
     */
    public void tellYourPlay(Card play, boolean wasLegalPlay, Board boardState) {

    }

    /**
     * This method runs whenever your partner gives you a hint as to the color of your cards.
     *
     * @param color       The color hinted, from Colors.java: RED, YELLOW, BLUE, GREEN, or WHITE.
     * @param indices     The indices (from 0-4) in your hand with that color.
     * @param partnerHand Your partner's current hand.
     * @param boardState  The state of the board after the hint.
     */
    public void tellColorHint(int color, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
        for (int index : indices) {
            myCards.set(index, new Card(color, myCards.get(index).value));
        }
    }

    /**
     * This method runs whenever your partner gives you a hint as to the numbers on your cards.
     *
     * @param number      The number hinted, from 1-5.
     * @param indices     The indices (from 0-4) in your hand with that number.
     * @param partnerHand Your partner's current hand.
     * @param boardState  The state of the board after the hint.
     */
    public void tellNumberHint(int number, ArrayList<Integer> indices, Hand partnerHand, Board boardState) {
        for (int index : indices) {
            myCards.set(index, new Card(myCards.get(index).color, number));
        }
    }

    /**
     * This method runs when the game asks you for your next move.
     *
     * @param yourHandSize How many cards you have in hand.
     * @param partnerHand  Your partner's current hand.
     * @param boardState   The current state of the board.
     * @return A string encoding your chosen action. Actions should have one of the following formats; in all cases,
     * "x" and "y" are integers.
     * a) "PLAY x y", which instructs the game to play your card at index x and to draw a card back to index y. You
     * should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
     * Illegal plays will consume a fuse; at 0 fuses, the game ends with a score of 0.
     * b) "DISCARD x y", which instructs the game to discard the card at index x and to draw a card back to index y.
     * You should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
     * Discarding returns one hint if there are fewer than the maximum number available.
     * c) "NUMBERHINT x", where x is a value from 1-5. This command informs your partner which of his cards have a value
     * of the chosen number. An error will result if none of his cards have that value, or if no hints remain.
     * This command consumes a hint.
     * d) "COLORHINT x", where x is one of the RED, YELLOW, BLUE, GREEN, or WHITE constant values in Colors.java.
     * This command informs your partner which of his cards have the chosen color. An error will result if none of
     * his cards have that color, or if no hints remain. This command consumes a hint.
     */
    public String ask(int yourHandSize, Hand partnerHand, Board boardState) {
        String action;

        action = shouldNotDisposeExists(partnerHand, boardState);
        if (action != null) return action;


        // Provided for testing purposes only; delete.
        // Your method should construct and return a String without user input.
        return scn.nextLine();
    }

    /**
     * Checks to see if there is a card that should not be disposed, starting with the lowest card
     *
     * @param partnerHand Your partner's current hand.
     * @param boardState  The current state of the board.
     * @return a string encoding a command if there is one
     */
    private String shouldNotDisposeExists(Hand partnerHand, Board boardState) {
        for (int cardNum = 1; cardNum <= 5; cardNum++) {
            int cardCount = cardCount(cardNum);
            for (int color = 0; color < 5; color++) {
                for (Card card : boardState.discards) {
                    //if the same card is in the discarded pile
                    if (card.color == color && card.value == cardNum)
                        //decrement the number of cards left to play
                        cardCount--;
                }
                //look through my own hand for the same card
                for (Card card : myCards)
                    //if the same card is in my hand
                    if (card.color == color && card.value == cardNum)
                        //decrement the number of cards left to play
                        cardCount--;
            }

            //if there is exactly 1 card left playable
            if (cardCount == 1) return "NUMBERHINT " + cardNum;
        }
        return null;
    }

    private int cardCount(int card) {
        if (card == 1) return 3;
        if (card < 5) return 2;
        return 1;
    }

}
