import java.util.*;
import java.util.stream.Collectors;

/**
 * This is the only class you should edit.
 *
 * @author Noah Rosamilia
 * @author Ian Spryn
 */
public class Player {

    private void debug(String str) {
        System.out.println(str);
    }

    class UnknownCard {
        private String[] colorsMap = new String[]{"R", "Y", "B", "G", "W"};
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

        public void setValue(int value) {
            possibleValues.clear();
            possibleValues.add(value);
        }

        public void setColor(int color) {
            possibleColors.clear();
            possibleColors.add(color);
        }

        public int maxValue() {
            return Collections.max(possibleValues);
        }

        public int minValue() {
            return Collections.min(possibleValues);
        }

        public int onlyValue() {
            if (possibleValues.size() > 1) return -1;
            return possibleValues.get(0);
        }

        public int onlyColor() {
            if (possibleColors.size() > 1) return -1;
            return possibleColors.get(0);
        }

        public String toString() {
            return possibleColors.stream().map((Integer c) -> colorsMap[c]).collect(Collectors.joining("")) +
                    " " + possibleValues.stream().map(Object::toString).collect(Collectors.joining(""));
        }
    }

    // Add whatever variables you want. You MAY NOT use static variables, or otherwise allow direct communication between
    // different instances of this class by any means; doing so will result in a score of 0.

    // Used to keep track of what I know
    public List<UnknownCard> myCards;
    // Used to keep track of what I know my partner knows
    private List<Card> partnerCards;
    //used to keep track of the single card that was hinted (which means it's immediately playable)
    private Queue<Integer> safeToPlay;


    // Delete this once you actually write your own version of the class.
    private static Scanner scn = new Scanner(System.in);

    /**
     * This default constructor should be the only constructor you supply.
     */
    public Player() {
        myCards = new ArrayList<>();
        partnerCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            myCards.add(new UnknownCard());
            partnerCards.add(new Card(-1, -1));
        }
        safeToPlay = new LinkedList<>();
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
        for (int i = 0; i < myCards.size(); i++) {
            if (indices.contains(i)) myCards.get(i).setColor(color);
            else myCards.get(i).possibleColors.remove(Integer.valueOf(color));
        }
//        // If true, then partner notified us of a single playable card
//        if (indices.size() == 1) {
//            safeToPlay.add(indices.get(0));
//            debug("SAFE TO PLAY LIST AFTER ADDING COLOR: " + safeToPlay.toString());
//        }

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
        for (int i = 0; i < myCards.size(); i++) {
            if (indices.contains(i)) myCards.get(i).setValue(number);
            else myCards.get(i).possibleValues.remove(Integer.valueOf(number));
        }
        // If true, then partner possibly notified us of a single playable card
        if (indices.size() == 1) {
            debug("GIVEN ONE ***NUMBER*** HINT");
            // Verify there exists at least one card on the table that is one less than the given number
            boolean canPlaceCard = false;
            for (int topCard : boardState.tableau) {
                debug("top card: " + topCard);
                if (topCard == number - 1) {
                    canPlaceCard = true;
                    break;
                }
            }
            if (canPlaceCard) {
                safeToPlay.add(indices.get(0));
                debug("SAFE TO PLAY LIST AFTER ADDING NUMBER: " + safeToPlay.toString());
            }
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

        // Update myCards with any new knowledge that can be inferred from what is available
        selfInferNewKnowledge(partnerHand, boardState);

        // Try to play a card
        action = selfCanPlayCard(myCards, boardState);
        if (action != null) return action;

        // Check if partner has a card they really shouldn't throw out
        action = partnerShouldNotDisposeCard(partnerHand, boardState);
        if (action != null) return action;

        // Check if partner has an identical card in color that is already on the table.
        // This helps avoid using up a fuse
        action = partnerShouldDisposeCard(partnerHand, boardState);
        if (action != null) return action;

        //Give a general hint
        action = partnerGiveHint(partnerHand, boardState);
        if (action != null) return action;

        // Try to dispose a card first to get back a hint if we know we absolutely can
        action = selfDisposeCard(myCards, boardState);
        if (action != null) return action;

        action = selfDisposeRandomCard(myCards, boardState, true);
        if (action != null) return action;

        action = gambleCard(myCards, boardState);
        if (action != null) return action;

        // Fallback. Get rid of a random card
        return selfDisposeRandomCard(myCards, boardState, false);

        // Provided for testing purposes only; delete.
        // Your method should construct and return a String without user input.
//        return scn.nextLine();
    }

    private int cardsLeft(int color, int value, Hand partnerHand, Board boardState) {
        int left = cardCount(value);
        for (int i = 0; i < partnerHand.size(); i++) {
            try {
                Card card = partnerHand.get(i);
                if (card.color == color && card.value == value) left--;
            } catch (Exception e) {
                // pass
            }
        }
        for (Card card : boardState.discards) {
            if (card.color == color && card.value == value) left--;
        }

        return left;
    }

    private void selfInferNewKnowledge(Hand partnerHand, Board boardState) {
        for (UnknownCard card : myCards) {
            if (card.onlyValue() != -1) {
                List<Integer> axe = new ArrayList<>();
                for (int color : card.possibleColors) {
                    if (cardsLeft(color, card.onlyValue(), partnerHand, boardState) == 0) {
                        axe.add(color);
                    }
                }
                card.possibleColors.removeAll(axe);
            }

            if (card.onlyColor() != -1) {
                List<Integer> axe = new ArrayList<>();
                for (int value : card.possibleValues) {
                    if (cardsLeft(card.onlyColor(), value, partnerHand, boardState) == 0) {
                        axe.add(value);
                    }
                }
                card.possibleValues.removeAll(axe);
            }
        }

        for (int cardIndex : safeToPlay) {
            UnknownCard card = myCards.get(cardIndex);
            for (int color = 0; color < 5; color++) {
                int topCardIndex = boardState.tableau.get(color);
                if (topCardIndex >= card.maxValue()) {
                    card.possibleColors.remove(Integer.valueOf(color));
                }
            }
        }
    }

    private String partnerShouldNotDisposeCard(Hand partnerHand, Board boardState) {
        if (boardState.numHints == 0) return null;
        for (int cardIndex = 0; cardIndex < partnerHand.size(); cardIndex++) {
            try {
                Card partnerCard = partnerHand.get(cardIndex);
                int cardCount = cardCount(partnerCard.value); //get the total number of cards that exist for this number

                // Check if a card lower is fully discarded first, which means we can't play this card
                for (int number = 1; number <= partnerCard.value; number++) {
                    int cardsAvailable = cardCount(number);
                    for (Card discard : boardState.discards) {
                        if (discard.color != partnerCard.color) continue;
                        if (discard.value == number)
                            cardsAvailable--;
                    }
                    if (cardsAvailable == 0) return null;
                }

                // If the same card is in the discarded pile
                for (Card discardCard : boardState.discards)
                    if (discardCard.equals(partnerCard))
                        // Decrement the number of cards left to play
                        cardCount--;
                // Look through my own hand for the same card
                boolean sharedCard = false;
                for (UnknownCard card : myCards) {
                    if (card.onlyValue() != -1 && card.onlyValue() == partnerCard.value &&
                            card.onlyColor() != -1 && card.onlyColor() == partnerCard.color) {
                        sharedCard = true;
                    }
                }

                // If there is exactly 1 card left playable and I don't have the card
                // and they don't already know what number this card is
                if (cardCount == 1 && !sharedCard && !partnerAlreadyKnowsNumber(partnerHand, partnerCard.value)) {
                    // Update my local version of what I know they know with the new value
                    updateLocalPartnerCardNumber(partnerHand, partnerCard.value);
                    debug("################# partnerShouldNotDisposeCard()");
                    return "NUMBERHINT " + partnerHand.get(cardIndex).value;
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
                    debug("################# enter partnerShouldDisposeCard()");
                    return "COLORHINT " + partnerCard.color;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String selfDisposeCard(List<UnknownCard> myCards, Board boardState) {
        if (boardState.deckSize < boardState.numFuses) return null; //we wanna gamble instead!
        //Do I have any cards that are already played on the table?
        for (int cardIndex = 0; cardIndex < myCards.size(); cardIndex++) {
            UnknownCard myCard = myCards.get(cardIndex);
            for (int color : myCard.possibleColors) {
                //if I contain a card that is the number or lower than what is already on the table
                if (myCard.maxValue() <= boardState.tableau.get(color)) {
                    myCards.remove(cardIndex);
                    if (boardState.deckSize > 1)
                        myCards.add(cardIndex, new UnknownCard());
                    debug("#################enter selfDisposeCard() - already on table");
                    return "DISCARD " + cardIndex + " " + cardIndex;
                }
            }
        }

        //Were any cards previously disposed such that now some cards are useless to have?
        for (int cardIndex = 0; cardIndex < myCards.size(); cardIndex++) {
            UnknownCard myCard = myCards.get(cardIndex);
            // Check for cards lower than mine that have been completely discarded.
            // If I Find something, then it's impossible to play myCard.
            for (int color : myCard.possibleColors) {
                for (int i = 1; i < myCard.minValue(); i++) {
                    Card card = new Card(color, i);
                    int cardCount = cardCount(i);
                    //Count the number of instances of a given card in the discard pile
                    for (Card discard : boardState.discards)
                        if (discard.equals(card))
                            cardCount--;
                    // If true, all instances of this card are discarded, so all cards higher than it don't matter
                    if (cardCount == 0) {
                        myCards.remove(cardIndex);
                        //if there's another available card to pull from, then "add" it to my local list of cards
                        if (boardState.deckSize > 1)
                            myCards.add(cardIndex, new UnknownCard());

                        debug("################# etner selfDisposeCard() - already discarded too much");
                        return "DISCARD " + cardIndex + " " + cardIndex;
                    }
                }
            }
        }
        return null;
    }

    private String selfCanPlayCard(List<UnknownCard> myCards, Board boardState) {
        // If a previous hint has pointed to a single card
        if (safeToPlay.size() > 0) {
            debug("Safe to play list: " + safeToPlay.toString());
            int num = safeToPlay.remove();
            myCards.remove(num);
            //if there's another available card to pull from, then "add" it to my local list of cards
            if (boardState.deckSize > 1)
                myCards.add(num, new UnknownCard());
            // Play it! And put the new card in its place
            debug("################# enter selfCanPlayCard() - SAFE TO PLAY");
            return "PLAY " + num + " " + num;
        }

        //Can I lay down a card on top of another (or start a new stack)?
        for (int cardIndex = 0; cardIndex < myCards.size(); cardIndex++) {
            UnknownCard myCard = myCards.get(cardIndex);
            // For a given color, if the table card is one less than what I have in my hand, PLAY IT
            boolean all = true;
            for (int color : myCard.possibleColors) {
                if (boardState.tableau.get(color) != myCard.onlyValue() - 1) {
                    all = false;
                }
            }
            if (all) {
                myCards.remove(cardIndex);
                //if there's another available card to pull from, then "add" it to my local list of cards
                if (boardState.deckSize > 1)
                    myCards.add(cardIndex, new UnknownCard());
                debug("################# enter selfCanPlayCard() - REGULAR");
                return "PLAY " + cardIndex + " " + cardIndex;
            }
        }
        return null;
    }

    private String partnerGiveHint(Hand partnerHand, Board boardState) {
        if (boardState.numHints == 0) return null;
        //Try to smartly give a hint. If we can hint such that only one card is pointed out, then GREAT! That's an instant play
        for (int cardIndex = 0; cardIndex < partnerHand.size(); cardIndex++) {
            try {
                Card partnerCard = partnerHand.get(cardIndex);
                // If partner has a playable card
                if (boardState.tableau.get(partnerCard.color) == partnerCard.value - 1) {
                    // Make sure that's the ONLY card (to make sure we're pointing to 1 card)
                    boolean unique = true;
                    for (int otherCardsIndex = 0; otherCardsIndex < partnerHand.size(); otherCardsIndex++) {
                        if (otherCardsIndex == cardIndex) continue;
                        if (partnerHand.get(otherCardsIndex).value == partnerCard.value)
                            unique = false;
                    }
                    if (unique) {
                        partnerCards.set(cardIndex, new Card(partnerCards.get(cardIndex).color, partnerCard.value));
                        debug("################# enter partnerGiveHint() SINGLE HINT");
                        return "NUMBERHINT " + partnerCard.value;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Try to find a hint that will point to the most number of cards possible
        int mostCommonNumber = 0;
        int greatestNumberCount = 0;
        int mostCommonColor = 0;
        int greatestColorCount = 0;
        for (int number = 1; number <= 5; number++) {
            int numberCount = 0;
            for (int cardIndex = 0; cardIndex < partnerHand.size(); cardIndex++) {
                try {
                    // If it's the same color and the partner doesn't know the number of the card
                    if (partnerHand.get(cardIndex).value == number
                            && partnerCards.get(cardIndex).value != number)
                        numberCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (numberCount > greatestNumberCount) {
                greatestNumberCount = numberCount;
                mostCommonNumber = number;
            }
        }
        for (int color = 0; color < 5; color++) {
            int colorCount = 0;
            for (int cardIndex = 0; cardIndex < partnerHand.size(); cardIndex++) {
                try {
                    // If it's the same color and the partner doesn't know the color of the card
                    if (partnerHand.get(cardIndex).color == color
                            && partnerCards.get(cardIndex).color != color) {
                        colorCount++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (colorCount > greatestColorCount) {
                greatestColorCount = colorCount;
                mostCommonColor = color;
            }
        }

        if (greatestNumberCount > 1 || greatestColorCount > 1) {
            // Prioritize number of color
            if (greatestNumberCount >= greatestColorCount) {
                updateLocalPartnerCardNumber(partnerHand, mostCommonNumber);
                debug("################# enter partnerGiveHint() MULTI-HINT NUMBER");
                return "NUMBERHINT " + mostCommonNumber;
            } else {
                updateLocalPartnerCardColor(partnerHand, mostCommonColor);
                debug("################# enter partnerGiveHint() MULTI-HINT COLOR");
                return "COLORHINT " + mostCommonColor;
            }
        }
        return null;
    }

    private String gambleCard(List<UnknownCard> myCards, Board boardState) {
        // Don't gamble if we have one fuse left
        if (boardState.numFuses == 1) return null;

        int index = new Random().nextInt(myCards.size());
        myCards.remove(index);
        if (boardState.deckSize > 1)
            myCards.add(index, new UnknownCard());
        debug("################# enter gambleCard()");
        return "PLAY " + index + " " + index;
    }

    //We tried to be smart earlier. We couldn't. Pick a random card.
    private String selfDisposeRandomCard(List<UnknownCard> myCards, Board boardState, boolean mightGamble) {
        if (mightGamble && boardState.deckSize < boardState.numFuses) return null; //we might wanna gamble instead!
        int index = new Random(Driver.seed).nextInt(myCards.size());
        myCards.remove(index);
        //if there's another available card to pull from, then "add" it to my local list of cards
        if (boardState.deckSize > 1)
            myCards.add(index, new UnknownCard());
        debug("################# enter selfDisposeRandomCard()");
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
