#!/bin/sh

pip install -U selenium

if [ -f chromedriver ]; then
	export PATH=$PATH:./chromedriver
fi

python WebTestCrawler.py
