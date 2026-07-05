package ieee.cs.isik.platformergaeme.game.physics

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import ieee.cs.isik.platformergaeme.game.EntityFixtureData
import ieee.cs.isik.platformergaeme.game.FixtureData

class SolidConcatListener : ContactListener {

    override fun beginContact(contact: Contact?) {
        updateCollision(contact, true)
    }

    override fun endContact(contact: Contact?) {
        updateCollision(contact, false)
    }

    private fun updateCollision(contact: Contact?, colliding: Boolean) {
        val dataA = contact?.fixtureA?.userData
        val dataB = contact?.fixtureB?.userData

        if (dataA is FixtureData && dataA.type == FixtureData.Type.SOLID && dataB is EntityFixtureData) {
            dataB.entity.collidingSolid = colliding
        } else if (dataB is FixtureData && dataB.type == FixtureData.Type.SOLID && dataA is EntityFixtureData) {
            dataA.entity.collidingSolid = colliding
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}
}
