package se.molybden.games.backgammon.model

import org.junit.Test
import se.molybden.games.backgammon.model.exceptions.HitException
import se.molybden.games.backgammon.model.exceptions.OccupiedByOtherColorException
import spock.lang.Specification

import static se.molybden.games.backgammon.model.Color.BLACK
import static se.molybden.games.backgammon.model.Color.WHITE

/**
 * Created by joachim on 2017-02-08.
 */
class PointTest extends Specification {
	@Test
	void shouldAddWithExceptionToHolderWithDifferentColor() throws Exception {
		given:
		def holder = new Point()
		holder.add(new Checker(WHITE))
		holder.add(new Checker(WHITE))
		when:
		holder.add(new Checker(BLACK))
		then:
		thrown OccupiedByOtherColorException.class
	}

	@Test
	void shouldGenerateHit() throws Exception {
		given:
		def holder = new Point()
		def theWhiteChecker = new Checker(WHITE)
		holder.add(theWhiteChecker)
		when:
		holder.add(new Checker(BLACK))
		then:
		HitException hit = thrown()
		hit.checker == theWhiteChecker
	}

	@Test
	void shouldAddAndRemoveSeveralTimesThenThrowExceptionAddingWrongColor() throws Exception {
		given:
		def holder = new Point()
		when:
		holder.add(new Checker(WHITE))
		holder.remove()
		holder.add(new Checker(WHITE))
		holder.add(new Checker(WHITE))
		holder.add(new Checker(WHITE))
		holder.remove()
		holder.add(new Checker(BLACK))
		then:
		thrown OccupiedByOtherColorException.class
	}

	@Test
	void shouldAddAndRemoveSeveralTimesThenOKAddingOtherColorIfEmpty() throws Exception {
		given:
		def holder = new Point()
		when:
		holder.add(new Checker(WHITE))
		holder.remove()
		holder.add(new Checker(WHITE))
		holder.add(new Checker(WHITE))
		holder.remove()
		holder.remove()
		holder.add(new Checker(BLACK))
		then:
		!holder.empty
	}

}