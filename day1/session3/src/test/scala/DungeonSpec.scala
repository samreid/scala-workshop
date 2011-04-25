package dung.eon

import org.specs._
import org.scalacheck._

class DungeonSpec extends Specification {
  "a fireball" should {
    "match" in {
      val fireball: Magic = Fire(Some(3), 3)
      fireball must beLike {
        case Fire(attack, range) => attack must beSome(3); range must be(3)
      }
    }
  }
}

class MapSpec extends Specification with ScalaCheck {
  "a randomly generated map" should {
    "be fully connected" in {
      import Gen._
      import Arbitrary.arbitrary
      implicit val arbLoc  = Arbitrary(for (name <- arbitrary[String] suchThat (_.length > 3)) yield Location(name, Nil, Abandoned))
      implicit val arbLocs = Arbitrary(for (n <- choose(5, 50); locs <- Gen.listOfN(n, arbitrary[Location])) yield locs)

      val prop = Prop.forAll(
        (l: List[Location]) => {
          val map = DungeonMap.generate(l)
          l.forall(
            loc => map.exits(loc) must exist {
              case Exit(to, _) => map.exits(to) must exist {
                case Exit(from, _) => from == loc
              }
            }
          )
        }
      )

      prop must pass
    }
  }
}


// vim: set ts=4 sw=4 et:
