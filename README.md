# End-to-End Test Automation for Amazon Use Case

## Project Overview
This project automates an end-to-end integration test case for an e-commerce platform (Amazon) using Selenium WebDriver. It tests a user scenario of searching for an item, selecting specific attributes (like color), and adding the item to the cart. The automation ensures efficient test execution with data-driven capabilities, logging, and robust error handling.

## Key Features
- **Data-Driven Testing**: Dynamically loads test data (item details like name, type, color, price) from an Excel file using Apache POI.
- **Screenshots**: Captures key steps during the test for debugging and reporting purposes.
- **Logging**: Uses Log4j for detailed logging of each test step.
- **Assertions**: Validates outcomes using TestNG assertions to ensure correctness.
- **Efficient Waits**: Handles dynamic elements using WebDriverWait and JavaScript Executor.
- **Reusable Code**: Modular methods for better maintainability and scalability.

## Tools and Frameworks
- **Selenium WebDriver**: For browser automation.
- **TestNG**: For test case management and annotations.
- **Log4j**: For logging test steps and outcomes.
- **Apache POI**: For reading test data from Excel files.
- **Maven**: For dependency management and project building.

## Prerequisites
1. Java installed with Maven configured.
2. Firefox browser with Geckodriver.
3. Test data file (`data.xlsx`) containing item details.
4. Log4j configuration file for logging setup.
5. Selenium WebDriver and related dependencies.

## Test Case Workflow
1. **Setup**:
   - Initializes WebDriver, loads test data from `data.xlsx`, and navigates to the Amazon homepage.
2. **Search for Item**:
   - Inputs item details into the search bar and clicks the search button.
3. **Select Item**:
   - Selects the correct product from the search results and handles attributes like color.
4. **Add to Cart**:
   - Clicks the "Add to Cart" button and manages popups (e.g., warranty selection).
5. **Teardown**:
   - Closes the browser and cleans up resources.

## File Structure
- `ShopJavaSeliniumTest.java`: Main Java class implementing the test case.
- `data.xlsx`: Test data file containing item details (name, type, color, price).
- `screenshots/`: Directory to save screenshots for key test steps.

## Key Methods in Code
1. **searchItem**: Searches for the item and validates the results.
2. **selectItem**: Selects the desired item and handles optional attributes like color.
3. **addToCart**: Adds the item to the cart and handles popups using WebDriverWait.
4. **takeSnapShot**: Captures screenshots at critical steps for debugging.
5. **setExcelFile**: Reads test data from the Excel file using Apache POI.

## Results
- **Test Outcome**:
  - Verified that the item is successfully added to the cart with a confirmation message displayed.
- **Screenshots**:
  - Captured for each critical step, including search results, item selection, and cart addition.
- **Logs**:
  - Detailed logs generated for debugging and validation.

## Challenges and Solutions
- **Dynamic Elements**:
  - Managed with WebDriverWait and JavaScript Executor for stable interaction.
- **Verifying Test Results**:
  - Implemented TestNG assertions to validate outcomes dynamically.

## Future Improvements
1. Add test cases for checkout and payment flows.
2. Implement cross-browser testing with Chrome, Edge, and Safari.
3. Use DataProvider in TestNG for running tests with multiple datasets.
4. Generate HTML reports for better result visualization.

## How to Run the Test
1. Clone the repository and ensure all dependencies are installed using Maven.
2. Update the paths in `ShopJavaSeliniumTest.java` for:
   - Geckodriver.
   - `data.xlsx` file.
   - Screenshot directory.
3. Execute the test class using TestNG or directly via Maven (`mvn test`).

## References
- Selenium WebDriver Documentation: [SeleniumHQ](https://www.selenium.dev/documentation/)
- TestNG Documentation: [TestNG](https://testng.org/doc/)
- Apache POI Documentation: [Apache POI](https://poi.apache.org/)
