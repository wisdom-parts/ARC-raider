# arcweld

When life gives you an [Abstraction and Reasoning Corpus](https://github.com/fchollet/ARC), make a weld!

## Approach

Create a pile of [Wisdom](https://github.com/wisdom-parts/wisdom-kotlin) parts that implement the ARC priors.
Specifically, these "ARC parts" implement a DSL with two missions:

  * Generate any ARC input grid.
  * Transform any input-generation clan into the corresponding output-generation clan.
  
Ensure complete coverage by including catch-all parts, such as an imp that stamps an 
arbitrary grid pattern at an arbitrary location. But also regularize to penalize using these.
  
By randomly sampling the space of ARC clans, train deep neural nets to invert the operations performed by the
ARC parts:
* Given an ARC input grid, work backward to assemble an "input-generation" clan that emits it.
* Given an input-generation clan and an ARC output grid, work backward to assemble an "output-transformation" clan
  that maps the input-grid-generation clan into an "output-generation" clan that emits the output grid.
  
Also by sampling ARC clans, train a neural net to estimate the likelihood a given candidate solution 
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
        
## Implementation Stack

* Kotlin
* [joy-data-kotlin](https://github.com/joy-prime/joy-data-kotlin)
* [wisdom-kotlin](https://github.com/wisdom-parts/wisdom-kotlin)
* [Deep Java Library (DJL)](https://djl.ai/)  
