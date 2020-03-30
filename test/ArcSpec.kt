package parts.wisdom.arcweld

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.File

object ArcSpec : Spek({
    describe("loadTaskFromFile") {
        it("spot checks") {
            val file = File(arcHome, "data/training/0a938d79.json")
            val task = loadTaskFromFile(file)
        }
    }
})