**Homework 4**

**Due: March 17th, 2020**

**100 points**

**Reviews: Knowledge Bases**

**Assignment**

For this assignment, you will build a cooperative AI to play the card
game *Hanabi*. The game will be entirely automated, so one copy of your
AI will play with another. (Note that the game is cooperative.) In
keeping with the cooperative spirit, you may optionally work with one
partner on this assignment; if you do so, your code files should clearly
indicate who worked on the project. Only one of you needs to upload
files to MyGCC.

I will provide code to enforce the basic rules of Hanabi, including
limited information about your own hand. You should only edit the
"Player" file; leave all other files as they are. (You are free to add
additional files where desired, however.)

Before the deadline, upload any files you modified to MyGCC. I'll insert
them into my project and run the resulting program one thousand times,
tracking your average score across those runs. You will be evaluated in
large part on the performance of your AI; for full credit, you should
average at least 15 points. For each additional full point that you
score, you will receive one point of extra credit on your next exam.
(For the record, the current average to beat is 20.1.)

I have extra copies of Hanabi available in my office. At any time,
you're welcome to check out a copy to practice strategy.

**Hanabi Rules Summary**

Hanabi is a cooperative card game played with a deck of fifty cards. The
deck is divided into five suits by color: Red, Green, Blue, Yellow, and
White. In each suit, there are cards of various numerical rank: three
1s, two 2s, two 3s, two 4s, and one 5.

The objective of Hanabi is to build five piles of cards, one for each of
the five suits, with each pile counting from 1 up to 5 in order; these
piles of cards are called the *tableau*. Barring a complete tableau, the
players want to complete as much as possible. Each player is dealt a
hand of five cards, which he plays onto the shared number piles.

The central twist of the game is that players hold their cards facing
away from themselves; thus, no player ever knows for sure what cards he
has available. Players *can* see their partners' hands, but are
forbidden from commenting on them except as detailed below. On his turn,
a player [must]{.underline} take [one]{.underline} of the following
three actions:

-   He can play a card from his hand to the tableau. To do so, the
    player touches one of his cards in hand, without looking at it,
    announces "Playing," and lays it down face-up. If the card can be
    legally added to the tableau, it is; otherwise, the players expend
    one of their three *fuses*. If they run out of fuses, the game ends
    in defeat.

    -   *Example: The tableau currently shows the following cards on top
        of their respective stacks: Green 3, Blue 2, Red 5, White 3.
        (The Yellow 1 has not yet been played, and so there is no yellow
        pile.) A play of Green 4 or Blue 3 is legal and would be placed
        on top of the appropriate pile. A play of Green 3 or Blue 4 is
        illegal and would expend a fuse.*

    -   If a player legally plays a 5, he also recovers a hint.

-   He can identify all cards in a partner's hand of a particular number
    or color. He could, for instance, indicate all 3s or all Blue cards
    in the hand. A player giving hints can only give [one]{.underline}
    hint, and he must indicate [all]{.underline} cards of the chosen
    number or color. Unfortunately, the players must spend a hint token
    in order to offer a hint, and they begin with only eight hint
    tokens.

    -   A player who discards a card for his action (below) may recover
        a hint token, again to a maximum of 8.

    -   A player who legally plays a 5 to the tableau may recover a hint
        token, again to a maximum of 8.

-   He can discard a card from his hand, again without looking at it
    until after declaring "Discard." Doing so allows the players to
    recover one hint token (again, to a maximum of 8). Discards are
    common knowledge, and players may sort through the discard pile at
    any time.

At the end of any turn in which a player's hand size dropped below 5, he
must draw back up to 5 cards if able. The game ends when one of three
conditions are met:

-   If the tableau is ever completed (all five suits at 5), the players
    win with a score of 25 (the maximum possible).

-   If the players spend all of their fuse tokens, they immediately
    lose, with a score of 0.

-   If the draw deck runs out, all players (including the one who drew
    the last card of the deck) receive one more turn. Then the game
    ends, and players score one point for each card in the tableau. (So
    if the tableau was Green 3, Red 2, Blue 5, White 4, Yellow 4, the
    players would score 18 points.)

**Code Base**

The base code to play Hanabi has been provided for you; you will only
need to implement the *Player.h* class (plus any supplemental classes
you want). A skeleton of this class has been provided for you; you
[must]{.underline} implement all provided functions.

It is recommended that players review the existing code base, especially
*GameConstants.h* and *Events.h*. You may also experiment with altering
main -- creating a Game-class object with the "chatty" parameter set to
*true* will provide much more information about what's happening when
the game runs.

Your Player class should maintain a knowledge base, tracking everything
you know about your own hand (for which you have partial information),
your partner's hand, and the discard pile (for both of which you have
perfect information). The main *Game* class will communicate with your
class by calling two functions, similar to those we discussed in class:

-   *Tell* allows the game to tell the player about some event that has
    just happened: a card discarded, a hint given, etc. (Note that
    players never communicate directly -- the game serves as an
    intermediary.) *Tell* takes the following arguments:

    -   The triggering Event.

    -   A vector of integers giving the highest card in each of the five
        columns, in order R B G Y W.

    -   The number of hints remaining.

    -   The number of fuses remaining.

    -   A vector of cards, the other player's hand.

    -   The number of cards remaining in the deck.

-   *Ask* requests an Event from the player: what do you want to do
    next?

Both *Ask* and *Tell* rely on the player either supplying or receiving
*Event* pointers, where *Event* is a multiclassed object describing
different things that can happen in-game:

-   The base class *Event* has a single private member, its *action*,
    which is one of SWAP (trade positions of two cards), PLAY (play a
    card), DISCARD (discard a card), COLOR\_HINT (tell the partner all
    cards of a particular color), NUMBER\_HINT (tell the partner all
    cards of a particular number), DRAW (reports a card draw), or
    NO\_OP. Players should only ever create hints for PLAY, DISCARD,
    SWAP, COLOR\_HINT, and NUMBER\_HINT.

-   A SwapEvent has two members, the two indices in the player's hand to
    be exchanged. Players creating a SwapEvent should indicate the two
    positions. Swaps are the only player-triggered event that does not
    end your turn; you may swap any number of times.

-   A DrawEvent is announced to a player whenever his partner draws a
    card. DrawEvents report the position to which the card went in the
    hand and the card that was drawn. Players will never take
    DrawEvents.

-   A DiscardEvent is taken by a player whenever he wishes to throw off
    a card. Players taking a DiscardEvent need only indicate the
    position of the card to be discarded. The Game will then report to
    *both* players a complete DiscardEvent including the exact card
    discarded, the position from which it was discarded, and whether the
    discard was made by you or your partner (*wasItThisPlayer* is true
    if you were the one to discard and false otherwise.) **Note:** When
    a DiscardEvent fires, the initial event will still report the
    discarded card as being in the player's hand (i.e., "Here are his
    five cards, and he's discarding \#3.") This will be followed by an
    event announcing his new hand.

-   A ColorHintEvent is taken by a player to indicate that he is hinting
    at a particular color. Players need only indicate the color; they do
    not need to list the positions of all relevant cards.
    ColorHintEvents are illegal if the other player has no cards of that
    color and/or if there are no hints remaining; either will terminate
    the game.

-   A NumberHintEvent is identical to a ColorHintEvent, except that it
    indicates a number rather than a color.

-   A PlayEvent is taken by a player whenever he chooses to play a card.
    Again, the player need only indicate the position of the card he
    wants to play (since he doesn't know what the card actually is!). As
    with DiscardEvents, both players will be *tell*ed a PlayEvent
    reporting what card was actually played as well as who played it.

Event pointers will be deleted by existing code; the player does not
need to manage memory for Events given to/received from the Game class.
