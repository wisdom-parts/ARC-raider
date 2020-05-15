# Roadmap

## Implement `ArcGrid` and related classes.

* Get these to the point where they can load the actual ARC data. (Done)

* Give them nice constructors for creating simpler examples by hand. (Dean)

* Problem editor GUI (Diego)

## Implement the imps for the simplified example ARC world described in the README. (Antonio)

## Integrate David -> Diego

## Pull Diego -> Antonio

## Write code that randomly generates entire problems.

* Random Generator clan -> creates a single input grid
  `Generator` has `fun (): ArcGrid`. (Antonio)

* Random Transformation clan -- converts the generator to create that single output grid
  (Assuming transformation is a `fun (Generator): Generator`.) (Antonio)

* Somehow vary the `Generator` and `Transformer` clans to create analogous problems for
  the other training and test pairs.
  
## Implement a DNN that solves the problem at the grid level

Goes from a whole problem statement (training pairs plus test input grid) to solution

### Integrate djl-kotlin (test: the Mnist example works) (David)

### Train the DNN using the random problem generator (Dean)

### Build the network. (everyone)



