# bm1121
Demo:

To run the tests for the Demo project, run the JUnit Test suit provided. 
These tests cover all things required by the Demo project specifications. 

Here are some extra things done, as part of future proofing the design.

1. Ability to specify different daily rates for weekdays, weekends, and holidays. This might be a need in future. This ability is covered by the provided tests. 
2. Ability to checkout multiple tools at one session.
3. Ability to specify Tool data via external file (tool-inventory.txt) so that other people can build inventory of available tools.
4. Ability to manage global settings (date format would be a candidate, for example) via a configuration file (tool-rental-config.txt).
 
Item#3 and #4 are not yet integrated with the checkout part. The idea was to verify if a Tool that was ordered actually exists in the inventory.

For #3 and #4, loading resources from files is implemented and can be tested by running the main method in the ToolRentalAppConfig.java file.



