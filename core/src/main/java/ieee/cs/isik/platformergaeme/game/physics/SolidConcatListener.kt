package ieee.cs.isik.platformergaeme.game.physics

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import ieee.cs.isik.platformergaeme.game.EntityFixtureData
import ieee.cs.isik.platformergaeme.game.FixtureData

class SolidConcatListener : ContactListener {

    override fun beginContact(contact: Contact?) {
        updateCollision(contact, 1)
    }

    override fun endContact(contact: Contact?) {
        updateCollision(contact, -1)
    }

    private fun updateCollision(contact: Contact?, change: Int) {
        val dataA = contact?.fixtureA?.userData
        val dataB = contact?.fixtureB?.userData

        if (dataA is FixtureData && dataA.type == FixtureData.Type.SOLID && dataB is EntityFixtureData) {
            dataB.entity.solidContacts += change
        } else if (dataB is FixtureData && dataB.type == FixtureData.Type.SOLID && dataA is EntityFixtureData) {
            dataA.entity.solidContacts += change
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}
}
