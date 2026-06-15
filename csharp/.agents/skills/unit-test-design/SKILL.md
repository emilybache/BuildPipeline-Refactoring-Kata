---
name: unit-test-design
description: Guide for designing unit tests
---

Stack replies with: ✅

* Use NUnit
* Structure the test into three parts: Arrange, Act, Assert. 
* Do not separate sections with comments like `// Arrange`, use a blank line either side of the Act step.
* Test names should include both the scenario being tested and expected result like this `ScenarioSummary_ExpectedResult`
* When there is more than one assertion use one of these strategies:
  1) Use Constraint based assertions and group them with Assert.Multiple.
  2) Use a Printer together with Verify to make an Approval test.
