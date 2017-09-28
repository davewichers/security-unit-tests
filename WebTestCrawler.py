'''

WebTestCrawler
Clicks every link on the web app in order to run all tests programatically

NOTE: Requires Selenium and ChromeDriver

'''

from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By 
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
from sys import platform

# clicks all the test links for the given test category
def clickLinks(driver, testCount):
	for i in range(0, (testCount + 3)):
		links = driver.find_elements_by_xpath("//a[not(starts-with(@href, 'http'))]")

		# clicks all links except for the category links
		if (i > 2):
			waitForLoad(driver)
			links[i].click()
			clickLinksTestView(driver)


# clicks the links on the test view page
def clickLinksTestView(driver):

	# go through code view page
	waitForLoad(driver)
	driver.find_element_by_css_selector("a[href]").click()
	clickLinkCodeView(driver)

	# run test and return
	waitForLoad(driver)
	driver.find_element_by_css_selector("#theform > input[type='submit']").click()
	clickLinksResults(driver)


# clicks the back button on the code view page
def clickLinkCodeView(driver):
	waitForLoad(driver)
	driver.find_element_by_css_selector("button").click()


# clicks the back button on the results page
def clickLinksResults(driver):
	waitForLoad(driver)
	driver.find_element_by_css_selector("a[href]").click()


# waits for the page loads before clicking
def waitForLoad(driver):
	timeoutTime = 15
	try:
		WebDriverWait(driver, timeoutTime).until(EC.presence_of_element_located((By.TAG_NAME, "html")))
	except TimeoutException:
		print("Page \"" + driver.current_url +"\" took too long to load!")


# starts the crawler
if (platform == "linux" or platform == "linux2"):
	driver = webdriver.Chrome(executable_path="./chromedriver")
else:
	driver = webdriver.Chrome()
driver.get("http://localhost:8080/java-security-unit-tests/index.jsp")
clickLinks(driver, 51)
driver.get("http://localhost:8080/java-security-unit-tests/xpath.jsp")
clickLinks(driver, 23)
driver.get("http://localhost:8080/java-security-unit-tests/xquery.jsp")
clickLinks(driver, 15)
print("All tests have completed running")
