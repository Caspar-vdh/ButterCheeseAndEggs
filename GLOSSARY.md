# ButterCheeseAndEggs

A small reinforcement-learning library that learns to play two-player grid games (tic-tac-toe, connect four) through self-play, and then acts as an AI opponent.

## Language

**State**:
A board position as seen by the player to move, with cells recorded as mine, theirs or empty. In connect four, positions that are left-right mirror images of each other count as one State.
_Avoid_: Grid state, board id

**Action**:
A legal move from a State: a cell in tic-tac-toe, a column in connect four.
_Avoid_: Move index

**Weight**:
How strongly a State favours one of its Actions. A Weight of zero marks an illegal Action.
_Avoid_: Score, value

**Policy**:
All known States with their Weights for one game: what Training produces and what the AI opponent plays from.
_Avoid_: Model, training data, brain

**Training**:
Playing many games of AI against AI and adjusting Weights after each game.
_Avoid_: Learning session

**Reward**:
The change in Weights at the end of a game: an increase for the winner's Actions and a decrease for the loser's. Later Actions in the game get larger changes.
_Avoid_: Score

**Evaluation**:
Measuring how strong a Policy is by playing it against a random opponent without changing it. Win rates are reported separately for moving first and moving second.
_Avoid_: Benchmark, test run
