Build Pipeline Refactoring Kata
===============================

Your task is to add a new feature - a new step in the build pipeline. If the existing tests pass, deploy to a staging environment and run smoke tests. Only if they succeed do you proceed to deploy to production. If there are no smoke tests, fail the pipeline and email the message "Pipeline failed - no smoke tests". In other cases be sure to add suitable log messages and include in the email which tests or deployment failed if any. 

Before you make changes to the code you will want to add some tests for the existing functionality. If you prefer to start with the refactoring, go to the 'with_tests' branch.


## Acknowledgements

This exercise was originally named "Untangled Conditionals Kata" and was designed by [Tom Oram](https://github.com/tomphp). I wanted to use it as a test design kata as well as a refactoring kata, so I removed the tests from the main branch and put them on the 'with_tests' branch instead.
