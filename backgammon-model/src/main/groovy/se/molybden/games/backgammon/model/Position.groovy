package se.molybden.games.backgammon.model

/**
 * Created by joachim on 2017-02-08.
 */
interface Position {
	Color getColor()

	void checkDistance(Position to)
}