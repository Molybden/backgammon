package se.molybden.games.backgammon.model

import se.molybden.games.backgammon.model.dice.Dice
import se.molybden.games.backgammon.model.dice.Die
import se.molybden.games.backgammon.model.dice.DieRoll

import static se.molybden.games.backgammon.model.Color.BLACK
import static se.molybden.games.backgammon.model.Color.WHITE

/**
 * Created by joachim on 2017-02-11.
 */
class BackGammonGame {
	Board board = new Board()

	Map<Color, Player> players = new HashMap<>()
	Dice dice = new Dice()

	void play() {
		initPlayers()
		Color color
		Tuple roll
		while (color == null) {
			DieRoll whiteStart = new Die().roll()
			DieRoll blackStart = new Die().roll()
			if (whiteStart.value > blackStart.value) {
				color = WHITE
			} else if (whiteStart.value > blackStart.value) {
				color = BLACK
			} else {
				println "Equal, re-roll"
			}
			roll = new Tuple(whiteStart, blackStart)
		}
		println "${color} starts"
		while (!board.confirmedBoardState.hasWinner()) {
			println board
			println "Player ${color} up next."
			players.get(color).makeMoves(board, roll)
			board.confirmState()
			roll = dice.roll()
			color = change(color)
		}
		println "We have a winner: " + board.confirmedBoardState.winner
	}

	private Color change(Color color) {
		color == WHITE ? BLACK : WHITE
	}

	def initPlayers() {
		players.put(BLACK, new Player(BLACK))
		players.put(WHITE, new Player(WHITE))
	}
}
