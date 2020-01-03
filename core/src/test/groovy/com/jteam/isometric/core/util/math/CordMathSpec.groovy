package com.jteam.isometric.core.util.math

import com.badlogic.gdx.math.Vector2
import spock.lang.Specification
import spock.lang.Unroll

class CordMathSpec extends Specification {

    @Unroll
    def "should calculate cord from position"() {
        when:
            CordMath.positionToCord(position, resultCoord)
        then:
            (int)resultCoord.x == (int)targetCoord.x
            (int)resultCoord.y == (int)targetCoord.y
        where:
            position             | resultCoord     | targetCoord
            new Vector2(96, 16)  |  new Vector2()  | new Vector2(1, 0)
            new Vector2(70, 20)  |  new Vector2()  | new Vector2(0, 1)
            new Vector2(130, 70) |  new Vector2()  | new Vector2(1, 3)
    }

    @Unroll
    def "should calculate center position of tile from cord"() {
        when:
            CordMath.cordToPosition(coord, resultPosition)
        then:
            (int)resultPosition.x == (int)targetPosition.x
            (int)resultPosition.y == (int)targetPosition.y
        where:
            coord                 | resultPosition     | targetPosition
            new Vector2(1, 0)     | new Vector2()      | new Vector2(96, 16)
            new Vector2(0, 1)     | new Vector2()      | new Vector2(64, 32)
            new Vector2(1, 3)     | new Vector2()      | new Vector2(128, 64)
    }

}
