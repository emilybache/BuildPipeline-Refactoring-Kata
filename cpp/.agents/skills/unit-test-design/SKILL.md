---
name: unit-test-design
description: Guide for designing unit tests
---

Stack replies with: ✅

* Use Catch2
* Structure the test into three parts: Arrange, Act, Assert.
* Do not separate sections with comments like `// Arrange`, use a blank line either side of the Act step.
* Test names should include both the scenario being tested and expected result like this `ScenarioSummary_ExpectedResult`
