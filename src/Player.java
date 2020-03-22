import java.util.*;

/**
 * This is the only class you should edit.
 *
 * @author Noah Rosamilia
 * @author Ian Spryn
 */
public class Player {
    class UnknownCard {
		public ArrayList<Integer> possibleValues = new ArrayList<>();
		public ArrayList<Integer> possibleColors = new ArrayList<>();

		public UnknownCard() {
			possibleValues.add(1);
			possibleValues.add(2);
			possibleValues.add(3);
			possibleValues.add(4);
			possibleValues.add(5);

			possibleColors.add(Colors.RED);
			possibleColors.add(Colors.YELLOW);
			possibleColors.add(Colors.BLUE);
			possibleColors.add(Colors.GREEN);
			possibleColors.add(Colors.WHITE);
		}
	}
    // Add whatever variables you want. You MAY NOT use static variables, or otherwise allow direct communication between
    // different instances of this class by any means; doing so will result in a score of 0.
    private List<Card> myCards;
    // Used to keep track of what I know my partner knows
    private List<Card> partnerCards;
    //used to keep track of the single card that was hinted (which means it's playable)
    private int singleCardHintIndex;


    // Delete this once you actually write your own version of the class.
    private static Scanner scn = new Scanner(System.in);

    /**
     * This default constructor should be the only constructor you supply.
     */
    public Player() {
        singleCardHintIndex = -1;
        myCards = new ArrayList<>();
        partnerCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            myCards.add(new Card(-1, -1));
            partnerCards.add(new Card(-1, -1));
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
        partnerCards.remove(disIndex);
        if (draw != null)
            partnerCards.add(drawIndex, new Card(-1, -1));
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
        partnerCards.remove(playIndex);
        if (draw != null) {
            partnerCards.add(drawIndex, new Card(-1, -1));
        }

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
        // If true, then partner notified us of a single playable card
        if (indices.size() == 1) singleCardHintIndex = indices.get(0);

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
        // If true, then partner notified us of a single playable card
        if (indices.size() == 1) singleCardHintIndex = indices.get(0);
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

        // Check if partner has a card they really shouldn't throw out
        action = partnerShouldNotDisposeCard(partnerHand, boardState);
        if (action != null) return action;

        // Check if partner has an identical card in color that is already on the table.
        // This helps avoid using up a fuse
        action = partnerShouldDisposeCard(partnerHand, boardState);
        if (action != null) return action;

        // Try to dispose a card first to get back a hint if we know we absolutely can
        action = selfDisposeCard(myCards, boardState);
        if (action != null) return action;

        // Try to play a card
        action = selfCanPlayCard(myCards, boardState);
        if (action != null) return action;

        //Give a general hint
        action = partnerGiveHint(partnerHand, boardState);
        if (action != null) return action;

        // Fallback. Get rid of a random card
        return selfDisposeRandomCard(myCards, boardState);


        // Provided for testing purposes only; delete.
        // Your method should construct and return a String without user input.
//        return scn.nextLine();
    }

    private String partnerShouldNotDisposeCard(Hand partnerHand, Board boardState) {
        if (boardState.numHints == 0) return null;
        for (int i = 0; i < partnerHand.size(); i++) {
            try {
                Card partnerCard = partnerHand.get(i);
                int cardCount = cardCount(partnerCard.value); //get the total number of cards that exist for this number
                boolean sharedCard = false;

                // If the same card is in the discarded pile
                if (boardState.discards.contains(partnerCard))
                    // Decrement the number of cards left to play
                    cardCount--;
                // Look through my own hand for the same card
                sharedCard = myCards.contains(partnerCard);

                // If there is exactly 1 card left playable and I don't have the card
                // and they don't already know what number this card is
                if (cardCount == 1 && !sharedCard && !partnerAlreadyKnowsNumber(partnerCard, i)) {
                    // Update my local version of what I know they know with the new value
                    updateLocalPartnerCardNumber(i, partnerCard.value);
                    return "NUMBERHINT " + partnerHand.get(i).value;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private String partnerShouldDisposeCard(Hand partnerHand, Board boardState) {
        if (boardState.numHints == 0) return null;
        for (int i = 0; i < partnerHand.size(); i++) {
            try {
                Card partnerCard = partnerHand.get(i);
                // If partner contains another card of the same color that is already on top of a stack on the table
                // and they don't already know what color color this card is
                if (partnerCard.value == boardState.tableau.get(partnerCard.color) && !partnerAlreadyKnowsColor(partnerCard, i))
                    //update my local version of what I know they know with the new color
                    updateLocalPartnerCardColor(i, partnerCard.color);
                return "COLORHINT " + partnerCard.color;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String selfDisposeCard(List<Card> myCards, Board boardState) {
        //Are there any duplicate cards?
        //Do I have any cards that are lower than what is currently on the board for a given color?
        //Were any cards previously disposed such that now it some cards are useless to have?
        //TODO
        return null;
    }

    private String selfCanPlayCard(List<Card> myCards, Board boardState) {
        // If a previous hint has pointed to a single card
        if (singleCardHintIndex != -1) {
            int num = singleCardHintIndex;
            singleCardHintIndex = -1; //reset
            myCards.remove(num);
            //if there's another available card to pull from, then "add" it to my local list of cards
            if (boardState.deckSize > 1)
                myCards.add(num, new Card(-1, -1));
            // Play it! And put the new card in its place
            return "PLAY " + num + " " + num;
        }
        // Otherwise, check to see if we can infer anything else to play

        //Can I lay down a card on top of another (or start a new stack)?
        //TODO

        return null;
    }

    private String partnerGiveHint(Hand partnerHand, Board boardState) {
        //Try to smartly give a hint. If we can hint such that only one card is pointed out, then GREAT! That's an instant play
        //TODO
        return null;
    }

    private String selfDisposeRandomCard(List<Card> myCards, Board boardState) {
        //Try to be smart. If we can't, pick randomly
        //TODO
        return null;
    }


    private int cardCount(int card) {
        if (card == 1) return 3;
        if (card < 5) return 2;
        return 1;
    }

    private boolean partnerAlreadyKnowsNumber(Card partnerCard, int index) {
        return partnerCard.value == partnerCards.get(index).value;
    }

    private boolean partnerAlreadyKnowsColor(Card partnerCard, int index) {
        return partnerCard.color == partnerCards.get(index).color;
    }

    private void updateLocalPartnerCardNumber(int index, int number) {
        partnerCards.set(index, new Card(partnerCards.get(index).color, number));
    }

    private void updateLocalPartnerCardColor(int index, int color) {
        partnerCards.set(index, new Card(color, partnerCards.get(index).value));
    }
}
