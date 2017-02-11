package se.molybden.games.backgammon.manager

import se.molybden.games.backgammon.model.BackGammonGame

/**
 * Created by joachim on 2017-02-08.
 */
class Manager {
	static main(args) {
		//TODO: consider JMonkeyEngine for graphics
		def game = new BackGammonGame()
		game.play()
	}
}
