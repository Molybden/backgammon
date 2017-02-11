package se.molybden.games.backgammon.model.dice

import java.security.SecureRandom

import static java.lang.String.valueOf
import static java.lang.System.nanoTime
import static java.nio.charset.Charset.defaultCharset

/**
 * Created by joachim on 2017-02-08.
 */
class Die {
	Random random = new SecureRandom(valueOf(nanoTime()).getBytes(defaultCharset()))

	DieRoll roll() {
		new DieRoll(random.nextInt(5) + 1)
	}
}
