# ARC-raider

raiders of the new [Abstraction and Reasoning Corpus](https://github.com/fchollet/ARC)

## Approach Overview

Create a pile of [Wisdom](https://github.com/wisdom-parts/wisdom-kotlin) parts that implement the ARC priors.
Specifically, these "ARC parts" implement a DSL with two missions:

* Generate any ARC input grid. (Ensure complete coverage by including catch-all parts, such as an imp that stamps an 
  arbitrary grid pattern at an arbitrary location. But also regularize to penalize using these.)
  
* Transform any input-generation clan into the corresponding output-generation clan.
  
By randomly sampling the space of ARC clans, train deep neural nets to invert the operations performed by the
ARC parts:

* Given an ARC input grid, work backward to assemble an "input-generation" clan that emits it.

* Given an input-generation clan and an ARC output grid, work backward to assemble an "output-transformation" clan
  that maps the input-grid-generation clan into an "output-generation" clan that emits the output grid.
  
Also by sampling ARC clans, train a neural net to estimate the likelihood that a given candidate solution 
will give the correct answer when run against a given input-generation clan. Take into account the candidate's 
success rate across a few training pairs.
  
Solve a given ARC task as follows:

* For each training pair:
  * Assemble as many successful input-generation clans as feasible. 
    (At worst, there's always "emit the whole grid as a literal".)
  * For each input-generation clan:
    * Assemble as many successful output-transformation clans as feasible.
      (Just verify against this same "training pair of origin". We may not find any.)

* For each output-transformation clan that succeeded on its training pair of origin:
  * For each other training pair in the task:
    * For each input-generation clan for that training pair:
      * Test the output-transformation clan.
      
* For each output-transformation clan that succeeds on multiple training pairs (i.e. each "candidate transformation"):
  * For each test pair, assemble as many successful input-generation clans as feasible.
  * For each (input-generation clan, candidate transformation) pair (i.e. each "candidate solution"), estimate the
    likelihood it gives a correct answer.
      
* For the 3 most-likely-correct candidate solutions (3 being the number of trials allowed by the ARC rules), 
  construct our proposed solution by applying the output-transformation clan to the input-generation clan and 
  then using the resulting output-generation clan to construct an output grid.
        
## Example

Imagine a drastically simpler version of the ARC problem set where the only available abstraction is straight lines
that can be extended or shortened. Each line has a color, a starting point, a direction, and a length. 
Each grid has exactly one line. A line's direction is one of horizontal to the right, diagonal to the lower right, 
vertically down, etc. The direction cannot be determined from a single grid, but can be determined from an input/output 
pair of grids in which the line is lengthened or shortened in its direction. 

Imagine there are only four kinds of ARC tasks in this world: 
shorten / lengthen the line toward right-else-up or left-else-down.
Right-else-up the line's direction will be toward the right unless it is vertical, in which case upward.

In this world, our ARC solver's job is pretty simple:
* Recognize the line in a training input grid. (We only need a single example.)
* Compare to the line in the output grid.
* Determine whether the line was shortened or lengthened.
* Determine whether the line's direction kind is upper right or lower left.
* Recognize the line in the test input grid.
* Generate the output grid by shortening or lengthening the input line in the correct direction.

In this world, our imps might look something like this:

```kotlin
object X : Role<Int>()
object Y : Role<Int>()

open class Coords(vararg parts: Part) : Mix(*parts) {
    val x by X
    val y by Y 
}

enum class DeltaCoord { MINUS1, ZERO, PLUS1 }

object DeltaX : Role<DeltaCoord>()
object DeltaY : Role<DeltaCoord>()

open class DeltaCoords(vararg parts: Part) : Mix(*parts) {
    val deltaX by DeltaX
    val deltaY by DeltaY 
}

enum class Color { RED, GREEN, /* ... */ }

object TheColor : Role<Color>()

open class Line(vararg parts: Part) : Mix(*parts) {
    val start by StartCoords
    val direction by Direction
    val length by Length
    val color by TheColor

    fun draw(grid: ArcGrid): ArcGrid = TODO()
}

object TheLine : Role<Line>()

object Width : Role<Int>()
object Height : Role<Height>()

open class Grid(vararg parts: Part) : Mix(*parts) {
    val width by Width
    val height by Height
}

object TheGrid : Role<Grid>()

open class Generator(vararg parts: Part) : Mix(*parts) {
    val grid by TheGrid
    val line by TheLine

    fun make() = line.draw(grid.make())
}

enum class DirectionRule { RIGHT_ELSE_UP, DOWN_ELSE_LEFT }

object TheDirectionRule : Role<DirectionRule>() 

enum class LengthRule { SHORTEN, LENGTHEN }

object TheLengthRule : Role<LengthRule>() 

open class OutputTransformation(vararg parts: Part) : Mix(*parts) {
    val directionRule by TheDirectionRule
    val lengthRule by TheLengthRule

    fun transform(inputGenerator: Generator): Generator = TODO()
}
```

## Implementation Stack

* Kotlin
* [joy-data-kotlin](https://github.com/joy-prime/joy-data-kotlin)
* [Deep Java Library (DJL)](https://djl.ai/)  
* [wisdom-kotlin](https://github.com/wisdom-parts/wisdom-kotlin)

## Approach to the Main Implementation Challenges

### ARC Parts Generate an Input Grid

### ARC Parts Transform Input-Generation Clan into Output-Generation Clan

### DNN Examines Input Grid and Assembles ARC Parts

### DNN Examines Input-Generation Clan and Input Grid and Assembles ARC Parts

### DNN Examines Candidate Solution to Estimate Likelihood Correct
