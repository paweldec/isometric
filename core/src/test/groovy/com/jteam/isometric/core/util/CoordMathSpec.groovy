package com.jteam.isometric.core.util

import com.badlogic.gdx.math.Vector2
import spock.lang.Specification
import spock.lang.Unroll

class CoordMathSpec extends Specification {

    Vector2 coord = new Vector2(4, 9)

    @Unroll
    def "should calculate coord from position"() {
        when:
            CoordMath.positionToCoord(position, resultCoord)
        then:
            (int)resultCoord.x == (int)targetCoord.x
            (int)resultCoord.y == (int)targetCoord.y
        where:
            position                  | resultCoord     | targetCoord
            new Vector2(96, 0)   |  new Vector2()  | new Vector2(1, 0)
            new Vector2(64, 32)  |  new Vector2()  | new Vector2(0, 1)
            new Vector2(128, 64) |  new Vector2()  | new Vector2(1, 3)
    }

    @Unroll
    def "should calculate position from coord"() {
        when:
            CoordMath.coordToPosition(coord, resultPosition)
        then:
            (int)resultPosition.x == (int)targetPosition.x
            (int)resultPosition.y == (int)targetPosition.y
        where:
            coord                      | resultPosition     | targetPosition
            new Vector2(1, 0)     | new Vector2()      | new Vector2(96, 0)
            new Vector2(0, 1)     | new Vector2()      | new Vector2(32, 16)
            new Vector2(1, 3)     | new Vector2()      | new Vector2(96, 48)
    }

}
