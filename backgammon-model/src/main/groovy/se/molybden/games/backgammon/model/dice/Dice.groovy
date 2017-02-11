package se.molybden.games.backgammon.model.dice

/**
 * Created by joachim on 2017-02-11.
 */
class Dice {
	Die die1 = new Die()
	Die die2 = new Die()

	Tuple roll() {
		new Tuple(die1.roll(), die2.roll())
	}
}
