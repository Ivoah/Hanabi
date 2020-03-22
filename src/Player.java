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

    // Used to keep track of what I know
    private List<Card> myCards;
    // Used to keep track of what I know my partner knows
    private List<Card> partnerCards;
    //used to keep track of the single card that was hinted (which means it's immediately playable)
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

        // Update myCards with any new knowledge that can be inferred from what is available
        selfInferNewKnowledge();
        // Update partnerCards with any new knowledge that can be inferred from what is available
        partnerInferNewKnowledge();

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

    private void selfInferNewKnowledge() {
        //TODO
    }

    private void partnerInferNewKnowledge() {
        //TODO
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
                if (cardCount == 1 && !sharedCard && !partnerAlreadyKnowsNumber(partnerHand, partnerCard.value)) {
                    // Update my local version of what I know they know with the new value
                    updateLocalPartnerCardNumber(partnerHand, partnerCard.value);
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
                // If partner contains another card of the same color that is already on the table
                // and they don't already know what color color this card is but DO know its number
                if (partnerCard.value <= boardState.tableau.get(partnerCard.color)
                        && !partnerAlreadyKnowsColor(partnerHand, partnerCard.color)
                        && partnerAlreadyKnowsNumber(partnerHand, partnerCard.value)) {
                    //update my local version of what I know they know with the new color
                    updateLocalPartnerCardColor(partnerHand, partnerCard.color);
                    return "COLORHINT " + partnerCard.color;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String selfDisposeCard(List<Card> myCards, Board boardState) {
        //Do I have any cards that are already played on the table?
        for (int cardIndex = 0; cardIndex < myCards.size(); cardIndex++) {
            Card myCard = myCards.get(cardIndex);
            if (myCard.color == -1) continue;
            //if I contain a card that is the number or lower than what is already on the table
            if (myCard.value <= boardState.tableau.get(myCard.color)) {
                myCards.remove(myCard);
                if (boardState.deckSize > 1)
                    myCards.add(cardIndex, new Card(-1, -1));
                return "DISCARD " + cardIndex + " " + cardIndex;
            }
        }

        //Were any cards previously disposed such that now some cards are useless to have?
        for (int cardIndex = 0; cardIndex < myCards.size(); cardIndex++) {
            Card myCard = myCards.get(cardIndex);
            // Check for cards lower than mine that have been completely discarded.
            // If I Find something, then it's impossible to play myCard.
            for (int i = 1; i < myCard.value; i++) {
                Card card = new Card(myCard.color, i);
                int cardCount = cardCount(i);
                //Count the number of instances of a given card in the discard pile
                for (Card discard : boardState.discards)
                    if (discard.equals(card))
                        cardCount--;
                // If true, all instances of this card are discarded, so all cards higher than it don't matter
                if (cardCount == 0)
                    return "DISCARD " + cardIndex + " " + cardIndex;
            }
        }
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

        //Can I lay down a card on top of another (or start a new stack)?
        for (int cardIndex = 0; cardIndex < myCards.size(); cardIndex++) {
            Card myCard = myCards.get(cardIndex);
            if (myCard.color == -1 || myCard.value == -1) continue; // Incomplete information
            // For a given color, if the table card is one less than what I have in my hand, PLAY IT
            if (boardState.tableau.get(myCard.color) == myCard.value - 1) {
                myCards.remove(cardIndex);
                //if there's another available card to pull from, then "add" it to my local list of cards
                if (boardState.deckSize > 1)
                    myCards.add(cardIndex, new Card(-1, -1));
                return "PLAY " + cardIndex + " " + cardIndex;
            }
        }
        return null;
    }

    private String partnerGiveHint(Hand partnerHand, Board boardState) {
        //Try to smartly give a hint. If we can hint such that only one card is pointed out, then GREAT! That's an instant play
        //TODO
        return null;
    }

    //We tried to be smart earlier. We couldn't. Pick a random card.
    private String selfDisposeRandomCard(List<Card> myCards, Board boardState) {
        int index = new Random().nextInt(myCards.size());
        myCards.remove(index);
        //if there's another available card to pull from, then "add" it to my local list of cards
        if (boardState.deckSize > 1)
            myCards.add(index, new Card(-1, -1));
        return "DISCARD " + index + " " + index;
    }


    private int cardCount(int card) {
        if (card == 1) return 3;
        if (card < 5) return 2;
        return 1;
    }

    /**
     * Checks to see if my partner knows about all cards that are a certain number
     *
     * @param partnerHand their current hand
     * @param number      the number to check
     * @return true if they know all cards with the number; false otherwise
     */
    private boolean partnerAlreadyKnowsNumber(Hand partnerHand, int number) {
        for (int i = 0; i < partnerHand.size(); i++) {
            try {
                //Only look at the number that equals the parameter
                if (partnerHand.get(i).value != number) continue;
                if (partnerHand.get(i).value != partnerCards.get(i).value)
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Checks to see if my partner knows about all cards that are a certain color
     *
     * @param partnerHand their current hand
     * @param color       the color to check
     * @return true if they know all cards with the color; false otherwise
     */
    private boolean partnerAlreadyKnowsColor(Hand partnerHand, int color) {
        for (int i = 0; i < partnerHand.size(); i++) {
            try {
                //Only look at the color that equals the parameter
                if (partnerHand.get(i).color != color) continue;
                if (partnerHand.get(i).color != partnerCards.get(i).color)
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Called whenever I tell my partner a hint about a number. This method updates our local version of what I know
     * my partner knows.
     *
     * @param partnerHand their current hand
     * @param number      the number hint that was just given
     */
    private void updateLocalPartnerCardNumber(Hand partnerHand, int number) {
        for (int i = 0; i < partnerHand.size(); i++) {
            try {
                if (partnerHand.get(i).value == number)
                    partnerCards.set(i, new Card(partnerCards.get(i).color, number));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called whenever I tell my partner a hint about a color. This method updates our local version of what I know
     * my partner knows.
     *
     * @param partnerHand their current hand
     * @param color       their color hint that was just given
     */
    private void updateLocalPartnerCardColor(Hand partnerHand, int color) {
        {
            for (int i = 0; i < partnerHand.size(); i++) {
                try {
                    if (partnerHand.get(i).color == color)
                        partnerCards.set(i, new Card(color, partnerCards.get(i).value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
